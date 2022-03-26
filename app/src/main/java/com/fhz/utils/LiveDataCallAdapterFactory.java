package com.fhz.utils;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * 时间:11/9/21
 *
 * @author msfeng
 * 简述: T自定义 LiveDataAdapter Factory
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    private static final String TAG = LiveDataCallAdapterFactory.class.getSimpleName();

    @SuppressWarnings("ClassGetClass")
    @Nullable
    @Override
    public CallAdapter<?, ?> get( Type returnType,  Annotation[] annotations,  Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawType = getRawType(observableType);
        Log.d(TAG, "rawType = " + rawType.getClass().getSimpleName());
        boolean isApiResponse = true;
        if (rawType != ApiResponse.class) {
            //不是返回ApiResponse类型的返回值
            isApiResponse = false;
        }

        //判断第一个参数是不是泛型
//        if (!(observableType instanceof ParameterizedType)) {
//            throw new IllegalArgumentException("resource must be parameterized");
//        }
        return new LiveDataCallAdapter<>(observableType, isApiResponse);
    }
}
