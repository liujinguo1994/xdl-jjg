package com.xdl.jjg.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsLogiCompany;
import com.xdl.jjg.mapper.EsLogiCompanyMapper;
import com.xdl.jjg.model.form.ExpressPlatform;
import com.xdl.jjg.model.vo.EsConfigItemVO;
import com.xdl.jjg.model.vo.EsRadioOptionVO;
import com.xdl.jjg.model.vo.ExpressDetailVO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.util.HttpRequest;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.util.MD5;
import com.xdl.jjg.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * showapi 快递实现
 *
 * @author zh
 * @version v7.0
 * @date 18/7/11 下午3:52
 * @since v7.0
 */
@Component
public class Kuaidi100Plugin extends ServiceImpl<EsLogiCompanyMapper, EsLogiCompany> implements ExpressPlatform {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public List<EsConfigItemVO> definitionConfigItem() {

            List<EsConfigItemVO> list = new ArrayList<>();
            EsConfigItemVO codeItem = new EsConfigItemVO();
            codeItem.setName("code");
            codeItem.setText("公司代码");
            codeItem.setType("text");

            EsConfigItemVO secretItem = new EsConfigItemVO();
            secretItem.setName("id");
            secretItem.setText("id");
            secretItem.setType("text");

            EsConfigItemVO typeItem = new EsConfigItemVO();
            typeItem.setName("user");
            typeItem.setText("用户类型");
            typeItem.setType("radio");
            //组织用户类型可选项
            List<EsRadioOptionVO> options = new ArrayList<>();
            EsRadioOptionVO radioOption = new EsRadioOptionVO();
            radioOption.setLabel("普通用户");
            radioOption.setValue(0);
            options.add(radioOption);
            radioOption = new EsRadioOptionVO();
            radioOption.setLabel("企业用户");
            radioOption.setValue(1);
            options.add(radioOption);
            typeItem.setOptions(options);

            list.add(codeItem);
            list.add(secretItem);
            list.add(typeItem);
            return list;

    }

    @Override
    public String getPluginId() {
        return "kuaidi100Plugin";
    }

    @Override
    public String getPluginName() {
        return "快递100";
    }

    @Override
    public Integer getIsOpen() {

        return 0;
    }

    @Override
    public DubboResult<ExpressDetailVO> getExpressDetail(String abbreviation, String num, Map config) {
        try {
            String url = "";
            //获取快递平台参数
            Integer user = new Double(StringUtil.toDouble(config.get("user"), false)).intValue();
            String code = StringUtil.toString(config.get("code"));
            String id = StringUtil.toString(config.get("id"));
            HashMap<String, String> parms = new HashMap<String, String>(16);
            //根据不同的用户类型调取不同的查询接口
            if (user.equals(1)) {
                url = "http://poll.kuaidi100.com/poll/query.do";
                String param = "{\"com\":\"" + abbreviation + "\",\"num\":\"" + num + "\"}";
                String sign = MD5.encode(param + id + code);
                parms.put("param", param);
                parms.put("sign", sign);
                parms.put("customer", code);
            } else {
                url = "http://api.kuaidi100.com/api?id=" + id + "&nu=" + num + "&com=" + abbreviation + "&muti=1&order=asc";
            }

            String content = HttpRequest.postData(url, parms, "utf-8").toString();
            Map map = JsonUtil.toMap(content);
            ExpressDetailVO expressDetailVO = new ExpressDetailVO();
            expressDetailVO.setData((List<Map>) map.get("data"));
            expressDetailVO.setCourierNum(map.get("nu").toString());
            QueryWrapper<EsLogiCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(EsLogiCompany::getCode,map.get("com").toString());
            EsLogiCompany esLogiCompany =this.getOne(queryWrapper);
            if (esLogiCompany !=null){
                expressDetailVO.setName(esLogiCompany.getName());
            }
            return DubboResult.success(expressDetailVO);
        } catch (ArgumentException ae) {
            logger.error("查询物流轨迹失败", ae);
            return DubboResult.fail(ae.getExceptionCode(), ae.getMessage());
        } catch (Throwable th) {
            logger.error("查询物流轨迹失败", th);
            return DubboResult.fail(ErrorCode.SYS_ERROR.getErrorCode(), "系统错误");
        }

    }
}
