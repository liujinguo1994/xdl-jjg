package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.member.model.domain.EsClerkDO;
import com.jjg.member.model.domain.EsClerkListDO;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.domain.EsShopRoleDO;
import com.jjg.member.model.dto.ClerkQueryParamDTO;
import com.jjg.member.model.dto.EsClerkDTO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.xdl.jjg.constant.MemberConstant;
import com.xdl.jjg.constant.MemberErrorCode;
import com.xdl.jjg.entity.EsClerk;
import com.xdl.jjg.entity.EsMember;
import com.xdl.jjg.mapper.EsClerkMapper;
import com.xdl.jjg.mapper.EsMemberMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsClerkService;
import com.xdl.jjg.web.service.IEsMemberService;
import com.xdl.jjg.web.service.IEsShopRoleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * <p>
 * 店员 服务实现类
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-04
 */
@Service
public class EsClerkServiceImpl extends ServiceImpl<EsClerkMapper, EsClerk> implements IEsClerkService {

    private static Logger logger = LoggerFactory.getLogger(EsClerkServiceImpl.class);

    @Autowired
    private EsClerkMapper clerkMapper;
    @Autowired
    private IEsShopRoleService iEsShopRoleService;
    @Autowired
    private IEsMemberService iEsMemberService;
    @Autowired
    private EsMemberMapper memberMapper;

    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;

