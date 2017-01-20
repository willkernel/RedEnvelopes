package com.willkernel.app.redenvelopes.biz;


import android.view.accessibility.AccessibilityEvent;

import com.willkernel.app.redenvelopes.controller.IStatusBarNotification;
import com.willkernel.app.redenvelopes.service.RedEnvelopesService;

/**
 * <p>Created 16/1/16 上午12:32.</p>
 * <p><a href="mailto:codeboy2013@gmail.com">Email:codeboy2013@gmail.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public interface AccessbilityBiz {
    String getTargetPackageName();
    void onCreate(RedEnvelopesService service);
    void onReceive(AccessibilityEvent event);
    void onStop();
    void onNotificationPosted(IStatusBarNotification service);
    boolean isEnable();
}