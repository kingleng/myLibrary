package com.example.leng.myapplication2.view.tools;

import org.reactivestreams.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by leng on 2017/3/1.
 */

public class RxBus {

    private static final RxBus INSTANCE = new RxBus();

//    private final Subject<Object> mBusSubject = new SerializedSubject<>(PublishSubject.create());

    private final Map<String ,List<Flowable>> maps = new HashMap<>();

    public static RxBus getInstance(){
        return INSTANCE;
    }

//    public <T>Observable<T> register(@NonNull Object tag,@NonNull Class<T> clazz){
//        List<Subject> subjects = maps.get(tag);
//        if(subjects == null){
//            subjects = new ArrayList<>();
//            maps.put(tag,subjects);
//        }
//        Subject<T> object = PublishSubject.<T>create();
//        subjects.add(object);
//        return object;
//    }
//
//    public void unregister(@NonNull Object tag,@NonNull Observable observable){
//        List<Subject> subjects = maps.get(tag);
//        if(subjects!=null){
//            subjects.remove(observable);
//            if(subjects.isEmpty()){
//                maps.remove(tag);
//            }
//        }
//    }



}
