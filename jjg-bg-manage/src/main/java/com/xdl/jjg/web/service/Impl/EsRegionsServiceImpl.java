package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjg.system.model.domain.EsRegionsDO;
import com.jjg.system.model.dto.EsRegionsDTO;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsRegions;
import com.xdl.jjg.mapper.EsRegionsMapper;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.web.service.IEsRegionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Service
public class EsRegionsServiceImpl extends ServiceImpl<EsRegionsMapper, EsRegions> implements IEsRegionsService {

    private static Logger logger = LoggerFactory.getLogger(EsRegionsServiceImpl.class);

    @Autowired
    private EsRegionsMapper regionsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 插入数据
     *
     * @param regionsDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult insertRegions(EsRegionsDTO regionsDTO) {
        try {
            if (regionsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsRegions regions = new EsRegions();
            BeanUtil.copyProperties(regionsDTO, regions);
            this.regionsMapper.insert(regions);
            EsRegions esRegions = regionsMapper.selectById(regions.getId());
            //设置path
            String regionPath = "";
            if (esRegions.getParentId() != null && esRegions.getParentId() != 0) {
                EsRegions p = regionsMapper.selectById(esRegions.getParentId());
                if (p == null) {
                    throw new ArgumentException(ErrorCode.PARENT_REGIONS_IS_NULL.getErrorCode(), "当前地区父地区id无效");
                }
                regionPath = p.getRegionPath() + regions.getId() + ",";
            } else {
                regionPath = "," + regions.getId() + ",";
            }
            //对地区级别进行处理
            String subreg = regionPath.substring(0, regionPath.length() - 1);
            subreg = subreg.substring(1);
            String[] regs = subreg.split(",");
            EsRegions es = new EsRegions();
            es.setId(regions.getId());
            es.setRegionGrade(regs.length);
            es.setRegionPath(regionPath);
            regionsMapper.updateById(es);
            //删除地区缓存
           /* Set<String> hkeys = jedisCluster.hkeys(CachePrefix.REGIONLIDEPTH.getPrefix()+"*");
            if (CollectionUtils.isNotEmpty(hkeys)){
                hkeys.stream().forEach(s -> {
                    jedisCluster.del(s);
                });
            }*/
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable ae) {
            logger.error("新增失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 根据条件更新数据
     *
     * @param regionsDTO DTO
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult updateRegions(EsRegionsDTO regionsDTO) {
        try {
            if (regionsDTO == null) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), ErrorCode.PARAM_ERROR.getErrorMsg());
            }
            EsRegions esRegions = regionsMapper.selectById(regionsDTO.getId());
            if (esRegions == null) {
                throw new ArgumentException(ErrorCode.REGIONS_IS_NULL.getErrorCode(), "当前地区不存在");
            }
            EsRegions parentRegion = regionsMapper.selectById(esRegions.getParentId());
            if (regionsDTO.getParentId() != 0 && parentRegion == null) {
                throw new ArgumentException(ErrorCode.PARENT_REGIONS_IS_NULL.getErrorCode(), "当前地区父地区id无效");
            }
            EsRegions regions = new EsRegions();
            BeanUtil.copyProperties(regionsDTO, regions);
            QueryWrapper<EsRegions> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRegions::getId, regionsDTO.getId());
            this.regionsMapper.update(regions, queryWrapper);
            //删除地区缓存
           /* Set<String> hkeys = jedisCluster.hkeys(CachePrefix.REGIONLIDEPTH.getPrefix()+"*");
            if (CollectionUtils.isNotEmpty(hkeys)){
                hkeys.stream().forEach(s -> {
                    jedisCluster.del(s);
                });
            }*/
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

    /**
     * 根据id获取详情
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     */
    @Override
    public DubboResult<EsRegionsDO> getRegions(Long id) {
        try {
            QueryWrapper<EsRegions> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRegions::getId, id);
            EsRegions regions = this.regionsMapper.selectOne(queryWrapper);
            EsRegionsDO regionsDO = new EsRegionsDO();
            if (regions == null) {
                throw new ArgumentException(ErrorCode.DATA_NOT_EXIST.getErrorCode(), ErrorCode.DATA_NOT_EXIST.getErrorMsg());
            }
            BeanUtil.copyProperties(regions, regionsDO);
            return DubboResult.success(regionsDO);
        } catch (ArgumentException ae) {
            logger.error("查询失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据查询列表
     *
     * @param regionsDTO DTO
     * @param pageSize   页码
     * @param pageNum    页数
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboPageResult<EsRegionsDO>
     */
    @Override
    public DubboPageResult<EsRegionsDO> getRegionsList(EsRegionsDTO regionsDTO, int pageSize, int pageNum) {
        QueryWrapper<EsRegions> queryWrapper = new QueryWrapper<>();
        try {
            // 查询条件

            Page<EsRegions> page = new Page<>(pageNum, pageSize);
            IPage<EsRegions> iPage = this.page(page, queryWrapper);
            List<EsRegionsDO> regionsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
                regionsDOList = iPage.getRecords().stream().map(regions -> {
                    EsRegionsDO regionsDO = new EsRegionsDO();
                    BeanUtil.copyProperties(regions, regionsDO);
                    return regionsDO;
                }).collect(Collectors.toList());
            }
            return DubboPageResult.success(regionsDOList);
        } catch (ArgumentException ae) {
            logger.error("分页查询失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("分页查询失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param id 主键id
     * @auther: rm 2817512105@qq.com
     * @date: 2019-06-04
     * @return: com.shopx.common.model.result.DubboResult<EsRegionsDO>
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DubboResult deleteRegions(Long id) {
        try {
            if (id == 0) {
                throw new ArgumentException(ErrorCode.PARAM_ERROR.getErrorCode(), String.format("参数传入错误ID不能为空[%s]", id));
            }
            QueryWrapper<EsRegions> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.lambda().eq(EsRegions::getId, id);
            this.regionsMapper.delete(deleteWrapper);
            //删除地区缓存
           /* Set<String> hkeys = jedisCluster.hkeys(CachePrefix.REGIONLIDEPTH.getPrefix()+"*");
            if (CollectionUtils.isNotEmpty(hkeys)){
                hkeys.stream().forEach(s -> {
                    jedisCluster.del(s);
                });
            }*/
            return DubboResult.success();
        } catch (ArgumentException ae) {
            logger.error("删除失败", ae);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("删除失败", th);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    //获取某地区的子地区
    @Override
    public DubboResult<List<EsRegionsDO>> getChildrenById(Long id) {
        try {
            EsRegions esRegions = regionsMapper.selectById(id);
            if (esRegions == null && id != 0) {
                throw new ArgumentException(ErrorCode.REGIONS_NOT_EXIT.getErrorCode(), "此地区不存在");
            }
            // String str = jedisCluster.get(CachePrefix.REGIONLIDEPTH.getPrefix() + id);
            List<EsRegions> regionsList = null;
            // 如果为空的话需要重数据库中查出数据 然后放入缓存
            // if (str == null) {
            QueryWrapper<EsRegions> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsRegions::getParentId, id);
            regionsList = regionsMapper.selectList(queryWrapper);
            //if (regionsList.size() !=0 ){
            // String s = JsonUtil.objectToJson(regionsList);
            // jedisCluster.set(CachePrefix.REGIONLIDEPTH.getPrefix() + id,s);
            // }
            // }else {
            // regionsList = JsonUtil.jsonToList(str, EsRegions.class);
            // }
            List<EsRegionsDO> esRegionsDOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(regionsList)) {
                //属性复制
                esRegionsDOList = regionsList.stream().map(regions -> {
                    EsRegionsDO esRegionsDO = new EsRegionsDO();
                    BeanUtil.copyProperties(regions, esRegionsDO);
                    return esRegionsDO;
                }).collect(Collectors.toList());
            }
            return DubboResult.success(esRegionsDOList);
        } catch (ArgumentException ae) {
            logger.error("获取某地区的子地区失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("获取某地区的子地区失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    @Override
    public DubboPageResult<EsRegionsDO> getRegionByDepth(Integer depth) {
        try {
            //如果深度大于4级，则修改深度为最深4级
            if (depth > 4) {
                depth = 4;
            }
            QueryWrapper<EsRegions> queryWrapper = new QueryWrapper<>();
            List<EsRegions> data = regionsMapper.selectList(queryWrapper);
            List<EsRegionsDO> tree = new ArrayList<>();
            this.sort(1, depth, tree, data);
            return DubboPageResult.success(tree);
        } catch (ArgumentException ae) {
            logger.error("根据深度获取组织地区数据结构的数据失败", ae);
            return DubboPageResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("根据深度获取组织地区数据结构的数据失败", th);
            return DubboPageResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }
    }

    /**
     * 负责递归的停止
     *
     * @param level 标示
     * @param depth 深度
     * @param tree  新的树结构
     * @param data  原始数据
     */
    private void sort(int level, int depth, List<EsRegionsDO> tree, List<EsRegions> data) {
        if (level + 1 > depth) {
            // 如果是第一级的情况直接返回
            if (depth == 1) {
                for (EsRegions regions : data) {
                    if (regions.getParentId() == 0) {
                        tree.add(regions.toDO());
                    }
                }
            }
            return;
        }
        // 如果为0 则代表初始化 初始化顶级数据
        if (level == 1) {
            for (EsRegions regions : data) {
                if (regions.getParentId() == 0) {
                    tree.add(regions.toDO());
                }
            }
        }
        this.recursion(level, tree, data);
        level++;
        this.sort(level, depth, tree, data);
    }

    /**
     * 负责树结构的创建
     *
     * @param level 标示
     * @param tree  树结构
     * @param data  原始数据
     */
    private void recursion(int level, List<EsRegionsDO> tree, List<EsRegions> data) {
        for (EsRegionsDO regionsDO : tree) {
            if (regionsDO.getRegionGrade() != level) {
                if (regionsDO.getChildren().size() != 0) {
                    this.recursion(level, regionsDO.getChildren(), data);
                }
                continue;
            }
            for (EsRegions regions : data) {
                if (regions.getParentId().equals(regionsDO.getId())) {
                    regionsDO.getChildren().add(regions.toDO());
                }
            }
        }
    }
}
