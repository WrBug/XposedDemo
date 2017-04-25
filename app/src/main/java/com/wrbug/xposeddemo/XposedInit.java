package com.wrbug.xposeddemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * XposedInit
 *
 * @author wrbug
 * @since 2017/4/20
 */
public class XposedInit implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.wrbug.xposeddemo")) {
            XposedHelpers.findAndHookMethod("com.wrbug.xposeddemo.MainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    //不能通过Class.forName()来获取Class ，在跨应用时会失效
                    Class c=lpparam.classLoader.loadClass("com.wrbug.xposeddemo.MainActivity");
                    Field field=c.getDeclaredField("textView");
                    field.setAccessible(true);
                    //param.thisObject 为执行该方法的对象，在这里指MainActivity
                    TextView textView= (TextView) field.get(param.thisObject);
                    textView.setText("Hello Xposed");
                }
            });
        }
    }
}
