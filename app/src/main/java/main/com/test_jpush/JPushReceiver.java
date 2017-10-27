package main.com.test_jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * @author LiHongCheng
 * @version V1.0
 * @Package main.com.test_jpush
 * @Description: 接受激光推送的广播
 * @mail diosamolee2014@gmail.com
 * @date 2017/10/25 16:03
 */
public class JPushReceiver extends JPushMessageReceiver {
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        Log.d("dada", "onAliasOperatorResult: ");
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
        Log.d("dada", "onCheckTagOperatorResult: ");
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        Log.d("dada", "onTagOperatorResult: ");
    }


}
