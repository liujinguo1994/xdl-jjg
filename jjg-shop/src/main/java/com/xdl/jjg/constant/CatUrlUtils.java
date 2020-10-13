package com.xdl.jjg.constant;


import com.xdl.jjg.model.co.SearchSelector;
import com.xdl.jjg.model.domain.EsCategoryDO;
import com.xdl.jjg.util.StringUtil;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类url生成工具
 *
 * @author waf
 * @version v1.0
 * @since v7.0
 */
public class CatUrlUtils {

    /**
     * 生成加入某个分类的url
     *
     * @param goodsCat
     * @param onlyCat  只拼接分类 true/false
     * @return
     */
    public static String createCatUrl(EsCategoryDO goodsCat, boolean onlyCat) {
        Map<String, String> params = null;

        if (onlyCat) {
            params = new HashMap<String, String>(16);
        } else {
            params = ParamsUtils.getReqParams();
        }

        String catpath = goodsCat.getCategoryPath();
        catpath = catpath.substring(2, catpath.length());
        if (catpath.endsWith("|")) {
            catpath = catpath.substring(0, catpath.length() - 1);
        }
        catpath = catpath.replace('|', Separator.SEPARATOR_PROP_VLAUE.charAt(0));

        params.put("category", catpath);

        return catpath;
    }

    /**
     * 根据树型结构的分类取出某个分类的名称
     *
     * @param allCatList
     * @param catid
     * @return
     */
    public static EsCategoryDO findCat(List<EsCategoryDO> allCatList, int catid) {
        for (EsCategoryDO cat : allCatList) {
            if (cat.getId().intValue() == catid) {
                return cat;
            }

            if (StringUtil.isNotEmpty(cat.getChildren())) {
                EsCategoryDO findCat = findCat(cat.getChildren(), catid);
                if (findCat != null) {
                    return findCat;
                }

            }

        }
        return null;
    }

    /**
     * 获取已经选择的分类维度
     *
     * @return
     */
    public static List<SearchSelector> getCatDimSelected(List<Terms.Bucket> categoryBuckets, List<EsCategoryDO> allCatList, String cat) {
        List<SearchSelector> selectorList = new ArrayList();
        if (!StringUtil.isEmpty(cat)) {
            String[] catAr = cat.split(Separator.SEPARATOR_PROP_VLAUE);
            String catStr = "";
            for (String catId : catAr) {
                String catName = "";
                EsCategoryDO findCat = findCat(allCatList, StringUtil.toInt(catId, 0));
                if (findCat != null) {
                    catName = findCat.getName();
                }

                if (StringUtil.isEmpty(catName)) {
                    continue;
                }

                if (!StringUtil.isEmpty(catStr)) {
                    catStr = catStr + Separator.SEPARATOR_PROP_VLAUE;
                }
                catStr = catStr + catId;


                SearchSelector selector = new SearchSelector();
                selector.setParamKey("分类");
                selector.setParamValue("cat");
                selector.setName(catName);
                selector.setValue(findCat.getId()+"");
                selector.setSelected(true);
                selector.setOtherOptions(createBrothersCat(categoryBuckets,allCatList, findCat));
                selectorList.add(selector);

            }
        }
        return selectorList;

    }

    /**
     * 生成此分类的同级别的selector
     *
     * @param allCatList
     * @param cat
     * @return
     */
    private static List<SearchSelector> createBrothersCat(List<Terms.Bucket> categoryBuckets,List<EsCategoryDO> allCatList, EsCategoryDO cat) {
        List<SearchSelector> selectorList = new ArrayList();

        int parentId = cat.getId().intValue();
        List<EsCategoryDO> children = new ArrayList();
        if (parentId == 0) {
            children = allCatList;
        } else {
            EsCategoryDO parentCat = findCat(allCatList, parentId);
            if (parentCat == null) {
                return selectorList;
            }

            if (StringUtil.isNotEmpty(parentCat.getChildren())) {
                children = parentCat.getChildren();
            }
        }
        for (EsCategoryDO child : children) {
//            if(map.get(child.getCategoryId())==null){
//                continue;
//            }
            SearchSelector selector = new SearchSelector();
            selector.setName(child.getName());
            selector.setValue(child.getId()+"");
//            selector.setSelected(true);
            selectorList.add(selector);
        }

        return selectorList;
    }

}
