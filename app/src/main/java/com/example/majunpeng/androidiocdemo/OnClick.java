package com.example.majunpeng.androidiocdemo;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by majunpeng on 2018/12/5.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerType = View.OnClickListener.class,listenerSetter = "setOnClickListener",MethodName = "onClick")
public @interface OnClick {
    int[] value();
}
