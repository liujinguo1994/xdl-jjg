package com.xdl.jjg.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


/**
 * JSON 相关操作
 *
 * @author Sylow
 * 需要GJSON
 * 2015-07-14
 */
public class JsonUtil {

    /**
     * 把json格式的字符串转换为map对线
     *
     * @param json
     * @return LinkedHashMap
     */
    public static LinkedHashMap<String, Object> toMap(String json) {
        return toMap(parseJson(json));
    }

    /**
     * 把json数组格式的字符串转换为 List
     *
     * @return List<Object>
     */
    public static List<Map<String, Object>> toList(String jsonArr) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JsonArray jsonArray = parseJsonArray(jsonArr);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            Map<String, Object> map = toMap(value.toString());
            list.add(map);
        }
        return list;
    }


    public static String objectToJson(Object object) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 获取JsonObject
     *
     * @param json
     * @return
     */
    private static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    private static JsonArray parseJsonArray(String jsonArr) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(jsonArr).getAsJsonArray();
        return jsonArray;
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    private static LinkedHashMap<String, Object> toMap(JsonObject json) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        Set<Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter
                .hasNext(); ) {
            Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray) {
                map.put((String) key, toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                map.put((String) key, toMap((JsonObject) value));
            } else if (value instanceof JsonNull) {
                map.put((String) key, "");
            } else {
                String str = value.toString();
                if (str.startsWith("\"")) {
                    str = str.substring(1, str.length() - 1);
                    Object obj = str;
                    map.put((String) key, obj);
                } else {
                    map.put((String) key, value);
                }

            }

        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    private static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else if (value instanceof JsonNull) {
                list.add("");
            } else {
                String str = value.toString();
                if (str.startsWith("\"")) {
                    str = str.substring(1, str.length() - 1);
                    Object obj = str;
                    list.add(obj);
                } else {
                    list.add(value);
                }
            }
        }
        return list;
    }

    /**
     * 将json字符串转成list
     *
     * @param jsonData
     * @param clz
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> clz) {

        ObjectMapper mapper = new ObjectMapper();

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clz);

        try {
            return mapper.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将json字符串转成Object
     *
     * @param jsonData
     * @param clz
     * @return
     */
    public static <T> T jsonToObject(String jsonData, Class<T> clz) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(jsonData, clz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
