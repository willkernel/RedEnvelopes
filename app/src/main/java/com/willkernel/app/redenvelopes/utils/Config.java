package com.willkernel.app.redenvelopes.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

    public static final String ACTION_RED_ENVELOPES_SERVICE_DISCONNECT = "com.willkernel.app.redenvelopes.ACCESSBILITY_DISCONNECT";
    public static final String ACTION_RED_ENVELOPES_SERVICE_CONNECT = "com.willkernel.app.redenvelopes.ACCESSBILITY_CONNECT";

    public static final String ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT = "com.willkernel.app.redenvelopes.NOTIFY_LISTENER_DISCONNECT";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "com.willkernel.app.redenvelopes.NOTIFY_LISTENER_CONNECT";

    public static final String PREFERENCE_NAME = "config";
    public static final String KEY_ENABLE_WECHAT = "KEY_ENABLE_WECHAT";
    public static final String KEY_ENABLE_ALIPAY = "KEY_ENABLE_ALIPAY";
    public static final String KEY_WECHAT_AFTER_OPEN_HONGBAO = "KEY_WECHAT_AFTER_OPEN_HONGBAO";
    public static final String KEY_WECHAT_DELAY_TIME = "KEY_WECHAT_DELAY_TIME";
    public static final String KEY_WECHAT_AFTER_GET_HONGBAO = "KEY_WECHAT_AFTER_GET_HONGBAO";
    public static final String KEY_WECHAT_MODE = "KEY_WECHAT_MODE";

    public static final String KEY_NOTIFICATION_SERVICE_ENABLE = "KEY_NOTIFICATION_SERVICE_ENABLE";
    public static final String KEY_NOTIFICATION_SERVICE_TEMP_ENABLE = "KEY_NOTIFICATION_SERVICE_TEMP_ENABLE";

    public static final String KEY_NOTIFY_SOUND = "KEY_NOTIFY_SOUND";
    public static final String KEY_NOTIFY_VIBRATE = "KEY_NOTIFY_VIBRATE";
    public static final String KEY_NOTIFY_NIGHT_ENABLE = "KEY_NOTIFY_NIGHT_ENABLE";

    private static final String KEY_AGREEMENT = "KEY_AGREEMENT";

    public static final int WX_AFTER_OPEN_HONGBAO = 0;//拆红包
    public static final int WX_AFTER_OPEN_SEE = 1; //看大家手气
    public static final int WX_AFTER_OPEN_NONE = 2; //静静地看着

    public static final int WX_AFTER_GET_GOHOME = 0; //返回桌面
    public static final int WX_AFTER_GET_NONE = 1;

    public static final int WX_MODE_0 = 0;//自动抢
    public static final int WX_MODE_1 = 1;//抢单聊红包,群聊红包只通知
    public static final int WX_MODE_2 = 2;//抢群聊红包,单聊红包只通知
    public static final int WX_MODE_3 = 3;//通知手动抢

    private static Config current;

    public static synchronized Config getConfig(Context context) {
        if (current == null) {
            current = new Config(context.getApplicationContext());
        }
        return current;
    }

    private SharedPreferences preferences;
    private Context mContext;

    private Config(Context context) {
        mContext = context;
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 是否启动微信抢红包
     */
    public boolean isEnableWechat() {
        return preferences.getBoolean(KEY_ENABLE_WECHAT, true) && UmengConfig.isEnableWechat(mContext);
    }

    /** 是否启动支付宝*/
    public boolean isEnableAlipay() {
        return preferences.getBoolean(KEY_ENABLE_ALIPAY, true) && UmengConfig.isEnableWechat(mContext);
    }

    /**
     * 微信打开红包后的事件
     */
    public int getWechatAfterOpenHongBaoEvent() {
        int defaultValue = 0;
        String result = preferences.getString(KEY_WECHAT_AFTER_OPEN_HONGBAO, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 微信抢到红包后的事件
     */
    public int getWechatAfterGetHongBaoEvent() {
        int defaultValue = 1;
        String result = preferences.getString(KEY_WECHAT_AFTER_GET_HONGBAO, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 微信打开红包后延时时间
     */
    public int getWechatOpenDelayTime() {
        int defaultValue = 0;
        String result = preferences.getString(KEY_WECHAT_DELAY_TIME, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 获取抢微信红包的模式
     */
    public int getWechatMode() {
        int defaultValue = 0;
        String result = preferences.getString(KEY_WECHAT_MODE, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(result);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public void setWeChatMode(int weChatMode) {
        preferences.edit().putString(KEY_WECHAT_MODE, String.valueOf(weChatMode)).apply();
    }

    public void setWechatAfterOpenHongbaoEvent(int mode){
        preferences.edit().putString(KEY_WECHAT_AFTER_OPEN_HONGBAO, String.valueOf(mode)).apply();
    }

    public void setWechatAfterGetHongBaoEvent(int mode) {
        preferences.edit().putString(KEY_WECHAT_AFTER_GET_HONGBAO, String.valueOf(mode)).apply();
    }

    /**
     * 是否启动通知栏模式
     */
    public boolean isEnableNotificationService() {
        return true;
    }

    public void setNotificationServiceEnable(boolean enable) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_SERVICE_ENABLE, enable).apply();
    }

    public void setNotificationListener(boolean enable){
        preferences.edit().putBoolean(KEY_NOTIFICATION_SERVICE_TEMP_ENABLE,enable).apply();
    }

    /**
     * 是否开启声音
     */
    public boolean isNotifySound() {
        return true;
    }

    /**
     * 是否开启震动
     */
    public boolean isNotifyVibrate() {
        return true;
    }

    /**
     * 是否开启夜间免打扰模式
     */
    public boolean isNotifyNight() {
        return preferences.getBoolean(KEY_NOTIFY_NIGHT_ENABLE, false);
    }

    public void setNotifiSound(boolean enable) {
        preferences.edit().putBoolean(KEY_NOTIFY_SOUND, enable).apply();
    }

    /**
     * 免费声明
     */
    public boolean isAgreement() {
        return true;
    }

    /**
     * 设置是否同意
     */
    public void setAgreement(boolean agreement) {
        preferences.edit().putBoolean(KEY_AGREEMENT, agreement).apply();
    }

}
