package com.wrbug.xposeddemo;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XResources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
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
public class XposedInit implements IXposedHookLoadPackage, IXposedHookInitPackageResources {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.wrbug.xposeddemo")) {
            XposedHelpers.findAndHookMethod("com.wrbug.xposeddemo.MainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    //不能通过Class.forName()来获取Class ，在跨应用时会失效
                    Class c = lpparam.classLoader.loadClass("com.wrbug.xposeddemo.MainActivity");
                    Field field = c.getDeclaredField("textView");
                    field.setAccessible(true);
                    //param.thisObject 为执行该方法的对象，在这里指MainActivity
                    TextView textView = (TextView) field.get(param.thisObject);
                    textView.setText("Hello Xposed");
                }
            });
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (resparam.packageName.equals("com.wrbug.xposeddemo")) {
            resparam.res.hookLayout(resparam.packageName, "layout", "activity_main", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                    printView((ViewGroup) liparam.view, 1);
                }
            });
            resparam.res.hookLayout(resparam.packageName, "layout", "view_demo", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                    XposedBridge.log("hook view_demo");
                }
            });
        }
    }

    //遍历资源布局树，并打印出来
    private void printView(ViewGroup view, int deep) {
        String viewgroupDeepFormat = "";
        String viewDeepFormat = "";
        for (int i = 0; i < deep - 1; i++) {
            viewgroupDeepFormat += "\t";
        }
        viewDeepFormat = viewgroupDeepFormat + "\t";
        XposedBridge.log(viewgroupDeepFormat + view.toString());
        int count = view.getChildCount();
        for (int i = 0; i < count; i++) {
            if (view.getChildAt(i) instanceof ViewGroup) {
                printView((ViewGroup) view.getChildAt(i), deep + 1);
            } else {
                XposedBridge.log(viewDeepFormat + view.getChildAt(i).toString());
            }
        }
    }
}
