package com.xdl.jjg.web.service.Impl;


import com.jjg.shop.model.co.*;
import com.jjg.shop.model.constant.*;
import com.jjg.shop.model.domain.*;
import com.jjg.shop.model.dto.EsGoodsIndexDTO;
import com.jjg.shop.model.dto.EsGoodsWordsDTO;
import com.jjg.shop.model.vo.EsSearchGoodsVO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.PinyinUtil;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.util.security.Hex;
import com.xdl.jjg.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EsGoodsIndexServiceImpl   implements IEsGoodsIndexService {

    private Logger logger = LoggerFactory.getLogger(EsGoodsIndexServiceImpl.class);

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private TransportClient client;

    @Autowired
    private IEsGoodsWordsService esGoodsWordsService;
    @Autowired
    private IEsCategoryService esCategoryService;
    @Autowired
    private IEsGoodsService esGoodsService;

    public  static String INDEXNAME = "zfsc";

    public final static String INDEXNAME_DEV = "zfsc_dev";
    public final static String TYPE = "goods";
    @Autowired
    private IEsBrandService esBrandService;
    @Autowired
    private IEsGoodsParamsService esGoodsParamsService;
    private  String active = SpringContextUtil.getActiveProfile();
    @Autowired
    private MQProducer mqProducer;



    /**
     * 商品索引数据添加
     * @param esGoodsIndexDTO
     * @return
     */
    @Override
    public DubboResult<EsGoodsIndexDO> insertEsGoodsIndex(List<EsGoodsIndexDTO> esGoodsIndexDTO) {
        try{
            if(StringUtils.equals("dev",active)){
                saveGoodsIndexDev(esGoodsIndexDTO);
            }else{
                saveGoodsIndex(esGoodsIndexDTO);
            }
            List<String> goodsNameTexts = esGoodsIndexDTO.stream().map(EsGoodsIndexDTO::getGoodsName).collect(Collectors.toList());
            Thread thread = new Thread((Runnable) () -> {
                List<String> wordsList = this.toWordsList(goodsNameTexts);
                wordsList.stream().forEach(words->{
                    EsGoodsWordsDTO esGoodsWordsDTO = new EsGoodsWordsDTO();
                    esGoodsWordsDTO.setWords(words);
                    esGoodsWordsDTO.setQuanpin(PinyinUtil.getPinYin(words));
                    esGoodsWordsDTO.setSzm(PinyinUtil.getPinYinHeadChar(words));
                    this.esGoodsWordsService.insertGoodsWords(esGoodsWordsDTO);
                });
            });
            thread.start();
           return  DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品索引创建失败",ae);
           return DubboResult.fail(ae.getExceptionCode(),ae.getMessage()) ;
        }catch (Throwable th){
            logger.error("商品索引创建失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }

    }
    /**
     * 商品索引数据更新
     * @param esGoodsIndexDTO
     * @return
     */
    @Override
    public DubboResult<EsGoodsIndexDO> updateEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO) {
        try{
            //先删除索引 和分词信息
            this.deleteEsGoodsIndex(esGoodsIndexDTO);
            //创建索引和分词信息
            List<EsGoodsIndexDTO> goodsIndexDTOList = new ArrayList<>();
            goodsIndexDTOList.add(esGoodsIndexDTO);
            this.insertEsGoodsIndex(goodsIndexDTOList);
            return  DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品索引更新失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage()) ;
        }catch (Throwable th){
            logger.error("商品索引更新失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 商品索引数据删除
     * @param esGoodsIndexDTO
     * @return
     */
    @Override
    public DubboResult<EsGoodsIndexDO> deleteEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO) {
        try{
            if(StringUtils.equals("dev",active)){
                INDEXNAME = INDEXNAME_DEV;
            }
            this.elasticsearchOperations.delete(INDEXNAME, TYPE, esGoodsIndexDTO.getId().toString());
            DubboResult<EsGoodsCO> result = esGoodsService.getEsGoods(esGoodsIndexDTO.getId());
            if(result.isSuccess()){
                //分词删除
                List<String> text = new ArrayList<>();
                text.add(result.getData().getGoodsName());
                List<String> words = this.toWordsList(text);
                this.esGoodsWordsService.deleteGoodsWords(words);
            }
            return  DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品索引删除失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage()) ;
        }catch (Throwable th){
            logger.error("商品索引删除失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 商品索引数据删除
     * @return
     */
    @Override
    public DubboResult<EsGoodsIndexDO> deleteEsGoodsIndex() {
        try{
            if(StringUtils.equals("dev",active)){
                INDEXNAME = INDEXNAME_DEV;
            }
            this.elasticsearchOperations.deleteIndex(INDEXNAME);
            this.esGoodsWordsService.deleteGoodsWords();
            return  DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品索引删除失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage()) ;
        }catch (Throwable th){
            logger.error("商品索引删除失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    public DubboResult<EsGoodsIndexDO> deleteEsGoodsIndex(String prod) {
        try{
            this.elasticsearchOperations.deleteIndex(INDEXNAME);
            this.esGoodsWordsService.deleteGoodsWords();
            return  DubboResult.success();
        }catch (ArgumentException ae){
            logger.error("商品索引删除失败",ae);
            return DubboResult.fail(ae.getExceptionCode(),ae.getMessage()) ;
        }catch (Throwable th){
            logger.error("商品索引删除失败",th);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }
    /**
     * 获取分词结果
     *
     * @param text
     * @return 分词list
     */
    private List<String> toWordsList(List<String> text) {
        if(StringUtils.equals("dev",active)){
            INDEXNAME = INDEXNAME_DEV;
        }
        List<String> list = new ArrayList<String>();
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        text.stream().forEach(txt->{
            AnalyzeRequestBuilder request = new AnalyzeRequestBuilder(indicesAdminClient, AnalyzeAction.INSTANCE, INDEXNAME, txt);
            //	分词
            //ik_smart ik_max_word
            request.setAnalyzer("ik_max_word");
            List<AnalyzeResponse.AnalyzeToken> listAnalysis = request.execute().actionGet().getTokens();
            for (AnalyzeResponse.AnalyzeToken token : listAnalysis) {
                list.add(token.getTerm());
            }
        });
        return list;
    }
    /**
     * ES 商品全文检索
     * @param esGoodsIndexDTO

     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public DubboPageResult<EsGoodsIndexDO> getEsGoodsIndex(EsGoodsIndexDTO esGoodsIndexDTO, int pageSize, int pageNum) {
        try {
            //pageNum = pageNum == 0 ? 1 : pageNum;
           // pageSize = pageSize == 0 ? 10 : pageSize;
            SearchRequestBuilder  searchRequestBuilder = this.createQuery(esGoodsIndexDTO);
            //设置分页信息
            searchRequestBuilder.setFrom((pageNum - 1) * pageSize).setSize(pageSize);
            // 设置是否按查询匹配度排序
            searchRequestBuilder.setExplain(true);
            SearchResponse response = searchRequestBuilder.execute().actionGet();

            SearchHits searchHits = response.getHits();
            List<EsGoodsIndexDO> resultlist = new ArrayList<EsGoodsIndexDO>();
            for(SearchHit his:searchHits){
                Map<String, Object> hisMap = his.getSource();
                EsGoodsIndexDO goodsIndexDO = new EsGoodsIndexDO();
                goodsIndexDO.setName(hisMap.get("goodsName").toString());
                goodsIndexDO.setOriginal(hisMap.get("original")==null ? "" :hisMap.get("original").toString() );
                goodsIndexDO.setMoney(StringUtil.toDouble(hisMap.get("money") ==null ? "0" :hisMap.get("money").toString() ));
                goodsIndexDO.setGoodsId(Long.parseLong(hisMap.get("id") ==null ? "0" :hisMap.get("id").toString()));
                goodsIndexDO.setCommentNum(Integer.parseInt(hisMap.get("commentNum")==null ? "0" :hisMap.get("commentNum").toString() ));
                goodsIndexDO.setBuyCount(Integer.parseInt(hisMap.get("buyCount")==null ? "0" :hisMap.get("buyCount").toString() ));
                goodsIndexDO.setGrade(StringUtil.toDouble(hisMap.get("grade") ==null ? "0" :hisMap.get("grade").toString() ));
                goodsIndexDO.setShopId(Integer.parseInt(hisMap.get("shopId") ==null ? "0" :hisMap.get("shopId").toString() ));
                goodsIndexDO.setShopName(hisMap.get("shopName") ==null ? "" : (hisMap.get("shopName").toString()));
                goodsIndexDO.setBrandId(Long.parseLong(hisMap.get("brandId")==null ? "0" :hisMap.get("brandId").toString()));
                resultlist.add(goodsIndexDO);
                /*
                 DubboResult<EsGoodsCO> goodsResult = esGoodsService.getEsGoods(goodsIndexDO.getGoodsId());
                if(goodsResult.isSuccess() && goodsResult.getData()!= null){
                    //已审核 已上架 未删除
                    if(goodsResult.getData().getMarketEnable() == 0 && goodsResult.getData().getIsAuth() == 1
                            && goodsResult.getData().getIsDel() == 0 ){
                        resultlist.add(goodsIndexDO);
                    }
                }*/
            }
            return DubboPageResult.success(searchHits.getTotalHits(),resultlist);
        } catch (ArgumentException ae) {
            logger.error("商品索引查询失败",ae);
            return DubboPageResult.fail(ae.getExceptionCode(),ae.getMessage());
        }catch (Throwable th){
            logger.error("商品索引查询失败",th);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }
    }

    /**
     * 批量插入索引数据
     * @param esGoodsIndexDTO
     * @return
     */
    @Override
    public DubboResult batchInsertEsGoodsIndex(List<EsGoodsIndexDTO> esGoodsIndexDTO) {
        if(StringUtils.equals("dev",active)){
            this.saveGoodsIndexDev(esGoodsIndexDTO);
        }else{
            this.saveGoodsIndex(esGoodsIndexDTO);
        }


        return DubboResult.success();
    }

    private SearchRequestBuilder createQuery(EsGoodsIndexDTO goodsIndexDTO) throws Exception {
        if(StringUtils.equals("dev",active)){
            INDEXNAME = INDEXNAME_DEV;
        }
        //查询构造器
        SearchRequestBuilder searchRequestBuilder =  client.prepareSearch(INDEXNAME);
        //布尔查询构造
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 关键字检索
        if (!StringUtil.isEmpty(goodsIndexDTO.getKeyword())) {
            QueryStringQueryBuilder queryString = new QueryStringQueryBuilder(goodsIndexDTO.getKeyword()).field("goodsName");
            queryString.defaultOperator(QueryStringQueryBuilder.Operator.AND);
            boolQueryBuilder.must(queryString);
        }
        //品牌
        if (CollectionUtils.isNotEmpty(goodsIndexDTO.getBrandList())) {
            List<String> brandList = goodsIndexDTO.getBrandList();
            boolQueryBuilder.must(QueryBuilders.termsQuery("brandIdStr", brandList));
        }
        //分类
        if (CollectionUtils.isNotEmpty(goodsIndexDTO.getCategoryIdList())) {
            List<String> categoryIdList = goodsIndexDTO.getCategoryIdList();
            boolQueryBuilder.must(QueryBuilders.termsQuery("categoryIdStr", categoryIdList));
        }//分类
        if (CollectionUtils.isNotEmpty(goodsIndexDTO.getGoodsList())) {
            List<String> goodsList = goodsIndexDTO.getGoodsList();
            boolQueryBuilder.must(QueryBuilders.termsQuery("goodsIdStr", goodsList));
        }

        if (goodsIndexDTO.getCategoryId() != null) {
           DubboPageResult<EsCategoryDO> result= esCategoryService.getCategoryList(goodsIndexDTO.getCategoryId());
           if(result.isSuccess()){
              List<Long> categoryIdList =  result.getData().getList().stream().map(EsCategoryDO::getId).collect(Collectors.toList());
               boolQueryBuilder.must(QueryBuilders.termsQuery("categoryIdStr", categoryIdList));
           }

        }
  /*      //分类
        if(goodsIndexDTO.getCategoryId() !=null){
           DubboResult<EsCategoryDO> cateResult = esCategoryService.getCategory(goodsIndexDTO.getCategoryId());
           if(!cateResult.isSuccess() || cateResult.getData() == null){
               throw new ArgumentException(GoodsErrorCode.DATA_NOT_EXIST.getErrorCode(),"商品分类不存在");
           }
               boolQueryBuilder.must(QueryBuilders.wildcardQuery("categoryPath", encode(goodsIndexDTO.getCategoryPath()) + "*"));
               //分类名称
               QueryStringQueryBuilder queryString = new QueryStringQueryBuilder(goodsIndexDTO.getKeyword()).field("categoryName");
               queryString.defaultOperator(Operator.AND);
               boolQueryBuilder.must(queryString);
        }*/
        //卖家ID
        if(goodsIndexDTO.getShopId() !=null){
            boolQueryBuilder.must(QueryBuilders.termQuery("shopId", goodsIndexDTO.getShopId()));
        }
        // 参数检索
        List<String> propList = goodsIndexDTO.getPropList();
        if (CollectionUtils.isNotEmpty(propList)){
            propList.forEach(s -> {
                String[] onpropAr = s.split(Separator.SEPARATOR_PROP_VLAUE);
                String name = onpropAr[0];
                String value = onpropAr[1];
                String[] values = value.split(Separator.SEPARATOR_PROP_FENHAO);
                for (String p : values) {
                    boolQueryBuilder.must(QueryBuilders.nestedQuery("params", QueryBuilders.termQuery("params.paramName", name)));
                    boolQueryBuilder.must(QueryBuilders.nestedQuery("params", QueryBuilders.termQuery("params.paramValue", p)));
                }
            });
        }


//        String prop = goodsIndexDTO.getProp();
//        if (!StringUtil.isEmpty(prop)) {
//            String[] propArray = prop.split(Separator.SEPARATOR_PROP_COMMA);
//            for (String p : propArray) {
//                String[] onpropAr = p.split(Separator.SEPARATOR_PROP_VLAUE);
//                String name = onpropAr[0];
//                String value = onpropAr[1];
//                boolQueryBuilder.must(QueryBuilders.nestedQuery("params", QueryBuilders.termQuery("params.paramName", name), ScoreMode.None));
//                boolQueryBuilder.must(QueryBuilders.nestedQuery("params", QueryBuilders.termQuery("params.paramValue", value), ScoreMode.None));
//            }
//        }
        //价格搜索
        if (goodsIndexDTO.getPrice() != null) {
            String money = goodsIndexDTO.getPrice();
            String[] pricear = money.split(Separator.SEPARATOR_PROP_VLAUE);
            Double min = StringUtil.toDouble(pricear[0], 0.0);
            Double max = Double.MAX_VALUE;
            if (pricear.length == 2) {
                max = StringUtil.toDouble(pricear[1], Double.MAX_VALUE);
            }
            boolQueryBuilder.must(QueryBuilders.rangeQuery("money").from(min).to(max).includeLower(true).includeUpper(true));
        }
        // 删除的商品不显示
        boolQueryBuilder.must(QueryBuilders.termQuery("isDel", "0"));
        // 未上架的商品不显示
        boolQueryBuilder.must(QueryBuilders.termQuery("marketEnable", "1"));
        // 待审核和审核不通过的商品不显示
        boolQueryBuilder.must(QueryBuilders.termQuery("isAuth", "1"));
        //不展示赠品商品
        boolQueryBuilder.must(QueryBuilders.termQuery("isGifts", "2"));
        searchRequestBuilder.setQuery(boolQueryBuilder);

        //排序
        if (goodsIndexDTO.getSort() != null) {
            Map<String, String> sortMap = SortContainer.getSort(goodsIndexDTO.getSort());
            String sortid = sortMap.get("id");
            // 如果是默认排序
            if ("def".equals(sortid)) {
                sortid = "id";
            }
            if ("buynum".equals(sortid)) {
                sortid = "buyCount";
            }
            if ("price".equals(sortid)) {
                sortid = "money";
            }
            if ("grade".equals(sortid)) {
                sortid = "grade";
            }
            SortOrder sort;
            if ("desc".equals(sortMap.get("def_sort"))) {
                sort = SortOrder.DESC;
            } else {
                sort = SortOrder.ASC;
            }
            searchRequestBuilder.addSort(sortid, sort);
        }
        System.out.println(searchRequestBuilder.toString());
        return searchRequestBuilder;
    }

    /**
     * 将字符串进行hex
     * @param str
     * @return
     */
    public static String encode(String str){
        try{
            return new String(Hex.encodeHex(str.getBytes("UTF-8")));
        }catch(Exception ex){
            return str;
        }
    }

    public void saveGoodsIndex(List<EsGoodsIndexDTO> esGoodsIndexDTO){
        //创建索引
        if (!elasticsearchOperations.indexExists(INDEXNAME)) {
            elasticsearchOperations.createIndex(INDEXNAME);
            //指定Mapping
            elasticsearchOperations.putMapping(EsGoodsIndexCO.class);
        }
        List<IndexQuery> indexQueryList =  esGoodsIndexDTO.stream().map(goodsIndexDTO->{
            EsGoodsIndexCO esGoodsIndexCO = new EsGoodsIndexCO();
            BeanUtil.copyProperties(goodsIndexDTO,esGoodsIndexCO);
            esGoodsIndexCO.setBrandIdStr(goodsIndexDTO.getBrandId()+"");
            esGoodsIndexCO.setCategoryIdStr(goodsIndexDTO.getCategoryId()+"");
            esGoodsIndexCO.setGoodsIdStr(goodsIndexDTO.getId()+"");
            DubboResult<EsCategoryDO> result = esCategoryService.getCategory(goodsIndexDTO.getCategoryId());
            if(result.isSuccess()){
                esGoodsIndexCO.setCategoryName(result.getData().getName());
            }
            esGoodsIndexCO.setCategoryPath(encode(result.getData().getCategoryPath()));
            DubboPageResult<EsGoodsParamsDO> paramsResult= esGoodsParamsService.getGoodsParamsByGoodsId(goodsIndexDTO.getId());
            if(paramsResult.isSuccess()){
                List<EsParams> esParams= BeanUtil.copyList(paramsResult.getData().getList(),EsParams.class);
                esGoodsIndexCO.setParams(esParams);
            }
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setIndexName(INDEXNAME);
            indexQuery.setType(TYPE);
            indexQuery.setId(esGoodsIndexCO.getId().toString());
            indexQuery.setObject(esGoodsIndexCO);
            return indexQuery;
        }).collect(Collectors.toList());
        this.elasticsearchOperations.bulkIndex(indexQueryList);

    }
    public void saveGoodsIndexDev(List<EsGoodsIndexDTO> esGoodsIndexDTO){
        //创建索引
        if (!elasticsearchOperations.indexExists(INDEXNAME_DEV)) {
            elasticsearchOperations.createIndex(INDEXNAME_DEV);
            //指定Mapping
            elasticsearchOperations.putMapping(EsGoodsIndexDevCO.class);
        }
        List<IndexQuery> indexQueryList =  esGoodsIndexDTO.stream().map(goodsIndexDTO->{
            EsGoodsIndexDevCO esGoodsIndexCO = new EsGoodsIndexDevCO();
            BeanUtil.copyProperties(goodsIndexDTO,esGoodsIndexCO);
            esGoodsIndexCO.setBrandIdStr(goodsIndexDTO.getBrandId()+"");
            esGoodsIndexCO.setCategoryIdStr(goodsIndexDTO.getCategoryId()+"");
            esGoodsIndexCO.setGoodsIdStr(goodsIndexDTO.getId()+"");
            DubboResult<EsCategoryDO> result = esCategoryService.getCategory(goodsIndexDTO.getCategoryId());
            if(result.isSuccess()){
                esGoodsIndexCO.setCategoryName(result.getData().getName());
            }
            esGoodsIndexCO.setCategoryPath(encode(result.getData().getCategoryPath()));
            DubboPageResult<EsGoodsParamsDO> paramsResult= esGoodsParamsService.getGoodsParamsByGoodsId(goodsIndexDTO.getId());
            if(paramsResult.isSuccess()){
                List<EsParams> esParams= BeanUtil.copyList(paramsResult.getData().getList(),EsParams.class);
                esGoodsIndexCO.setParams(esParams);
            }
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setIndexName(INDEXNAME_DEV);
            indexQuery.setType(TYPE);
            indexQuery.setId(esGoodsIndexCO.getId().toString());
            indexQuery.setObject(esGoodsIndexCO);
            return indexQuery;
        }).collect(Collectors.toList());
        this.elasticsearchOperations.bulkIndex(indexQueryList);

    }
    public DubboResult<EsSelectSearchCO> getSelector(EsGoodsIndexDTO goodsSearch) {
        SearchRequestBuilder searchRequestBuilder;
        try {
            searchRequestBuilder = this.createQuery(goodsSearch);
            //分类
            AggregationBuilder categoryTermsBuilder = AggregationBuilders.terms("categoryAgg").field("categoryId").size(Integer.MAX_VALUE);
            //品牌
            AggregationBuilder brandTermsBuilder = AggregationBuilders.terms("brandAgg").field("brandId").size(Integer.MAX_VALUE);
            //参数
            AggregationBuilder valuesBuilder = AggregationBuilders.terms("valueAgg").field("params.paramValue").size(Integer.MAX_VALUE);
            AggregationBuilder paramsNameBuilder = AggregationBuilders.terms("nameAgg").field("params.paramName").subAggregation(valuesBuilder).size(Integer.MAX_VALUE);
            AggregationBuilder avgBuild = AggregationBuilders.nested("paramsAgg").subAggregation(paramsNameBuilder);

            searchRequestBuilder.addAggregation(categoryTermsBuilder);
            searchRequestBuilder.addAggregation(brandTermsBuilder);
            searchRequestBuilder.addAggregation(avgBuild);

            SearchResponse sr = searchRequestBuilder.execute().actionGet();
            Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
            EsSelectSearchCO selectSearchCO = new EsSelectSearchCO();
            //分类
            LongTerms categoryTerms = (LongTerms) aggMap.get("categoryAgg");
            List<Terms.Bucket> categoryBuckets = categoryTerms.getBuckets();

            DubboPageResult<EsCategoryDO> allCatList = this.esCategoryService.getCategoryChildren(0L);

            List<SearchSelector> catDim = SelectorUtil.createCatSelector(categoryBuckets, allCatList.getData().getList(), goodsSearch.getCategoryId());
            selectSearchCO.setCat(catDim);
            String catPath = null;
            if(goodsSearch.getCategoryId()!=null){
                DubboResult<EsCategoryDO> cat = esCategoryService.getCategory(goodsSearch.getCategoryId());
                String path = cat.getData().getCategoryPath();
                catPath = path.replace("|",Separator.SEPARATOR_PROP_VLAUE).substring(0,path.length()-1);
            }

            List<SearchSelector> selectedCat = CatUrlUtils.getCatDimSelected(categoryBuckets, allCatList.getData().getList(),catPath );
            //已经选择的分类
           selectSearchCO.setSelectedCat(selectedCat);
            //品牌
            LongTerms brandTerms = (LongTerms) aggMap.get("brandAgg");
            List<Terms.Bucket> brandBuckets = brandTerms.getBuckets();
            DubboPageResult<EsBrandDO> brandList = esBrandService.getAllBrandList();
            List<SearchSelector> brandDim = SelectorUtil.createBrandSelector(brandBuckets, brandList.getData().getList());
            selectSearchCO.setBrand(brandDim);
            //参数
            InternalNested paramsAgg = (InternalNested) aggMap.get("paramsAgg");
            InternalAggregations paramTerms = paramsAgg.getAggregations();
            Map<String, Aggregation> nameMap = paramTerms.asMap();
            StringTerms nameTerms = (StringTerms) nameMap.get("nameAgg");
            if(nameTerms !=null){
                Iterator<Terms.Bucket> paramBucketIt = nameTerms.getBuckets().iterator();
                List<PropSelector> paramDim = SelectorUtil.createParamSelector(paramBucketIt);
                selectSearchCO.setProp(paramDim);
            }else{
                selectSearchCO.setProp(new ArrayList<>());
            }
            List<Long> cateIds  = categoryBuckets.stream().map(s-> Long.parseLong(s.getKeyAsString())).distinct().collect(Collectors.toList());
            DubboPageResult<EsGoodsDO> result= esGoodsService.getGuessLook(cateIds.stream().toArray(Long[]::new) ,goodsSearch.getKeyword());
            if(result.isSuccess()){
                selectSearchCO.setLikeGoods(BeanUtil.copyList(result.getData().getList(), EsSearchGoodsVO.class));
            }
            return DubboResult.success(selectSearchCO);
        } catch (Exception e) {
            logger.error("选择器查询失败",e);
            return DubboResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }

    }



    public DubboPageResult<EsPcSelectSearchCO> getPcSelector(EsGoodsIndexDTO goodsSearch) {
        SearchRequestBuilder searchRequestBuilder;
        try {

            List<Object> paramsList = new ArrayList<>();
            searchRequestBuilder = this.createQuery(goodsSearch);
            //分类
            AggregationBuilder categoryTermsBuilder = AggregationBuilders.terms("categoryAgg").field("categoryId").size(Integer.MAX_VALUE);
            //品牌
            AggregationBuilder brandTermsBuilder = AggregationBuilders.terms("brandAgg").field("brandId").size(Integer.MAX_VALUE);
            //参数
            AggregationBuilder valuesBuilder = AggregationBuilders.terms("valueAgg").field("params.paramValue").size(Integer.MAX_VALUE);
            AggregationBuilder paramsNameBuilder = AggregationBuilders.terms("nameAgg").field("params.paramName").subAggregation(valuesBuilder).size(Integer.MAX_VALUE);
            AggregationBuilder avgBuild = AggregationBuilders.nested("paramsAgg").subAggregation(paramsNameBuilder);

            searchRequestBuilder.addAggregation(categoryTermsBuilder);
            searchRequestBuilder.addAggregation(brandTermsBuilder);
            searchRequestBuilder.addAggregation(avgBuild);

            SearchResponse sr = searchRequestBuilder.execute().actionGet();
            Map<String, Aggregation> aggMap = sr.getAggregations().asMap();
            EsPcSelectSearchCO selectSearchCat = new EsPcSelectSearchCO();
            //分类
            LongTerms categoryTerms = (LongTerms) aggMap.get("categoryAgg");
            List<Terms.Bucket> categoryBuckets = categoryTerms.getBuckets();

            DubboPageResult<EsCategoryDO> allCatList = this.esCategoryService.getCategoryChildren(0L);
            List<SearchSelector> catDim = SelectorUtil.createCatSelector(categoryBuckets, allCatList.getData().getList(), goodsSearch.getCategoryId());

            // --------------------------
            selectSearchCat.setParamName("cat");
            selectSearchCat.setParamNameText("分类");
            selectSearchCat.setParamsList(Collections.singletonList(catDim));
            // --------------------------
            String catPath = null;
            Boolean flag = true;
            // 已选择的分类
            List<SearchSelector> selectedAll = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(goodsSearch.getCategoryIdList())){
                List<String> categoryIdList = goodsSearch.getCategoryIdList();
                for (String s : categoryIdList) {
                    DubboResult<EsCategoryDO> cat = esCategoryService.getCategory(Long.parseLong(s));
                    String path = cat.getData().getCategoryPath();
                    catPath = path.replace("|", Separator.SEPARATOR_PROP_VLAUE).substring(0, path.length() - 1);
                    List<SearchSelector> selectedCat = CatUrlUtils.getCatDimSelected(categoryBuckets, allCatList.getData().getList(),catPath );
                    selectedAll.addAll(selectedCat);
                    if (CollectionUtils.isEmpty(goodsSearch.getBrandList())){
                        // 通过分类 查询分类下关联的品牌
                        List<String> categoryIdList1 = goodsSearch.getCategoryIdList();
                        DubboPageResult<EsBrandDO> brands = esBrandService.getBrandsByCategory(Long.valueOf(categoryIdList1.get(0)));
                        List<SearchSelector> selectorList = new ArrayList<>();
                        List<EsBrandDO> list1 = brands.getData().getList();
                        for (EsBrandDO esBrandDO : list1) {
                            SearchSelector selector = new SearchSelector();
                            selector.setName(esBrandDO.getName());
                            selector.setUrl(esBrandDO.getLogo());
                            selector.setValue(esBrandDO.getId()+"");
                            selectorList.add(selector);
                        }
                        EsPcSelectSearchCO selectSearchBrand = new EsPcSelectSearchCO();
                        selectSearchBrand.setParamName("brand");
                        selectSearchBrand.setParamNameText("品牌");
                        selectSearchBrand.setParamsList(Collections.singletonList(selectorList));
                        // 放入返回参数中
                        paramsList.add(selectSearchBrand);
                    }

                }
                flag = false;
            }
            EsPcSelectSearchCO searchSelected = new EsPcSelectSearchCO();
            searchSelected.setParamName("selected");
            searchSelected.setParamsList(Collections.singletonList(selectedAll));

            LongTerms brandTerms = (LongTerms) aggMap.get("brandAgg");
            List<Terms.Bucket> brandBuckets = brandTerms.getBuckets();
            // 已选择的品牌
            List<String> bandList = goodsSearch.getBrandList();
            if(CollectionUtils.isNotEmpty(bandList)){

                List<Long> collect = bandList.stream().map(s -> Long.parseLong(s)).collect(Collectors.toList());
                DubboPageResult<EsBrandDO> brandByIds = this.esBrandService.getBrandByIds(collect);
                List<EsBrandDO> list = brandByIds.getData().getList();
                List<String> brandName = list.stream().map(esBrandDO -> esBrandDO.getName()).collect(Collectors.toList());
                String stripName = StringUtils.strip(brandName.toString(), "[]");
                List<Long> brandValue = list.stream().map(esBrandDO -> esBrandDO.getId()).collect(Collectors.toList());
                String stripValue = StringUtils.strip(brandValue.toString(), "[]");
                SearchSelector searchSelector = new SearchSelector();
                searchSelector.setParamKey("品牌");
                searchSelector.setParamValue("brand");
                searchSelector.setSelected(true);
                searchSelector.setName(stripName);
                searchSelector.setValue(stripValue);
                selectedAll.add(searchSelector);

            }else if (flag){
                // 已选择的分类 展示分类下的品牌
                //品牌
                DubboPageResult<EsBrandDO> brandList = esBrandService.getAllBrandList();
                List<SearchSelector> brandDim = SelectorUtil.createBrandSelector(brandBuckets, brandList.getData().getList());
                EsPcSelectSearchCO selectSearchBrand = new EsPcSelectSearchCO();
                selectSearchBrand.setParamName("brand");
                selectSearchBrand.setParamNameText("品牌");
                selectSearchBrand.setParamsList(Collections.singletonList(brandDim));
                // 放入返回参数中
                paramsList.add(selectSearchBrand);

            }
            List<String> propList = goodsSearch.getPropList();
            // 已选择的参数
            if(CollectionUtils.isNotEmpty(propList)){

                propList.forEach(s -> {
                    String[] onpropAr = s.split(Separator.SEPARATOR_PROP_VLAUE);
                    String name = onpropAr[0];
                    String value = onpropAr[1];
                    SearchSelector searchSelector = new SearchSelector();
                    searchSelector.setParamKey("prop");
                    searchSelector.setParamValue("prop");
                    searchSelector.setName(name);
                    searchSelector.setValue(value);
                    selectedAll.add(searchSelector);
                });
                searchSelected.setParamName("selected");
                searchSelected.setParamsList(Collections.singletonList(selectedAll));
            }
                //参数
                InternalNested paramsAgg = (InternalNested) aggMap.get("paramsAgg");
                InternalAggregations paramTerms = paramsAgg.getAggregations();
                Map<String, Aggregation> nameMap = paramTerms.asMap();
                StringTerms nameTerms = (StringTerms) nameMap.get("nameAgg");
                EsPcSelectSearchCO selectSearchProp = new EsPcSelectSearchCO();
                if(nameTerms !=null){
                    Iterator<Terms.Bucket> paramBucketIt = nameTerms.getBuckets().iterator();
                    List<PropSelector> paramDim = SelectorUtil.createParamSelector(paramBucketIt);
                    List<String> propkey = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(propList)){
                        propList.forEach(s -> {
                            String[] onpropAr = s.split(Separator.SEPARATOR_PROP_VLAUE);
                            String name = onpropAr[0];
                            propkey.add(name);
                        });
                        List<PropSelector> collect = paramDim.stream().filter(propSelector -> !propkey.contains(propSelector.getKey())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(collect)){
                            selectSearchProp.setParamName("prop");
                            selectSearchProp.setParamsList(Collections.singletonList(collect));
                            paramsList.add(selectSearchProp);
                        }
                    }else{
                        selectSearchProp.setParamName("prop");
                        selectSearchProp.setParamsList(Collections.singletonList(paramDim));
                        paramsList.add(selectSearchProp);
                    }
                }
            List<Long> cateIds  = categoryBuckets.stream().map(s-> Long.parseLong(s.getKeyAsString())).distinct().collect(Collectors.toList());
            DubboPageResult<EsGoodsDO> result= esGoodsService.getGuessLook(cateIds.stream().toArray(Long[]::new) ,goodsSearch.getKeyword());
            EsPcSelectSearchCO selectSearchLike = new EsPcSelectSearchCO();
            if(result.isSuccess()){
                selectSearchLike.setParamName("like");
                selectSearchLike.setParamsList(Collections.singletonList(result.getData().getList()));
            }
            paramsList.add(searchSelected);
            paramsList.add(selectSearchCat);
            paramsList.add(selectSearchLike);
            return DubboPageResult.success(paramsList);
        } catch (Exception e) {
            logger.error("选择器查询失败",e);
            return DubboPageResult.fail(GoodsErrorCode.SYS_ERROR.getErrorCode(), GoodsErrorCode.SYS_ERROR.getErrorMsg());
        }

    }
}
