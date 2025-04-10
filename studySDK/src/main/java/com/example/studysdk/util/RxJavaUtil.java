package com.example.studysdk.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.example.javatest.concurrent.atomic.AtomicInteger;
import com.liang.log.MLog;
import com.liang.rxjava3.core.Observable;
import com.liang.rxjava3.core.ObservableEmitter;
import com.liang.rxjava3.core.ObservableOnSubscribe;
import com.liang.rxjava3.core.Observer;
import com.liang.rxjava3.core.Scheduler;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.functions.Function;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RxJavaUtil {

    private static String TAG = "Rxjava, ";

    public static void test1() {


        // 创建一个 Observable，创建一个 Observer
        // observable.subscribe(observer) 时，会直接触发 Observable ObservableOnSubscribe subscribe\


        // 1. 创建了 ObservableCreate 对象，ObservableCreate 持有 ObservableOnSubscribe 对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            }
        });

        // 2. 调用了 ObservableCreate subscribeActual，进而调用了其 ObservableOnSubscribe 对象的 subscribe 方法
        observable.subscribe();

    }







    @SuppressLint("CheckResult")
    public static void test2() {


        System.setProperty("rx3.io-priority", String.valueOf(6));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
            @Override
            public Observable apply(Observable observable) throws Exception {
                MLog.i(TAG + "observable " + Thread.currentThread().getName());
                return observable;
            }
        });

        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                MLog.i(TAG + "scheduler " + Thread.currentThread().getName());
                return scheduler;
            }
        });

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                MLog.i(TAG + "observable create " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onComplete();
            }
        });

        observable.observeOn(Schedulers.io())
                // .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        MLog.i(TAG + "onSubscribe " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        MLog.i(TAG + "onNext " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        MLog.i(TAG + "onError " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        MLog.i(TAG + "onComplete " + Thread.currentThread().getName());
                    }
                });

//        observable.timer(,)



        // v -> MLog.i(TAG + "onnext " + Thread.currentThread().getName())


    }










    @SuppressLint("CheckResult")
    public static void test() {
//        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2/0);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        });

//        RxJavaPlugins.setOnObservableAssembly(observable -> {
//            return observable.doOnSubscribe(d -> MLog.i("RxJava", "Observable 被订阅"))
//                    .doOnNext(v -> MLog.i("RxJava", "发射数据: " + v));
//        });

//        RxJavaPlugins.setOnObservableAssembly(callable -> {
//            System.out.println("Observable assembly detected at: " + callable);
//            return callable.call();
//        });

        RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
            @Override
            public Observable apply(Observable observable) throws Exception {
                MLog.i("hook");
                return observable;
            }
        });
//
//        observable1.subscribe(
//                value -> MLog.i("onNext: " + value), // onNext
//                error -> MLog.i("onError: " + error),     // onError
//                () -> MLog.i("onComplete")                   // onComplete
//        );
//

//        Observable.just(1, 2, 3)
//                .map(num -> "Value: " + num) // 将整数转换为字符串
//                .subscribe(s -> MLog.i(s));



        /**
         * 调用流程
         * 1.
         */
//        Observable.just(1, 2, 3)
//                .map(num -> num*2) // 将整数转换为字符串
//                .subscribeOn()
//                .subscribe(System.out::println);


//        Observable.from()
//        Observable.just()
//        observable.sub
    }

    public static void decodeFoundationService(String state){
        FoundationServiceBean mAppStoreFoundationState;
        state = "{\"code\":\"000000\",\"data\":\"[{\\\"expireTime\\\":\\\"1898870399000\\\",\\\"expiredFlag\\\":0,\\\"functionCode\\\":\\\"appStore\\\",\\\"functionName\\\":\\\"appStore\\\",\\\"isActivate\\\":1,\\\"presetClassName\\\":\\\"基础服务\\\",\\\"remarkCN\\\":\\\"\\\",\\\"remarkEN\\\":\\\"\\\"}]\"}";
        FoundationServiceRootBean foundationServiceRootBean = GsonUtil.parseString2Object(state, FoundationServiceRootBean.class);
        if(foundationServiceRootBean == null){
            return;
        }
        String foundationServiceString = foundationServiceRootBean.getData();
        MLog.i(foundationServiceString);
        final ArrayList<FoundationServiceBean> list = GsonUtil.parseString2List(foundationServiceString, FoundationServiceBean.class);
        if(list == null || list.isEmpty()){
            return;
        }

        mAppStoreFoundationState = list.get(0);
        MLog.i( "mAppStoreFoundationState is " + mAppStoreFoundationState.getExpireTime());
    }



    public static void decodeFoundationServiceStateChanged(String state){
        FoundationServiceBean mAppStoreFoundationState;
        state = "{\"expireTime\":\"1898870399000\",\"expiredFlag\":0,\"functionCode\":\"appStore\",\"functionName\":\"appStore\",\"isActivate\":1,\"presetClassName\":\"基础服务\",\"remarkCN\":\"\",\"remarkEN\":\"\"}";
        FoundationServiceBean foundationServiceBean = GsonUtil.parseString2Object(state, FoundationServiceBean.class);
        if(foundationServiceBean == null){
            return;
        }
//        String foundationServiceString = foundationServiceRootBean.getData();
//        MLog.i(foundationServiceString);
//        final ArrayList<FoundationServiceBean> list = GsonUtil.parseString2List(foundationServiceString, FoundationServiceBean.class);
//        if(list == null || list.isEmpty()){
//            return;
//        }
//
        mAppStoreFoundationState = foundationServiceBean;
        MLog.i( "mAppStoreFoundationState is " + mAppStoreFoundationState.getExpireTime());
    }

}
