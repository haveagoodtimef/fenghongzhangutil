package com.fhz.utils;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 时间:11/9/21
 *
 * @author msfeng
 * 简述: 自定义 LiveDataAdapter
 */
public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private Type mResponseType;
    private boolean isApiResponse;

    LiveDataCallAdapter(Type mResponseType, boolean isApiResponse) {
        this.mResponseType = mResponseType;
        this.isApiResponse = isApiResponse;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public LiveData<T> adapt( final Call<T> call) {
        return new MyLiveData<>(call, isApiResponse);
    }

    private static class MyLiveData<T> extends LiveData<T> {

        private AtomicBoolean stared = new AtomicBoolean(false);
        private final Call<T> call;
        private boolean isApiResponse;

        MyLiveData(Call<T> call, boolean isApiResponse) {
            this.call = call;
            this.isApiResponse = isApiResponse;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //确保执行一次
            if (stared.compareAndSet(false, true)) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse( Call<T> call,  Response<T> response) {
                        T body = response.body();
                        postValue(body);
                    }

                    @Override
                    public void onFailure( Call<T> call,  Throwable t) {
                        if (isApiResponse) {
                            //noinspection unchecked
                            postValue((T) new ApiResponse<>(ApiResponse.CODE_ERROR, t.getMessage()));
                        } else {
                            postValue(null);
                        }
                    }
                });
            }
        }
    }
}

