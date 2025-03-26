package com.example.studysdk.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.liang.log.MLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ApkUtil {
    public static String TAG = "ApkUtil, ";

    public static void test1()  {
        List<String> asApps = Arrays.asList(
                "{\"servicePackage\":\"Alice\",\"hmiPackage\":530,\"name\":\"Alice\",\"usage\":30}",
                "{\"servicePackage\":\"Alice\",\"hmiPackage\":3540,\"name\":\"Alice\",\"usage\":30}",
                "{\"servicePackage\":\"Alice\",\"hmiPackage\":30,\"name\":\"Alice\",\"usage\":30}"
        );
        MLog.i(TAG+"secondScreenAppConfig:"+asApps);
        Map<String, Set<String>> map =new HashMap<>();
        for (String s : asApps) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String servicePackage = "";
            if (jsonObject.has("servicePackage")){
                try {
                    servicePackage = jsonObject.getString("servicePackage");//服务包名
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            String hmiPackage= "";
            if (jsonObject.has("hmiPackage")){
                try {
                    hmiPackage = jsonObject.getString("hmiPackage");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            String name= "";
            if (jsonObject.has("name")){
                try {
                    name = jsonObject.getString("name");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            Set<String> array = map.computeIfAbsent(servicePackage, new Function<String, Set<String>>() {
                @Override
                public Set<String> apply(String s) {
                    return new HashSet<>();
                }
            });
            assert array != null;
            array.add(hmiPackage);
//            array.add(name);
        }
        map.forEach((key, value) -> MLog.i(TAG+ key + "    "+ value));

        Map<String,String[]> map2 =new HashMap<>();
        map.forEach(new BiConsumer<String, Set<String>>() {
            @Override
            public void accept(String s, Set<String> strings) {
                map2.put(s, strings.toArray(new String[]{}));
                MLog.i(TAG+"secondScreenAppConfig2: " + s + "," + strings);
            }
        });
    }

    public static void test(Context context) {
//        // 假设你在一个Activity或Context中调用这个方法
//        PackageManager packageManager = context.getPackageManager();
//        List<PackageInfo> installedApps = getInstalledApps(packageManager);
//
//        for (PackageInfo appInfo : installedApps) {
//            MLog.i(TAG + "App Name: " + appInfo.applicationInfo.loadLabel(packageManager).toString());
//            MLog.i(TAG + "Package Name: " + appInfo.packageName);
//        }

//        if (isInstalled(context, "com.example.test")) {
//            MLog.i(TAG + "1");
//        }
//        if (isInstalled(context, "com.liang.test")) {
//            MLog.i(TAG + "2");
//        }

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedApps = getAllInstalledApps(packageManager);

        for (PackageInfo appInfo : installedApps) {
//            MLog.i(TAG + "App Name: " + appInfo.applicationInfo.loadLabel(packageManager).toString() + "; Package Name: " + appInfo.packageName + "; Is System App: " + ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0));
            MLog.i(TAG + "Package Name: " + appInfo.packageName + "; Is System App: " + ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0));
        }
    }

    public static List<PackageInfo> getInstalledApps(PackageManager packageManager) {
        List<PackageInfo> apps = new ArrayList<>();

        // 获取已安装应用的列表（不包括系统应用）
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0);
                apps.add(packageInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return apps;
    }

    public static List<PackageInfo> getAllInstalledApps(PackageManager packageManager) {
        List<PackageInfo> apps = new ArrayList<>();

        // 获取所有已安装的包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        for (PackageInfo packageInfo : installedPackages) {
            // 可以根据需要过滤应用，例如仅获取用户应用或系统应用
             if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                 // 用户应用
                 apps.add(packageInfo);
             } else {
                 // 系统应用
             }
//            apps.add(packageInfo);
        }

        return apps;
    }


    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            MLog.i(TAG + "installed");
            installed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return installed;
    }
}
