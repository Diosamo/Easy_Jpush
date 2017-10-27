package main.com.test_jpush;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * @author LiHongCheng
 * @version V1.0
 * @Package main.com.test_jpush
 * @Description: 程序入口全局类
 * @mail diosamolee2014@gmail.com
 * @date 2017/10/25 15:59
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化激光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);



    }
}
