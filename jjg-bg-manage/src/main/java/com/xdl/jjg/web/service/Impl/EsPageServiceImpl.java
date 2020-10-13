package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsPage;
import com.xdl.jjg.mapper.EsPageMapper;
import com.xdl.jjg.model.domain.EsPageDO;
import com.xdl.jjg.model.dto.EsPageDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsPageCreateManagerService;
import com.xdl.jjg.web.service.IEsPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsPageServiceImpl extends ServiceImpl<EsPageMapper, EsPage> implements IEsPageService {

    private static Logger logger = LoggerFactory.getLogger(EsPageServiceImpl.class);

    @Autowired
    private EsPageMapper pageMapper;

    @Autowired
    private IEsPageCreateManagerService pageCreateManagerService;


    /**
     * 根据条件更新数据
     *
     * @param pageDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsPageDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updatePage(EsPageDTO pageDTO) {
        try {
            if (pageDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsPage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPage::getClientType, pageDTO.getClientType()).eq(EsPage::getPageType, pageDTO.getPageType());
            EsPage esPage = pageMapper.selectOne(queryWrapper);
            //首次插入
            if (esPage == null) {
                EsPage page = new EsPage();
                BeanUtil.copyProperties(pageDTO, page);
                pageMapper.insert(page);
            } else {
                EsPage page = new EsPage();
                BeanUtil.copyProperties(pageDTO, page);
                QueryWrapper<EsPage> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(EsPage::getId, pageDTO.getId());
                pageMapper.update(page, queryWrapper);
            }

            //发送mq消息
           /* String[] choosePages = {"INDEX"};
            pageCreateManagerService.create(choosePages);*/

            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //使用客户端类型和页面类型查询一个楼层
    @Override
    public DubboResult<EsPageDO> getByType(String clientType, String pageType) {
        try {
            QueryWrapper<EsPage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsPage::getClientType, clientType).eq(EsPage::getPageType, pageType);
            EsPage esPage = pageMapper.selectOne(queryWrapper);
            EsPageDO esPageDO = new EsPageDO();
            if (esPage != null) {
                BeanUtil.copyProperties(esPage, esPageDO);
            }
            return DubboResult.success(esPageDO);
        } catch (ArgumentException ae) {
            logger.error("使用客户端类型和页面类型查询一个楼层失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("使用客户端类型和页面类型查询一个楼层失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }
}
