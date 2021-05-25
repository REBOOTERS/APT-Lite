package com.engineer.apt_library;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author: rookie
 * @since: 2019-05-26
 */
public class QRouterApi {

    public static void go(Activity activity, String route) {
        try {
            long begin = System.currentTimeMillis();
            String log;
            Class tableClass = Class.forName("com.engineer.aptlite.QRouterTable");
            Method methodMap = tableClass.getMethod("map");
            HashMap<String, String> tables = (HashMap<String, String>) methodMap.invoke(tableClass.newInstance(), null);

            System.err.println("tables===" + tables);

            String target = tables.get(route);

            log = String.format(Locale.CHINA, "use %f to find the target", 1000f * (System.currentTimeMillis() - begin));
            System.err.println(log);

            if (!TextUtils.isEmpty(target)) {
                Class targetClass = Class.forName(target);
                Intent intent = new Intent(activity, targetClass);
                activity.startActivity(intent);

                log = String.format(Locale.CHINA, "use %f to finish", 1000f * (System.currentTimeMillis() - begin));
                System.err.println(log);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
