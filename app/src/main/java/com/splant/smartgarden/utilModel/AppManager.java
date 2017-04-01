package com.splant.smartgarden.utilModel;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by aifengbin on 2017/3/8.
 */

public class AppManager {

    private static Stack<Activity> activityStack = new Stack<Activity>();

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static boolean isLastActivity(Class<?> cls) {
        return activityStack.lastElement().getClass().equals(cls);
    }

    /**
     * 退到指定的Activity
     *
     * @param cls
     */
    public static void Back2Activity(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.lastElement().getClass().equals(cls)) {
                return;
            } else {
                activityStack.pop().finish();
            }
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 推出最后一个activity
     */
    public static void pop(Activity activity) {
        if (activityStack.lastElement().equals(activity)) {
            activityStack.pop();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public static Activity getLastActivity() {
        return activityStack.lastElement();
    }
    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
