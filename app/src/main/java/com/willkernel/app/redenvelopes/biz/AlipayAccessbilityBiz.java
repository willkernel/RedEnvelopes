package com.willkernel.app.redenvelopes.biz;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.willkernel.app.redenvelopes.BuildConfig;
import com.willkernel.app.redenvelopes.controller.IStatusBarNotification;
import com.willkernel.app.redenvelopes.service.RedEnvelopesService;
import com.willkernel.app.redenvelopes.utils.AccessibilityHelper;

/**
 * Created by willkernel on 2017/1/20.
 * mail:willkerneljc@gmail.com
 */

public class AlipayAccessbilityBiz extends BaseAccessbilityBiz{
    private static final String TAG = "AlipayAccessbilityJob";
    /**
     * 支付宝的包名
     */
    public static final String ALIPAY_PACKAGENAME = "com.eg.android.AlipayGphone";
    /**
     * 不能再使用文字匹配的最小版本号
     */
    private static final int USE_ID_MIN_VERSION = 105;
    private PackageInfo mAliPayPackageInfo = null;
    private Handler mHandler = new Handler();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新安装包信息
            updatePackageInfo();
        }
    };

    @Override
    public void onCreate(RedEnvelopesService service) {
        super.onCreate(service);
        updatePackageInfo();

        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("package");
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");

        getContext().registerReceiver(broadcastReceiver, filter);
    }


    @Override
    public String getTargetPackageName() {
        return ALIPAY_PACKAGENAME;
    }

    @Override
    public void onReceive(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getService().getRootInActiveWindow();
        final AccessibilityNodeInfo lucky_button = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.scan.arplatform:id/lucky_button");
        long delay = 500;
        if (lucky_button != null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "-->:" + lucky_button);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityHelper.performClick(lucky_button);
                }
            }, delay);
        }
        final AccessibilityNodeInfo cover_click_button = AccessibilityHelper.findNodeInfosById(nodeInfo, "com.alipay.mobile.scan.arplatform:id/cover_click_button");
        if (cover_click_button != null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "-->点击重试:" + cover_click_button);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityHelper.performClick(cover_click_button);
                }
            }, delay);
        }
        final AccessibilityNodeInfo node3 = AccessibilityHelper.findNodeInfosByText(nodeInfo, "收入");
        if (node3 != null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "-->收入:" + node3);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityHelper.performClick(node3);
                }
            }, delay);
        }
    }

    @Override
    public void onStop() {
        try {
            getContext().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public void onNotificationPosted(IStatusBarNotification service) {

    }

    @Override
    public boolean isEnable() {
        return getConfig().isEnableAlipay();
    }

    /**
     * 更新微信包信息
     */
    private void updatePackageInfo() {
        try {
            mAliPayPackageInfo = getContext().getPackageManager().getPackageInfo(ALIPAY_PACKAGENAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}