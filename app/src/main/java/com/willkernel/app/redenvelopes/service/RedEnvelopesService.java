package com.willkernel.app.redenvelopes.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.willkernel.app.redenvelopes.BuildConfig;
import com.willkernel.app.redenvelopes.biz.AccessbilityBiz;
import com.willkernel.app.redenvelopes.biz.AlipayAccessbilityBiz;
import com.willkernel.app.redenvelopes.biz.WechatAccessbilityBiz;
import com.willkernel.app.redenvelopes.controller.IStatusBarNotification;
import com.willkernel.app.redenvelopes.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RedEnvelopesService extends AccessibilityService {
    private static final String TAG = "RedEnvelopes";
    private List<AccessbilityBiz> mAccessbilityBizlist;
    private HashMap<String, AccessbilityBiz> mPkgAccessbilityBizMap;

    private static RedEnvelopesService service;

    private static final Class[] ACCESSBILITY_BIZS = {
            WechatAccessbilityBiz.class, AlipayAccessbilityBiz.class
    };

    public RedEnvelopesService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAccessbilityBizlist = new ArrayList<>();
        mPkgAccessbilityBizMap = new HashMap<>();
        for (Class cls : ACCESSBILITY_BIZS) {
            try {
                Object object = cls.newInstance();
                if (object instanceof AccessbilityBiz) {
                    AccessbilityBiz biz = (AccessbilityBiz) object;
                    biz.onCreate(this);
                    mAccessbilityBizlist.add(biz);
                    mPkgAccessbilityBizMap.put(biz.getTargetPackageName(), biz);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收事件,如触发了通知栏变化、界面变化等
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "accessibilityEvent ==>" + accessibilityEvent);
        }
        String pkn = String.valueOf(accessibilityEvent.getPackageName());
        if (mAccessbilityBizlist != null && !mAccessbilityBizlist.isEmpty()) {
            if (!getConfig().isAgreement()) return;
            for (AccessbilityBiz biz : mAccessbilityBizlist) {
                if (pkn.equals(biz.getTargetPackageName()) && biz.isEnable()) {
                    biz.onReceive(accessibilityEvent);
                }
            }
        }
    }

    /**
     * 服务中断，如授权关闭或者将服务杀死
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    /**
     * 接收按键事件
     */
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    /**
     * 连接服务后,一般是在授权成功后会接收到
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_RED_ENVELOPES_SERVICE_CONNECT);
        sendBroadcast(intent);
        Toast.makeText(this, "已连接抢红包服务", Toast.LENGTH_SHORT).show();
    }

    /** 接收通知栏事件*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void handleNotificationPosted(IStatusBarNotification notificationService) {
        if(notificationService == null) {
            return;
        }
        if(service == null || service.mPkgAccessbilityBizMap == null) {
            return;
        }
        String pack = notificationService.getPackageName();
        AccessbilityBiz biz = service.mPkgAccessbilityBizMap.get(pack);
        if(biz == null) {
            return;
        }
        biz.onNotificationPosted(notificationService);
    }

    public static boolean isRunning(){
        if(service==null) return false;
        AccessibilityManager accessibilityManager= (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info=service.getServiceInfo();
        if(info==null){
            return false;
        }
        List<AccessibilityServiceInfo> list=accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator=list.iterator();
        boolean isConnect=false;
        while (iterator.hasNext()){
            AccessibilityServiceInfo i=iterator.next();
            if(i.getId().equals(info.getId())){
                isConnect=true;
                break;
            }
        }
        return isConnect;
    }

    public static boolean isNotificationServiceRunning(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN_MR2){
            return false;
        }
        //部份手机没有NotificationService服务
        try {
            return NotificationService.isRunning();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPkgAccessbilityBizMap != null) {
            mPkgAccessbilityBizMap.clear();
        }
        if (mAccessbilityBizlist != null && !mAccessbilityBizlist.isEmpty()) {
            for (AccessbilityBiz biz : mAccessbilityBizlist) {
                biz.onStop();
            }
            mAccessbilityBizlist.clear();
        }

        service = null;
        mAccessbilityBizlist = null;
        mPkgAccessbilityBizMap = null;
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_RED_ENVELOPES_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }

    public Config getConfig() {
        return Config.getConfig(this);
    }
}