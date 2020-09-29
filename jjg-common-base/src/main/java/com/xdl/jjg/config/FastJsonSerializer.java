package com.xdl.jjg.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月2019/7/27日
 * @Description redis序列化
 */

public class FastJsonSerializer implements RedisSerializer<Object> {


    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayOutputStream.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = inputStream.readObject();
            return obj;
        }catch (Exception e){
            String str = new String(bytes, Charset.forName("UTF-8"));
            return  str;
        }
    }


}
