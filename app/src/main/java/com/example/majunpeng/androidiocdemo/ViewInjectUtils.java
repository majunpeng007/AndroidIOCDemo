package com.example.majunpeng.androidiocdemo;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by majunpeng on 2018/12/5.
 */

public class ViewInjectUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";
    private static String TAG = "ViewInjectUtils.class";

    public static void inject(Activity activity) {
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
    }

    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods();
        //遍历所有的方法
        for (Method method : methods) {
            Annotation [] annotations = method.getAnnotations();
            //拿到方法上的所有注解
            for (Annotation annotation : annotations) {
                Class <? extends Annotation> annotationType = annotation.annotationType();
                //拿到注解上的注解
                EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
                if (eventBaseAnnotation != null) {
                    String listenerSetter = eventBaseAnnotation.listenerSetter();
                    Class <?> listenerType = eventBaseAnnotation.listenerType();
                    String methodName = eventBaseAnnotation.MethodName();

                    try {
                        //拿到onClick注解中的value方法
                        Method aMethod = annotationType.getDeclaredMethod("value");
                        int [] viewIds = (int[]) aMethod.invoke(annotation,null);

                        //通过InvocationHandler设置代理
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(methodName,method);
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class<?>[]{listenerType},handler);

                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass().getMethod(listenerSetter,listenerType);
                            setEventListenerMethod.invoke(view,listener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field [] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInjectAnnotation = field.getAnnotation(ViewInject.class);
            if (viewInjectAnnotation != null) {
                int viewId = viewInjectAnnotation.value();
                if (viewId != -1) {
                    Log.e(TAG, viewId + "" );
                    try {
                        Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID,int.class);
                        Object resView = method.invoke(activity,viewId);
                        field.setAccessible(true);
                        field.set(activity,resView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 通过传入的activity对象，获得它的Class类型，判断是否写了ContentView这个注解，
     * 如果写了，读取它的Value,然后得到setContentView这个方法，使用invoke进行调用
     * @param activity
     */
    private static void injectContentView(Activity activity) {
        Class <? extends Activity> clazz = activity.getClass();
        //查询类上是否存在ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {//存在
            int contentViewLayoutId = contentView.value();

            try {
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,int.class);
                method.setAccessible(true);
                method.invoke(activity,contentViewLayoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
