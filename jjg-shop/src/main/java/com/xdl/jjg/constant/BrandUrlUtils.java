package com.xdl.jjg.constant;

import com.xdl.jjg.model.co.SearchSelector;
import com.xdl.jjg.model.domain.EsBrandDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 品牌url工具
 *
 * @author waf
 * @version v6.4
 * @since v6.4
 */
public class BrandUrlUtils {


    /**
     * 生成品牌的url
     *
     * @param brandid
     * @return
     */
    public static String createBrandUrl(String brandid) {
        Map<String, String> params = ParamsUtils.getReqParams();

        params.put("brand", brandid);

        return ParamsUtils.paramsToUrlString(params);
    }

    /**
     * 生成没有品牌的url
     *
     * @return
     */
    private static String createUrlWithOutBrand() {
        Map<String, String> params = ParamsUtils.getReqParams();

        params.remove("brand");

        return ParamsUtils.paramsToUrlString(params);
    }

    /**
     * 根据id查找brand
     *
     * @param brandList
     * @param brandid
     * @return
     */
    public static EsBrandDO findBrand(List<EsBrandDO> brandList, Long brandid) {

        for (EsBrandDO brand : brandList) {
            if (Long.compare(brand.getId(),brandid) == 0) {
                return brand;
            }
        }
        return null;
    }

    /**
     * 生成已经选择的品牌
     *
     * @param brandList
     * @param brandId
     * @return
     */
    public static List<SearchSelector> createSelectedBrand(List<EsBrandDO> brandList, Long brandId) {
        List<SearchSelector> selectorList = new ArrayList();
        if (brandId == null) {
            return selectorList;
        }
        String brandName = "";
        String brandValue = "";

        EsBrandDO findBrand = findBrand(brandList, brandId);
        if (findBrand != null) {
            brandName = findBrand.getName();
            brandValue = findBrand.getId().toString();
        }

        SearchSelector selector = new SearchSelector();
        selector.setName(brandName);
        selector.setValue(brandValue);
//        selector.setSelected(true);
//        String url = createUrlWithOutBrand();
//        selector.setUrl(url);
        selectorList.add(selector);
        return selectorList;
    }

}
