package com.jjg.shop.model.constant;


import com.jjg.shop.model.co.SearchSelector;
import com.jjg.shop.model.domain.EsBrandDO;
import com.jjg.shop.model.domain.EsCategoryDO;
import com.xdl.jjg.util.StringUtil;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import java.util.*;

/**
 * 选择器工具类
 *
 * @author waf
 * @version v1.0
 * 2017年4月25日 下午4:59:31
 */
public class SelectorUtil {

    /**
     * 创建分类选择器
     *
     * @param categoryBuckets
     * @param allCatList
     * @return
     */
    public static List<SearchSelector> createCatSelector(List<Bucket> categoryBuckets, List<EsCategoryDO> allCatList, Long catId) {
        String catPid = null;
        if (catId != null) {
//            String[] catar = catId.split(Separator.SEPARATOR_PROP_VLAUE);
            catPid = catId + "";
        }
        List<SearchSelector> selectorList = new ArrayList<>();
        Map<String, String> map = new HashMap<>(16);
        if (categoryBuckets != null && categoryBuckets.size() > 0) {
            for (Bucket bucket : categoryBuckets) {
                String categoryId = bucket.getKey().toString();
                String catname = "";
                EsCategoryDO findcat = CatUrlUtils.findCat(allCatList, StringUtil.toInt(categoryId, 0));
                //没有传递cat参数
                if (catPid == null) {
                    String[] path = findcat.getCategoryPath().replace('|', Separator.SEPARATOR_PROP_VLAUE.charAt(0)).split(Separator.SEPARATOR_PROP_VLAUE);
                    if (map.get(path[1]) != null) {
                        continue;
                    }
                    EsCategoryDO parentCat = CatUrlUtils.findCat(allCatList, StringUtil.toInt(path[1], 0));
                    map.put(path[1], path[1]);
                    catname = parentCat.getName();
                    findcat = parentCat;
                } else {//有参数
                    int index = findcat.getCategoryPath().indexOf(catPid);
                    if (index == -1) {
                        continue;
                    }
                    String[] path = findcat.getCategoryPath().substring(index + catPid.length() + 1).replace('|', Separator.SEPARATOR_PROP_VLAUE.charAt(0))
                            .split(Separator.SEPARATOR_PROP_VLAUE);
                    if (map.get(path[0]) != null || "".equals(path[0])) {
                        continue;
                    }
                    EsCategoryDO cat = CatUrlUtils.findCat(allCatList, StringUtil.toInt(path[0], 0));
                    map.put(path[0], path[0]);
                    catname = cat.getName();
                    findcat = cat;
                }

                if (StringUtil.isEmpty(catname)) {
                    continue;
                }
                SearchSelector selector = new SearchSelector();
                selector.setName(catname);
//                String url = CatUrlUtils.createCatUrl(findcat, false);
                selector.setValue(findcat.getId()+"");
                selectorList.add(selector);
            }
        }

        return selectorList;
    }

    /**
     * 创建品牌选择器
     *
     * @param brandBuckets
     * @param brandList
     * @return
     */
    public static List<SearchSelector> createBrandSelector(List<Bucket> brandBuckets, List<EsBrandDO> brandList) {
        List<SearchSelector> selectorList = new ArrayList<>();

        if (brandBuckets != null && brandBuckets.size() > 0) {
            for (Bucket bucket : brandBuckets) {
                long brandid = StringUtil.toInt(bucket.getKey().toString(), 0);
                String brandname = "";
                EsBrandDO findbrand = BrandUrlUtils.findBrand(brandList, brandid);
                if (findbrand != null) {
                    brandname = findbrand.getName();
                }
                if (StringUtil.isEmpty(brandname)) {
                    continue;
                }
                SearchSelector selector = new SearchSelector();
                selector.setName(brandname);
                selector.setUrl(findbrand.getLogo());
                selector.setValue(brandid+"");
                selectorList.add(selector);
            }
        }
        return selectorList;

    }

    /**
     * 检测某个维度是否已经选择
     *
     * @param dim
     * @param props
     * @return 如果没有选择返回null，如果选择了返回属性值
     */
    private static String checkSelected(String dim, String[] props) {
        for (int i = 0; i < props.length; i++) {
            String p = props[i];
            String[] onepropAr = p.split(Separator.SEPARATOR_PROP_VLAUE);
            if (onepropAr[0].equals(dim)) {
                return onepropAr[1];
            }
        }
        return null;
    }

    /**
     * 创建参数选择器
     *
     * @param paramBucketIt
     * @return
     */
    public static List<PropSelector> createParamSelector(Iterator<Bucket> paramBucketIt) {

        List<PropSelector>  propSelectorList = new ArrayList<>();
        while (paramBucketIt.hasNext()) {
            PropSelector propSelector = new PropSelector();
            Bucket paramBucket = paramBucketIt.next();
            String param = paramBucket.getKey().toString();
            StringTerms valueTerms = (StringTerms)paramBucket.getAggregations().asMap().get("valueAgg");
            Iterator<Bucket> valueBucketIt = valueTerms.getBuckets().iterator();
            List<SearchSelector> selectList = new ArrayList<>();
            while (valueBucketIt.hasNext()) {

                Bucket valueBucket = valueBucketIt.next();
                String value = valueBucket.getKey().toString();

                if(!StringUtil.isEmpty(value)){
                    SearchSelector selector = new SearchSelector();
                    selector.setName(value);
                    selector.setValue(value);

                    selectList.add(selector);
                }
            }
            propSelector.setKey(param);
            propSelector.setValue(selectList);
            propSelectorList.add(propSelector);
        }

        return propSelectorList;
    }

    /**
     * 排序方式
     *
     * @return
     */
    public static List<SearchSelector> createSortSelector() {

        return null;
    }


}
