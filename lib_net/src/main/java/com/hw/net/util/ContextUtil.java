package com.hw.net.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hewei(David)
 * @date 2020-03-20  16:46
 * @Copyright ©  Shanghai Xinke Digital Technology Co., Ltd.
 * @description java类
 * 此类介绍通过java反射获取application 全局的context,而不需要传入activity、context
 */

public class ContextUtil {
    /**
     * 方法一：反射得到mInitialApplication域
     * 我们注意到android.app.ActivityThread类有个mInitialApplication成员变量，其类型就是Application，
     * 我们尝试反射得到该成员变量，然而该成员并不是静态的，所以首先我们需要得到当前的ActivityThread的实例，
     * 我们注意到ActivityThread有个currentActivityThread静态方法，我们可以使用其来得到当前的实例。
     * 得到实例后，我们就可以通过反射来得到mInitialApplication成员变量。
     * @return Application
     */
    @SuppressLint("PrivateApi")
    public static Context getApplicationContext() {
        Application application = null;
        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Field appField = activityThreadClass
                    .getDeclaredField("mInitialApplication");
            // Object object = activityThreadClass.newInstance();
            final Method method = activityThreadClass.getMethod(
                    "currentActivityThread");
            // 得到当前的ActivityThread对象
            Object localObject = method.invoke(null, (Object[]) null);
            appField.setAccessible(true);
            application = (Application) appField.get(localObject);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return application.getApplicationContext();
    }

    /**
     * 方法二：反射调用ActivityThread.getApplication方法
     * 其实这种方法和上面方法的原理是一样的，都是得到mInitialApplication域，
     * 但是，这次我们是调用getApplication方法来得到的，首先，getApplication并不是静态方法，
     * 所以我们用同样的方法先得到ActivityThread实例，然后调用getApplication方法。
     * @return Application
     */
    @SuppressLint("PrivateApi")
    public static Context getApplicationContext1() {
        Application application = null;
        Class<?> activityThreadClass;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method2 = activityThreadClass.getMethod(
                    "currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            Object localObject = method2.invoke(null, (Object[]) null);

            final Method method = activityThreadClass
                    .getMethod("getApplication");
            application = (Application) method.invoke(localObject, (Object[]) null);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return application.getApplicationContext();
    }
}
