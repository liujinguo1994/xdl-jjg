package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsMember;
import com.xdl.jjg.entity.EsMemberRfmConfig;
import com.xdl.jjg.mapper.EsMemberMapper;
import com.xdl.jjg.mapper.EsMemberRfmConfigMapper;
import com.xdl.jjg.model.domain.EsMemberDO;
import com.xdl.jjg.model.domain.EsMemberRfmConfigDO;
import com.xdl.jjg.model.domain.EsRFMTradeDO;
import com.xdl.jjg.model.dto.EsMemberDTO;
import com.xdl.jjg.model.dto.EsRFMTradeDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsGrowthWeightConfigService;
import com.xdl.jjg.web.service.IEsMemberRfmConfigService;
import com.xdl.jjg.web.service.IEsMemberService;
import com.xdl.jjg.web.service.IEsRFMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * RFM操作
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-25
 */
@Service
public class IEsRFMServiceImpl implements IEsRFMService {
    private static Logger logger = LoggerFactory.getLogger(IEsRFMServiceImpl.class);
    @Autowired
    private EsMemberMapper esMemberMapper;
    @Autowired
    private EsMemberRfmConfigMapper esMemberRfmConfigMapper;
    @Reference(version = "${dubbo.application.version}", timeout = 50000,check = false)
    private IEsMemberRfmConfigService iEsMemberRfmConfigService;
    @Autowired
    private IEsGrowthWeightConfigService iEsGrowthWeightConfigService;
    @Autowired
    private IEsMemberService iEsMemberService;


