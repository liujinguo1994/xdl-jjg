package com.xdl.jjg.transaction;


import com.jjg.trade.model.dto.EsTradeDTO;
import com.xdl.jjg.config.RocketMqAddress;
import com.xdl.jjg.manager.TradeManager;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


/**
 * @author LIUJG
 * @Description: 分布式事务RocketMQ 生产者
 * @date 2019/9/27 下午11:40
 */
@Slf4j
@Component
@Configuration
public class TransactionProducer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 需要自定义事务监听器 用于 事务的二次确认 和 事务回查
     */

    private TransactionListener transactionListener ;

    /**
     * 这里的生产者和之前的不一样
     */
    private TransactionMQProducer producer = null;

    /**
     * 官方建议自定义线程 给线程取自定义名称 发现问题更好排查
     */
    private ExecutorService executorService = new ThreadPoolExecutor(20, 500, 100, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        }

    });

    public TransactionProducer(@Autowired RocketMqAddress rocketMqAddress, @Autowired TradeManager tradeManager) {
        transactionListener = new TransactionListenerImpl(tradeManager);
        // 初始化 事务生产者
        producer = new TransactionMQProducer(rocketMqAddress.getTradeGroup());
        // 添加服务器地址
        producer.setNamesrvAddr(rocketMqAddress.getNamesrv());
        // 添加事务监听器
        producer.setTransactionListener(transactionListener);
        // 添加自定义线程池
        producer.setExecutorService(executorService);

        start();
    }

    public TransactionMQProducer getProducer() {
        return this.producer;
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public void shutdown() {
        this.producer.shutdown();
    }
}

/**
 * @author LIUJG
 * @Description: 自定义事务监听器
 *
 */
@Slf4j
class TransactionListenerImpl implements TransactionListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TradeManager TradeManager ;

    public TransactionListenerImpl(TradeManager TradeManager) {
        this.TradeManager = TradeManager;
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("=========本地事务开始执行=============");
        String message = new String(msg.getBody());
        String bizUniNo = msg.getUserProperty("bizUniNo");
        logger.info("消息bizUniNo：[{}]",bizUniNo);

        EsTradeDTO esTradeDTO = JsonUtil.jsonToObject(message, EsTradeDTO.class);

        /**
         * 本地事务执行会有三种可能
         * 1、commit 成功
         * 2、Rollback 失败
         * 3、网络等原因服务宕机收不到返回结果
         */
        long start = System.currentTimeMillis();
        logger.info("生成订单开始时间：[{}]",start);
        ApiResponse trade = TradeManager.createTrade(esTradeDTO);
        long end = System.currentTimeMillis();
        logger.info("生成订单结束时间：[{}]；生成订单耗时--->[{}]",end,end-start);
        int result = trade.getStatus();
        logger.info("生成订单返回状态码：[{}]",result);
        //1、二次确认消息，然后消费者可以消费
        if (result == 0) {
            logger.info("本地事务执行成功[{}]", LocalTransactionState.COMMIT_MESSAGE);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        //2、回滚消息，Broker端会删除半消息
        else if (result == 31000) {
            logger.info("本地事务执行失败[{}]", LocalTransactionState.ROLLBACK_MESSAGE);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        //3、Broker端会进行回查消息
        else {
            return LocalTransactionState.UNKNOW;
        }
    }

    /**
     * 只有上面接口返回 LocalTransactionState.UNKNOW 才会调用查接口被调用
     *
     * @param msg 消息
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("==========回查接口=========");
        String key = msg.getKeys();
        logger.info("消息Key[{}]",key);
        String bizUniNo = msg.getUserProperty(key);
        logger.info("消息bizUniNo：[{}]",bizUniNo);
        // 1、必须根据key先去检查本地事务消息是否完成。
        /**
         * 因为有种情况就是：上面本地事务执行成功了，但是return LocalTransactionState.COMMIT_MESSAG的时候
         * 服务挂了，那么最终 Brock还未收到消息的二次确定，还是个半消息 ，所以当重新启动的时候还是回调这个回调接口。
         * 如果不先查询上面本地事务的执行情况 直接在执行本地事务，那么就相当于成功执行了两次本地事务了。
         */
        //  2、这里返回要么commit 要么rollback。没有必要在返回 UNKNOW
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