    /**
     * 插入店员数据
     *
     * @param clerkDTO 店员DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 9:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertClerk(EsClerkDTO clerkDTO) {
        try {
            if(StringUtils.isBlank(clerkDTO.getMobile())){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            QueryWrapper<EsMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsMember::getState, 0);
            queryWrapper.lambda().eq(EsMember::getMobile, clerkDTO.getMobile());
            List<EsMember> memberList =  this.memberMapper.selectList(queryWrapper);
            if(CollectionUtils.isNotEmpty(memberList)){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(),"该店员已存在请勿重复提交["+clerkDTO.getMobile()+"]");
            }
            clerkDTO.setState(MemberConstant.IsDefault);
            DubboResult<EsShopRoleDO> shopRole = iEsShopRoleService.getShopRole(clerkDTO.getRoleId());
            EsShopRoleDO shopRoleDO = shopRole.getData();
            if (shopRoleDO == null) {
                throw new ArgumentException(MemberErrorCode.CUSTOM_NOT_ROLE.getErrorCode(), MemberErrorCode.CUSTOM_NOT_ROLE.getErrorMsg());
            }
            EsClerk clerk = new EsClerk();
            BeanUtil.copyProperties(clerkDTO, clerk);
            clerk.setIsAdmin(0);

            DubboResult<EsMemberDO> esMemberDODubboResult = iEsMemberService.getMemberInfoByMobile(clerkDTO.getMobile());
            EsMemberDO esMember = null;
            if(esMemberDODubboResult.isSuccess() && esMemberDODubboResult.getData() != null){
                esMember = esMemberDODubboResult.getData();
            }
            if(esMember == null){
                //插入会员表
                EsMemberDTO esMemberDTO = new EsMemberDTO();
                esMemberDTO.setEmail(clerkDTO.getEmail());
                esMemberDTO.setMobile(clerkDTO.getMobile());
                esMemberDTO.setCardName(clerkDTO.getClerkName());

                String salt = ShiroKit.getRandomSalt(ENLENGTH);
                String passWord = ShiroKit.md5(clerkDTO.getPassword(), esMemberDTO.getCardName() + salt);
                esMemberDTO.setPassword(passWord);
                esMemberDTO.setSalt(salt);
                esMemberDTO.setName(clerkDTO.getClerkName());
                DubboResult<EsMemberDO> esMemberDOResult = iEsMemberService.insertMember(esMemberDTO);
                if(!esMemberDOResult.isSuccess()){
                    return DubboResult.fail(MemberErrorCode.FAIL_ADD_MEMBER.getErrorCode(), MemberErrorCode.FAIL_ADD_MEMBER.getErrorMsg());
                }
                esMember = esMemberDOResult.getData();
            }

            clerk.setMemberId(esMember.getId());
            if(clerk.getRoleId().equals(0l)){
                return DubboResult.fail(MemberErrorCode.UNABLE_ADD_SUPER.getErrorCode(), MemberErrorCode.UNABLE_ADD_SUPER.getErrorMsg());
            }
            this.clerkMapper.insert(clerk);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店员新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable ae) {
            logger.error("店员新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店员数据
     *
     * @param clerkDTO 店员DTO
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 9:50:20
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateClerk(EsClerkDTO clerkDTO) {
        try {
            EsClerk esClerk = this.clerkMapper.selectById(clerkDTO.getId());
            if (esClerk==null){
                return DubboResult.fail(MemberErrorCode.NOTEXIST_CLERK.getErrorCode(), MemberErrorCode.NOTEXIST_CLERK.getErrorMsg());
            }
            EsClerk clerk = new EsClerk();
            BeanUtil.copyProperties(clerkDTO, clerk);
            QueryWrapper<EsClerk> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsClerk::getId, clerkDTO.getId());
            this.clerkMapper.update(clerk, queryWrapper);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店员更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店员更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新店员数据
     *
     * @param clerkDTO 店员DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/06/28 9:50:20
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateSellerClerk(EsClerkDTO clerkDTO) {
        QueryWrapper<EsClerk> queryWrapper=new QueryWrapper<>();
        try {
            //获取该店员信息
            EsClerk esClerk = clerkMapper.selectById(clerkDTO.getId());
            if(null == esClerk){
                throw new ArgumentException(MemberErrorCode.NOTEXIST_CLERK.getErrorCode(), MemberErrorCode.NOTEXIST_CLERK.getErrorMsg());
            }

            queryWrapper.lambda().eq(EsClerk::getShopId,clerkDTO.getShopId()).eq(EsClerk::getId,clerkDTO.getId());
            EsClerk clerk = new EsClerk();
            BeanUtil.copyProperties(clerkDTO, clerk);

            //校验权限，超级管理员不能修改成为普通管理员，普通管理员也不能修改成为超级管理员
            if((clerkDTO.getRoleId().equals(1l) && esClerk.getRoleId().equals(0l)) || (clerkDTO.getRoleId().equals(0l) && esClerk.getRoleId().equals(1l))){
                throw new ArgumentException(MemberErrorCode.AUTHORITY.getErrorCode(), MemberErrorCode.AUTHORITY.getErrorMsg());
            }
            this.clerkMapper.update(clerk, queryWrapper);

            //获取会员信息
            EsMemberDO esMember = memberMapper.selectById(esClerk.getMemberId());
            if(null == esMember){
                throw new ArgumentException(MemberErrorCode.MEMBER_NOT_EXIST.getErrorCode(), MemberErrorCode.MEMBER_NOT_EXIST.getErrorMsg());
            }
            //更新会员表信息
            EsMemberDTO esMemberDTO = new EsMemberDTO();
            esMemberDTO.setEmail(clerkDTO.getEmail());
            esMemberDTO.setMobile(clerkDTO.getMobile());
            esMemberDTO.setId(esClerk.getMemberId());

            if(esMemberDTO.getPassword() != null){
                String salt = ShiroKit.getRandomSalt(ENLENGTH);
                String passWord = ShiroKit.md5(esMemberDTO.getPassword(), esMemberDTO.getCardName() + salt);
                esMemberDTO.setPassword(passWord);
                esMemberDTO.setSalt(salt);
            }

            iEsMemberService.updateMember(esMemberDTO);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店员更新失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        } catch (Throwable th) {
            logger.error("店员更新失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据id获取店员详情
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 10:00:16
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    @Override
    public DubboResult<EsClerkDO> getClerk(Long id) {
        try {
            if (id==null){
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsClerk clerk = this.clerkMapper.selectById(id);
            EsClerkDO clerkDO = new EsClerkDO();
            if (clerk == null) {
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(), MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(clerk, clerkDO);
            EsMemberDO esMember = memberMapper.selectById(clerk.getMemberId());
            if(esMember != null){
                clerkDO.setMobile(esMember.getMobile());
                clerkDO.setEmail(esMember.getEmail());
                clerkDO.setClerkName(esMember.getCardName()==null ? clerk.getClerkName() : esMember.getCardName());
                clerkDO.setPassword(esMember.getPassword());
            }
            return DubboResult.success(clerkDO);
        } catch (ArgumentException ae){
            logger.error("店员查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店员查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    @Override
    public DubboResult<EsClerkDO> getByMemberId(Long memberId) {
        try {
            QueryWrapper<EsClerk> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(EsClerk::getMemberId,memberId);
            EsClerk clerk = this.clerkMapper.selectOne(queryWrapper);
            if (clerk == null) {
                return DubboResult.success();
            }
            EsClerkDO clerkDO = new EsClerkDO();
            BeanUtil.copyProperties(clerk, clerkDO);
            return DubboResult.success(clerkDO);
        } catch (ArgumentException ae){
            logger.error("店员查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店员查询失败", th);
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询店员列表
     *
     * @param clerkQueryParamDTO 店员查询
     * @param pageSize     页码
     * @param pageNum      页数
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/06/28 11:03:53
     * @return: com.shopx.common.model.result.DubboPageResult<EsClerkDO>
     */
    @Override
    public DubboPageResult<EsClerkListDO> getClerkList(ClerkQueryParamDTO clerkQueryParamDTO, int pageSize, int pageNum) {
        try{
            IPage<EsClerkListDO> page = this.clerkMapper.getAllClerk(new Page(pageNum,pageSize),clerkQueryParamDTO);
            return DubboPageResult.success(page.getTotal(),page.getRecords());
        } catch (ArgumentException ae){
            logger.error("获取分类绑定的品牌，包括未选中的", ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("获取分类绑定的品牌，包括未选中的", th);
            return DubboPageResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除店员数据
     *
     * @param id 主键id
     * @auther: yuanj 595831329@qq.com
     * @date: 2019/03/28 10:10:34
     * @return: com.shopx.common.model.result.DubboResult<EsClerkDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteClerk(Long id, Long shopId) {
        try {
            if (id == null || shopId == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsClerk esClerk = this.clerkMapper.selectById(id);
            if(null == esClerk){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(),MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            if (!esClerk.getShopId().equals(shopId)){
                throw new ArgumentException(MemberErrorCode.CLERK_ERROR.getErrorCode(), MemberErrorCode.CLERK_ERROR.getErrorMsg());
            }

            if (esClerk.getIsAdmin().equals(MemberConstant.isAdmin)) {
                throw new ArgumentException(MemberErrorCode.ERROE_ADMIN.getErrorCode(), MemberErrorCode.ERROE_ADMIN.getErrorMsg());
            }
            clerkMapper.updateState(MemberConstant.unable, id);
            return DubboResult.success();
        } catch (ArgumentException ae){
             logger.error("店员删除失败", ae);
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店员删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboResult recovery(Long id, Long shopId) {
        try{
            if (id == null || shopId == null) {
                throw new ArgumentException(MemberErrorCode.PARAM_ERROR.getErrorCode(), MemberErrorCode.PARAM_ERROR.getErrorMsg());
            }

            EsClerk esClerk=this.clerkMapper.selectById(id);
            if(null == esClerk){
                throw new ArgumentException(MemberErrorCode.DATA_NOT_EXIST.getErrorCode(),MemberErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }

            if (!esClerk.getShopId().equals(shopId)){
                throw new ArgumentException(MemberErrorCode.CLERK_ERROR.getErrorCode(), MemberErrorCode.CLERK_ERROR.getErrorMsg());
            }

            if (esClerk.getIsAdmin().equals(MemberConstant.isAdmin)) {
                throw new ArgumentException(MemberErrorCode.ERROE_ADMIN.getErrorCode(), MemberErrorCode.ERROE_ADMIN.getErrorMsg());
            }

            clerkMapper.updateState(MemberConstant.enable, id);
            return DubboResult.success();
        } catch (ArgumentException ae){
            logger.error("店员恢复失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage());
        }  catch (Throwable th) {
            logger.error("店员恢复失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(MemberErrorCode.SYS_ERROR.getErrorCode(), MemberErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
}
