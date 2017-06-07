package com.example.che.annocationdemo;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by che on 2017/6/6.
 */

public class DynamicHandler implements InvocationHandler {
    private WeakReference<Object> handlerRef;
    private final HashMap<String, Method> methodMap = new HashMap<>(1);

    public DynamicHandler(Object handler) {
        this.handlerRef = new WeakReference<>(handler);
    }

    public void addMethod(String name, Method method) {
        methodMap.put(name, method);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object handler = handlerRef.get();
        if (handler!=null){
            String methodName = method.getName();
            method = methodMap.get(methodName);
            Log.e("TAG===",method.getName());
            if (method!=null){
                method.invoke(handler,args);
            }
        }

        return null;
    }
}
