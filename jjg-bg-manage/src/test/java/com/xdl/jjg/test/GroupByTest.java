package com.xdl.jjg.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xdl.jjg.data.bo.Sku;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/1 9:35
 */
public class GroupByTest {

    @Test
    public void test1(){
        List<Sku> skus = new ArrayList<>();
        skus.add(new Sku(52l,"颜色",53l,"红色"));
        skus.add(new Sku(52l,"颜色",54l,"蓝色"));
        skus.add(new Sku(52l,"颜色",55l,"黑色"));
        skus.add(new Sku(56l,"尺码",57l,"S"));
        skus.add(new Sku(56l,"尺码",58l,"M"));
        skus.add(new Sku(56l,"尺码",59l,"L"));

        Map<Map<Long, String>, List<Sku>> map = skus.stream()
                .collect(Collectors.groupingBy(new Function<Sku, Map<Long, String>>() {
                    @Override
                    public Map<Long, String> apply(Sku sku) {
                        Map<Long, String> m = new HashMap<>();
                        m.put(sku.getKid(), sku.getK());
                        return m;
                    }
                }));
//        Map<TwoTuple<Long, String>, List<Sku>> map = skus.stream()
//                .collect(Collectors.groupingBy(new Function<Sku, TwoTuple<Long, String>>() {
//                    @Override
//                    public TwoTuple<Long, String> apply(Sku sku) {
//                        return new TwoTuple<>(sku.getKid(), sku.getK());
//                    }
//                }
//            )
//        );

        System.out.println(map);
    }

    @Test
    public void  test2 (){

    }

    @Test
    public void test3(){
        JSONArray array=new JSONArray();
        JSONObject j1=new JSONObject();
        j1.put("id","1");
        j1.put("name","lily");
        j1.put("count","198");
        JSONObject j2=new JSONObject();
        j2.put("id","1");
        j2.put("name","lily");
        j2.put("count","2");
        JSONObject j3=new JSONObject();
        j3.put("id","2");
        j3.put("name","lily");
        j3.put("count","300");
        array.add(j1);
        array.add(j2);
        array.add(j3);
        System.out.println(array);
        //合并
        JSONArray newArray=new JSONArray();
        Map<String, JSONObject> map = new HashMap<>();
        for(Object json:array){
            JSONObject  obj= (JSONObject)json;
            JSONObject  newObj= map.get(obj.getString("id"));
            if(newObj!=null){
                long countOfOld=Long.parseLong((String)newObj.get("count"));
                long countOfNew=Long.parseLong((String) obj.get("count"));
                long count=countOfOld+countOfNew;
                newObj.put("count",count);
            }else {
                map.put(obj.getString("id"),obj);
                newArray.add(obj);
            }
        }
        Iterator<Map.Entry<String, JSONObject>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, JSONObject> next = iterator.next();
            System.out.println(next.getKey()+"==="+next.getValue());
        }
    }


}