    /**
     * 查询所有会员下单信息
     *
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     * @return: com.shopx.common.model.result.DubboPageResult<EsRFMTradeDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboPageResult<EsRFMTradeDO> getOrderRfmInfo() {


        return null;
    }

    /**
     * 修改会员表成长值
     *
     * @param esRFMTradeDTOList DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateMemberGrowthValue(List<EsRFMTradeDTO> esRFMTradeDTOList) {
        try {
            for (EsRFMTradeDTO esRFMTradeDTO : esRFMTradeDTOList) {
                if (null == esRFMTradeDTO || null == esRFMTradeDTO.getMemberId()) {
                    continue;
                }
                EsMember esMember = this.getMember(esRFMTradeDTO.getMemberId());
                if (null == esMember) {
                    continue;
                }
                Integer growthValue = this.getRfmInfoByRecency(esRFMTradeDTO);
                DubboResult<EsMemberDO> resultMember = iEsMemberService.getAdminMember(esRFMTradeDTO.getMemberId());
                if(!resultMember.isSuccess() || resultMember.getData() == null){
                    continue;
                }
                Integer  gradeValue = resultMember.getData().getGrade();
                if( null == gradeValue){
                     gradeValue = 0;
                }
               Integer resultValue = gradeValue + growthValue;
                EsMemberDTO esMemberDTO = new EsMemberDTO();
                esMemberDTO.setId(esRFMTradeDTO.getMemberId());
                esMemberDTO.setGrade(resultValue);
                DubboResult result = this.updateMemberGrade(esMemberDTO);
                if (!result.isSuccess()) {
                    throw new ArgumentException(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
                }
            }
        } catch (ArgumentException ae) {
            logger.error("跟新会员表失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新会员表失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
        return DubboResult.success();
    }


    /**
     * 判断会员是否存在
     *
     * @param memberId DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     */
    public EsMember getMember(Long memberId) {
        QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
        try {
            queryWrapper.lambda().eq(EsMember::getId, memberId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return esMemberMapper.selectOne(queryWrapper);
    }

    /**
     * 依据消费天数查询成长值详细信息
     *
     * @param esRFMTradeDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     */
    public Integer getRfmInfoByRecency(EsRFMTradeDTO esRFMTradeDTO) {
        Integer growthValue = 0;
        List<EsMemberRfmConfigDO> frequencyList = new ArrayList<>();
        List<EsMemberRfmConfigDO> monetaryList = new ArrayList<>();
        try {
            DubboPageResult<EsMemberRfmConfigDO> result = iEsMemberRfmConfigService.getMemberRfmConfigListInfo();
            EsMemberRfmConfigDO resultOut = result.getData().getList().get(result.getData().getList().size()-1);
            if (result.isSuccess()) {
                if (CollectionUtils.isNotEmpty(result.getData().getList())) {
                    result.getData().getList().stream().forEach(esMemberRfmConfigDO -> {
                        if (null != esMemberRfmConfigDO.getFrequency() && esRFMTradeDTO.getFrequency() >= esMemberRfmConfigDO.getFrequency()) {
                            frequencyList.add(esMemberRfmConfigDO);
                        }
                        if (null != esMemberRfmConfigDO.getRecency()) {
                            Integer value = esRFMTradeDTO.getMonetary().compareTo((esMemberRfmConfigDO.getMonetary()).doubleValue());
                            if (value >= 0) {
                                monetaryList.add(esMemberRfmConfigDO);
                            }
                        }
                    });
                }
            }
            //降序排序 mvn clean package -DskipTest -U
            Collections.sort(frequencyList, new Comparator<EsMemberRfmConfigDO>() {
                @Override
                public int compare(EsMemberRfmConfigDO o1, EsMemberRfmConfigDO o2) {
                    return o2.getFrequency().compareTo(o1.getFrequency());
                }
            });
            //降序排序
            Collections.sort(monetaryList, new Comparator<EsMemberRfmConfigDO>() {
                @Override
                public int compare(EsMemberRfmConfigDO o1, EsMemberRfmConfigDO o2) {
                    return o2.getMonetary().compareTo(o1.getMonetary());
                }
            });
            QueryWrapper<EsMemberRfmConfig> queryWrapperRecency = new QueryWrapper<>();
            QueryWrapper<EsMemberRfmConfig> queryWrapperFrequency = new QueryWrapper<>();
            QueryWrapper<EsMemberRfmConfig> queryWrapperMonetary = new QueryWrapper<>();

            if( null != esRFMTradeDTO.getSign()){
                queryWrapperRecency.lambda().eq(EsMemberRfmConfig::getRecency, resultOut.getRecency());
                queryWrapperFrequency.lambda().eq(EsMemberRfmConfig::getFrequency,resultOut.getFrequency());
                queryWrapperMonetary.lambda().eq(EsMemberRfmConfig::getMonetary, resultOut.getMonetary());
            }else {
                queryWrapperRecency.lambda().eq(EsMemberRfmConfig::getRecency, esRFMTradeDTO.getRecency());
                queryWrapperFrequency.lambda().eq(EsMemberRfmConfig::getFrequency,Integer.valueOf(frequencyList.get(0).getFrequency()));
                queryWrapperMonetary.lambda().eq(EsMemberRfmConfig::getMonetary, monetaryList.get(0).getMonetary());
            }
            EsMemberRfmConfig esMemberRfmRecency = this.esMemberRfmConfigMapper.selectOne(queryWrapperRecency);
            EsMemberRfmConfig esMemberRfmFrequency = this.esMemberRfmConfigMapper.selectOne(queryWrapperFrequency);
            EsMemberRfmConfig esMemberRfMonetary = this.esMemberRfmConfigMapper.selectOne(queryWrapperMonetary);
            growthValue = esMemberRfmRecency.getFrequencyGrowthValue() + esMemberRfMonetary.getMonetaryGrowthValue() + esMemberRfmFrequency.getRecencyGrowthValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return growthValue;
    }

    /**
     * 依据消费天数查询成长值详细信息
     *
     * @param esMemberDTO DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:40:44
     */
    public DubboResult updateMemberGrade(EsMemberDTO esMemberDTO) {
        try {
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getId, esMemberDTO.getId());
            EsMember esMember = new EsMember();
            BeanUtil.copyProperties(esMemberDTO, esMember);
            this.esMemberMapper.update(esMember, queryWrapper);
        } catch (ArgumentException ae) {
            logger.error("跟新会员表失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("更新会员表失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
        return DubboResult.success();
    }


}