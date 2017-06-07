package com.example.che.annocationdemo.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.che.annocationdemo.DynamicHandler;
import com.example.che.annocationdemo.anno.ContentView;
import com.example.che.annocationdemo.anno.EventBase;
import com.example.che.annocationdemo.anno.OnClick;
import com.example.che.annocationdemo.anno.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by che on 2017/6/6.
 */

public class ViewInjectUtils {

    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
    }

    private static void injectContentView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int contentLayoutId = contentView.value();

            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity, contentLayoutId);
                method.setAccessible(false);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }

    private static void injectViews(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int id = viewInject.value();
                Log.e("TAG",id+"");
                if (id != -1) {
                    try {
                        Method method = clazz.getMethod("findViewById", int.class);
                        Object resView = method.invoke(activity, id);
                        field.setAccessible(true);
                        field.set(activity, resView);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick!=null){
                EventBase eventBase = onClick.annotationType().getAnnotation(EventBase.class);
                if (eventBase!=null){
                    String listenerSetter =  eventBase.listenerSetter();
                    String methodName = eventBase.methodName();
                    Class<?> listenerType  = eventBase.listenerType();
                    Log.e("TAG",listenerSetter);
                    Log.e("TAG",methodName);
                    Log.e("TAG",listenerType.getSimpleName());
                     //通过InvocationHandler设置代理
                     DynamicHandler handler = new DynamicHandler(activity);
                     handler.addMethod(methodName, method);
                     Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[] { listenerType }, handler);

                    int[] viewIds = onClick.value();
                      //遍历所有的View，设置事件
                    for (int viewId : viewIds)
                       {
                           View view = activity.findViewById(viewId);
                           Method setEventListenerMethod = null;
                           try {
                               setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                               setEventListenerMethod.invoke(view, listener);
                           } catch (NoSuchMethodException e) {
                               e.printStackTrace();
                           } catch (InvocationTargetException e) {
                               e.printStackTrace();
                           } catch (IllegalAccessException e) {
                               e.printStackTrace();
                           }

                       }
                }
            }
        }
    }
}
