package com.xdl.jjg.staticpage;

import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.JsonUtil;
import com.shopx.goods.api.model.domain.EsGoodsDO;
import com.shopx.goods.api.service.IEsGoodsService;
import com.shopx.system.api.constant.TaskProgressConstant;
import com.shopx.system.api.model.domain.EsArticleDO;
import com.shopx.system.api.model.domain.EsSettingsDO;
import com.shopx.system.api.model.domain.TaskProgress;
import com.shopx.system.api.model.domain.vo.EsPageSettingVO;
import com.shopx.system.api.model.enums.ClientType;
import com.shopx.system.api.model.enums.PageCreatePrefixEnum;
import com.shopx.system.api.model.enums.SettingGroup;
import com.shopx.system.api.service.IEsArticleService;
import com.shopx.system.api.service.IEsProgressManagerService;
import com.shopx.system.api.service.IEsSettingsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.List;

/**
 * 静态页生成实现
 */
@Component
public class PageCreatorImpl implements PageCreator {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private IEsProgressManagerService progressManager;

    @Autowired
    private IEsSettingsService settingsService;

    @Reference(version = "${dubbo.application.version}",timeout = 5000,check = false)
    private IEsGoodsService goodsService;

    @Autowired
    private IEsArticleService articleService;

    @Override
    public void createOne(String path, String type, String name) throws Exception {
        //生成消息
        String url = getUrl(path, type);
        //通过http 来获取html存储redis
        String html = this.getHTML(url, type);
        jedisCluster.set(name,html);

    }

    @Override
    public void createAll() throws Exception {
        //生成商品信息页面
        this.createGoods();
        //生成商城首页
        this.createIndex();
        //生成帮助中心页面
        this.createHelp();
    }

    @Override
    public void createIndex() throws Exception {
        //生成静态页
        this.createOne("/", ClientType.PC.name(), "/" + ClientType.PC.name() + "/");
        this.createOne("/", ClientType.WAP.name(), "/" + ClientType.WAP.name() + "/");
        this.showMessage("/");
        this.createOne("/index.html", ClientType.PC.name(), "/" + ClientType.PC.name() + "/index.html");
        this.createOne("/index.html", ClientType.WAP.name(), "/" + ClientType.WAP.name() + "/index.html");
        this.showMessage("/index.html");
    }

    @Override
    public void createGoods() throws Exception{
        //为了防止首页有相关商品，所以需要生成首页一次
        this.createIndex();
        //商品总数
        int goodsCount = Integer.parseInt(goodsService.adminGetEsGoodsCount().getData().toString());
        //100条查一次
        int pageSize = 100;
        int pageCount = 0;
        pageCount = goodsCount / pageSize;
        pageCount = goodsCount % pageSize > 0 ? pageCount + 1 : pageCount;
        for (int i = 1; i <= pageCount; i++) {
            //100条查一次
            DubboPageResult<EsGoodsDO> result = goodsService.adminGetEsGoodsList(pageSize, i);
            List<EsGoodsDO> list = result.getData().getList();
            for (EsGoodsDO esGoodsDO : list) {
                int goodsId = Integer.valueOf(esGoodsDO.getId().toString());
                //商品页面名称
                String pageName = PageCreatePrefixEnum.GOODS.getHandlerGoods(goodsId);
                //生成静态页
                this.createOne(pageName, ClientType.PC.name(), "/" + ClientType.PC.name() + pageName);
                this.createOne(pageName, ClientType.WAP.name(), "/" + ClientType.WAP.name() + pageName);
                this.showMessage(pageName);
            }
        }
    }


    @Override
    public void createHelp() throws Exception {
        //帮助中心页面
        int helpCount = articleService.articleCount().getData();
        // 100条查一次
        int pageSize = 100;
        int pageCount = 0;
        pageCount = helpCount / pageSize;
        pageCount = helpCount % pageSize > 0 ? pageCount + 1 : pageCount;
        for (int i = 1; i <= pageCount; i++) {
            //获取数据
            DubboPageResult<EsArticleDO> result = articleService.getList(pageSize, i);
            List<EsArticleDO> articleDOList = result.getData().getList();
            for (EsArticleDO esArticleDO : articleDOList) {
                int articleId = Integer.valueOf(esArticleDO.getId().toString());
                String pageName = (PageCreatePrefixEnum.HELP.getHandlerHelp(articleId));
                //生成静态页
                String name = "/" + ClientType.PC.name() + pageName;
                this.createOne(pageName, ClientType.PC.name(), name);
                this.showMessage(pageName);
            }
        }
    }

    /**
     * 传入url 返回对应页面的html
     *
     * @param url  页面的url
     * @param type 客户端类型
     * @return 返回对应页面的html
     * @throws ClientProtocolException
     * @throws IOException
     */
    private String getHTML(String url, String type) throws ClientProtocolException, IOException {
        String html = "null";
        // socket超时  connect超时
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000)
                .setConnectTimeout(50000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Client-Type", type);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        html = EntityUtils.toString(response.getEntity(), "utf-8");
        return html;
    }

    /**
     * 获取url
     *
     * @param path 路径
     * @param type 客户端类型
     * @return url
     */
    private String getUrl(String path, String type) {
        DubboResult<EsSettingsDO> result = settingsService.getByCfgGroup(SettingGroup.PAGE.name());
        EsPageSettingVO settingVO = JsonUtil.jsonToObject(result.getData().getCfgValue(), EsPageSettingVO.class);

        String url = settingVO.getPcAddress() + "/" + path;
        if (type.equals(ClientType.WAP.name())) {
            url = settingVO.getWapAddress() + "/" + path;
        }
        return url;
    }

    /**
     * 生成消息
     *
     * @param path 路径
     */
    private void showMessage(String path) {
        try {
            DubboResult<TaskProgress> result = progressManager.getProgress(TaskProgressConstant.PAGE_CREATE);
            TaskProgress tk = result.getData();
            tk.step("正在生成[" + path + "]");
            progressManager.putProgress(TaskProgressConstant.PAGE_CREATE, tk);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
