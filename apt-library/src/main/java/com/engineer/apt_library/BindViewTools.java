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

            Method bindView = bindClass.getMethod("bindView", activity.getClass());
            bindView.invoke(bindClass.newInstance(), activity);

            Method methodBingString = bindClass.getMethod("bindString", activity.getClass());
            methodBingString.invoke(bindClass.newInstance(), activity);

        } catch (ClassNotFoundException | IllegalAccessException |
                InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, "bind: " + e.getMessage());
        }
    }
}
