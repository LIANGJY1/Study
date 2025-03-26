package com.example.studysdk.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson
 * 解析工具静态类
 * */
@SuppressWarnings({"rawtypes", "unused"})
final public class GsonUtil {

    public synchronized static <T> ArrayList<T> parseString2List(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        return new Gson().fromJson(json, type);
    }

    public synchronized static <T>T parseString2Object(String json, Class<T> clazz){
        return new Gson().fromJson(json, clazz);
    }

    public synchronized static String parseObject2String(Object object){
        return new Gson().toJson(object, object.getClass());
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        final Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }
        @NonNull
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }
        @NonNull
        @Override
        public Type getRawType() {
            return List.class;
        }
        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
