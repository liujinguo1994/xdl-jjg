package com.xdl.jjg.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjg.trade.model.domain.EsCakeCardDO;
import com.xdl.jjg.entity.EsCakeCard;

/**
 * <p>
 * 优惠卷 Mapper 接口
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
public interface EsCakeCardMapper extends BaseMapper<EsCakeCard> {

    EsCakeCardDO getLowCode();

}
