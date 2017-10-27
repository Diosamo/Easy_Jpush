package main.com.test_jpush;

import android.app.Notification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.content);

        mTextView.setText(this.getClass().toString());

            //设置别名
        JPushInterface.setAlias(this,1,"dada");
        //设置tag和添加tag
        Set<String> strings = new HashSet<>();
//        strings.add("da");
        strings.add("dad");
        JPushInterface.setTags(this, 1, strings);
//        JPushInterface.addTags(this, 1, strings);

    //默认样式需要适配
        //设置不同样式
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.mipmap.ic1;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);


        //小米中兴都可以
        CustomPushNotificationBuilder builder2 = new
                CustomPushNotificationBuilder(this,
                R.layout.customer_notitfication_layout,
                R.id.icon,
                R.id.title,
                R.id.text);
        // 指定定制的 Notification Layout
        builder2.statusBarDrawable = R.mipmap.ic2;
        // 指定最顶层状态栏小图标
        builder2.layoutIconDrawable = R.mipmap.ic3;
        // 指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(2, builder2);


        //TODO 机型适配问题
        MultiActionsNotificationBuilder builder3 = new MultiActionsNotificationBuilder(this);
//添加按钮，参数(按钮图片、按钮文字、扩展数据)
        builder3.addJPushAction(R.mipmap.ic1, "first", "my_extra1");
        builder3.addJPushAction(R.mipmap.ic2, "second", "my_extra2");
        builder3.addJPushAction(R.mipmap.ic3, "third", "my_extra3");
        JPushInterface.setPushNotificationBuilder(3, builder3);



    }
}
