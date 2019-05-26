package com.engineer.apt_library;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BindViewTools {
    private static final String TAG = "BindViewTools";

    public static void bind(Activity activity) {

        Class clazz = activity.getClass();
        try {
            Class bindClass = Class.forName(clazz.getName() + "_Binding");

            bindInternal(activity, bindClass, "bindView");

            bindInternal(activity, bindClass, "bindString");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "bindInternal: " + e.getMessage());
        }
    }

    private static void bindInternal(Activity activity, Class bindClass, String bindString) {
        try {
            Method methodBingString = bindClass.getMethod(bindString, activity.getClass());
            methodBingString.invoke(bindClass.newInstance(), activity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
