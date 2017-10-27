package main.com.test_jpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;

/**
 * @author LiHongCheng
 * @version V1.0
 * @Package main.com.test_jpush
 * @Description: 接受激光推送自定义消息广播
 * @mail diosamolee2014@gmail.com
 * @date 2017/10/25 16:03
 */
public class MyReceiver extends BroadcastReceiver{

      private static final String TAG = "MyReceiver";
    private static final String TYPE_THIS = "main";
    private static final String TYPE_ANOTHER = "first";

    private NotificationManager nm;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == nm) {
                nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            Bundle bundle = intent.getExtras();
            Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            String registrationID = JPushInterface.getRegistrationID(context);
            Log.d(TAG, "onReceive: "+"registrationID"+registrationID);
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                Log.d(TAG, "JPush用户注册成功");

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的自定义消息");
                Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
                try {
                    JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    String title = jsonObject.getString("title");
                    String msg = jsonObject.getString("msg");
                    String style = jsonObject.getString("style");
                    String activity = jsonObject.getString("Activity");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    if ("1".equals(style)){
                        builder.setSmallIcon(R.mipmap.ic1);
                    }else  if ("2".equals(style)){
                        builder.setSmallIcon(R.mipmap.ic2);
                    }else {
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                    }
                    builder.setContentTitle(title);
                    builder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE)+msg);
                    builder.setNumber(1);
                    //设置点击通知跳转页面后，通知消失
                    builder.setAutoCancel(true);
                    Intent intent1 = new Intent();
                    if ("first".equals(activity)){
                        intent1.setClass(context,MainActivity.class);
                    }else if ("two".equals(activity)){
                        intent1.setClass(context,FirstActivity.class);
                    }else {
                        intent1.setClass(context,MainActivity.class);
                    }
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pi);
                    Notification notification = builder.build();
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(3,notification);
                    //点亮屏幕
                    PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
                    mWakelock.acquire();
                    mWakelock.release();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的通知");

                receivingNotification(context,bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "用户点击打开了通知");

                openNotification(context,bundle);

            } else {
                Log.d(TAG, "Unhandled intent - " + intent.getAction());
            }
        }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        if (TYPE_THIS.equals(myValue)) {
            Intent mIntent = new Intent(context, MainActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        } else if (TYPE_ANOTHER.equals(myValue)){
            Intent mIntent = new Intent(context, FirstActivity.class);
            mIntent.putExtras(bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }else if ("123".equals(myValue)){
            Toast.makeText(context, "发送了123", Toast.LENGTH_SHORT).show();
        }
    }

//    private void processCustomMessage(Context context, Bundle bundle) {
//        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        if (TextUtils.isEmpty(title)) {
//            Log.w(TAG, "Unexpected: empty title (friend). Give up");
//            return;
//        }
//
//        boolean needIncreaseUnread = true;
//
//        if (title.equalsIgnoreCase(Bitmap.Config.myName)) {
//            Log.d(TAG, "Message from myself. Give up");
//            needIncreaseUnread = false;
//            if (!Config.IS_TEST_MODE) {
//                return;
//            }
//        }
//
//        String channel = null;
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        try {
//            JSONObject extrasJson = new JSONObject(extras);
//            channel = extrasJson.optString(Constants.KEY_CHANNEL);
//        } catch (Exception e) {
//            Log.w(TAG, "Unexpected: extras is not a valid json", e);
//        }
//
//        // Send message to UI (Webview) only when UI is up
//        if (!Config.isBackground) {
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(Constants.KEY_MESSAGE, message);
//            msgIntent.putExtra(Constants.KEY_TITLE, title);
//            if (null != channel) {
//                msgIntent.putExtra(Constants.KEY_CHANNEL, channel);
//            }
//
//            JSONObject all = new JSONObject();
//            try {
//                all.put(Constants.KEY_TITLE, title);
//                all.put(Constants.KEY_MESSAGE, message);
//                all.put(Constants.KEY_EXTRAS, new JSONObject(extras));
//            } catch (JSONException e) {
//            }
//            msgIntent.putExtra("all", all.toString());
//
//            context.sendBroadcast(msgIntent);
//        }
//
//        String chatting = title;
//        if (!TextUtils.isEmpty(channel)) {
//            chatting = channel;
//        }
//
//        String currentChatting = MyPreferenceManager.getString(Constants.PREF_CURRENT_CHATTING, null);
//        if (chatting.equalsIgnoreCase(currentChatting)) {
//            Log.d(TAG, "Is now chatting with - " + chatting + ". Dont show notificaiton.");
//            needIncreaseUnread = false;
//            if (!Config.IS_TEST_MODE) {
//                return;
//            }
//        }
//
//        if (needIncreaseUnread) {
//            unreadMessage(title, channel);
//        }
//
//        NotificationHelper.showMessageNotification(context, nm, title, message, channel);
//    }
//
//    // When received message, increase unread number for Recent Chat
//    private void unreadMessage(final String friend, final String channel) {
//        new Thread() {
//            public void run() {
//                String chattingFriend = null;
//                if (!TextUtils.isEmpty(channel)) {
//                    chattingFriend = friend;
//                }
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("udid", Config.udid);
//                params.put("friend", chattingFriend);
//                params.put("channel_name", channel);
//
//                try {
//                    HttpHelper.post(Constants.PATH_UNREAD, params);
//                } catch (Exception e) {
//                    Log.e(TAG, "Call pushtalk api to report unread error", e);
//                }
//            }
//        }.start();
//    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
