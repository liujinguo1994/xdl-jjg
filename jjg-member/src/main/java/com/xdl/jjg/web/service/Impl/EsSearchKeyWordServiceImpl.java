package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.member.api.model.domain.EsSearchKeyWordDO;
import com.shopx.member.api.model.domain.dto.EsSearchKeyWordDTO;
import com.shopx.member.api.service.IEsSearchKeyWordService;
import com.xdl.jjg.entity.EsSearchKeyWord;
import com.shopx.member.dao.mapper.EsSearchKeyWordMapper;
import com.shopx.system.api.constant.ErrorCode;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-10-26
 */
@Service
public class EsSearchKeyWordServiceImpl extends ServiceImpl<EsSearchKeyWordMapper, EsSearchKeyWord> implements IEsSearchKeyWordService {

    private static Logger logger = LoggerFactory.getLogger(EsSearchKeyWordServiceImpl.class);

    @Autowired
    private EsSearchKeyWordMapper searchKeyWordMapper;

    /**
     * 插入数据
     *
     * @param searchKeyWordDTO DTO
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertSearchKeyWord(EsSearchKeyWordDTO searchKeyWordDTO) {
        try {
            if (searchKeyWordDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsSearchKeyWord searchKeyWord = new EsSearchKeyWord();
            BeanUtil.copyProperties(searchKeyWordDTO, searchKeyWord);

            this.searchKeyWordMapper.insert(searchKeyWord);
            return DubboResult.success();
        } catch (Throwable ae) {
            logger.error("失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据查询列表
     *
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSearchKeyWordDO>
     */
    @Override
    public DubboPageResult getSearchKeyWordList(Long memberId) {
        QueryWrapper<EsSearchKeyWord> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsSearchKeyWord::getMemberId,memberId)
                    .groupBy(EsSearchKeyWord::getSearchKeyword)
                    .orderByDesc(EsSearchKeyWord::getId);
            queryWrapper.last("LIMIT 15");
            List<EsSearchKeyWord> esSearchKeyWords = this.searchKeyWordMapper.selectList(queryWrapper);
            List<EsSearchKeyWordDO> searchKeyWordDOList = new ArrayList<>();

            searchKeyWordDOList = esSearchKeyWords.stream().map(searchKeyWord -> {
                EsSearchKeyWordDO searchKeyWordDO = new EsSearchKeyWordDO();
                BeanUtil.copyProperties(searchKeyWord, searchKeyWordDO);
                return searchKeyWordDO;
            }).collect(Collectors.toList());
            return DubboPageResult.success(searchKeyWordDOList);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据会员ID 获取该会员的前三条 查询历史
     *
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/06/03 13:42:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsSearchKeyWordDO>
     */
    @Override
    public DubboPageResult getSearchKeyWord(Long memberId) {
        QueryWrapper<EsSearchKeyWord> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件
            queryWrapper.lambda().eq(EsSearchKeyWord::getMemberId,memberId)
                    .orderByDesc(EsSearchKeyWord::getId);
            queryWrapper.last("LIMIT 3");
            List<EsSearchKeyWord> esSearchKeyWords = this.searchKeyWordMapper.selectList(queryWrapper);
            List<String> collect = esSearchKeyWords.stream().map(EsSearchKeyWord::getSearchKeyword).collect(Collectors.toList());

            return DubboPageResult.success(collect);
        } catch (Throwable th) {
            logger.error("查询分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: LiuJG 344009799@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboResult<EsSearchKeyWordDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteSearchKeyWord(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsSearchKeyWord> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSearchKeyWord::getId, id);
            this.searchKeyWordMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult deleteSearchKeyWordBatch(Long memberId) {
        try {
            if (memberId == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误memberId不能为空[%s]", memberId));
            }
            QueryWrapper<EsSearchKeyWord> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsSearchKeyWord::getMemberId, memberId);
            this.searchKeyWordMapper.delete(deleteWrapper);
            return DubboResult.success();
        } catch (Throwable th) {
            logger.error("查询删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
