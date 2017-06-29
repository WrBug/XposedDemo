package com.wrbug.xposeddemo;

/**
 * XposedTest
 *
 * @author suanlafen
 * @since 2017/6/29
 */
public class XposedTest {
    private static String str = "no content";

    public XposedTest() {
        str = "XposedTest Init";
    }

    public static void staticTest(String s) {
        str = "static staticTest( " + s + " )  called";
    }

    private void test() {
        str = "test() called";
    }
}
