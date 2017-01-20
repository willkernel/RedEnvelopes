package com.willkernel.app.redenvelopes.biz;


import android.content.Context;

import com.willkernel.app.redenvelopes.service.RedEnvelopesService;
import com.willkernel.app.redenvelopes.utils.Config;

/**
 * <p>Created 16/1/16 上午12:38.</p>
 * <p><a href="mailto:codeboy2013@gmail.com">Email:codeboy2013@gmail.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author LeonLee
 */
public abstract class BaseAccessbilityBiz implements AccessbilityBiz {

    private RedEnvelopesService service;

    @Override
    public void onCreate(RedEnvelopesService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public RedEnvelopesService getService() {
        return service;
    }
}