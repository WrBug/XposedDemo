package com.wrbug.xposeddemo;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XResources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * XposedInit
 *
 * @author wrbug
 * @since 2017/4/20
 */
public class XposedInit implements IXposedHookLoadPackage {
    private static String PACKAGE_NAME = "com.wrbug.xposeddemo";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(PACKAGE_NAME)) {
            try {
                //获取class类
                Class c = XposedHelpers.findClass("com.wrbug.xposeddemo.XposedTest", lpparam.classLoader);
                //获取str字段
                Field strField = XposedHelpers.findField(c, "str");
                //可以省略setAccessible（true）XposedHelpers已经执行该操作
                strField.setAccessible(true);
                //打印str值，下同
                XposedBridge.log(strField.get(null).toString());

                //调用静态方法
                XposedHelpers.callStaticMethod(c, "staticTest", "xposedInvoke");
                XposedBridge.log(strField.get(null).toString());

                //获取实例
                Constructor constructor = XposedHelpers.findConstructorBestMatch(c);
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                XposedHelpers.callMethod(instance, "test");
                XposedBridge.log(strField.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
