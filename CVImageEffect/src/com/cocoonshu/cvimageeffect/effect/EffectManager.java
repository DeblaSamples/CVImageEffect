package com.cocoonshu.cvimageeffect.effect;

import java.lang.ref.WeakReference;

import org.opencv.android.OpenCVLoader;

import android.content.Context;


public class EffectManager {

    private static final String                       TAG                           = "EffectManager";
    private static       EffectManager                sInstance                     = null;
    private              boolean                      mIsEffectEngineReady          = false;
    private              WeakReference<Context>       mRefContext                   = null;
    
    public static EffectManager createInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EffectManager(context);
        }
        return sInstance;
    }
    
    public static void destoryInstance() {
        if (sInstance != null) {
            sInstance.destory();
            sInstance = null;
        }
    }
    
    public static EffectManager getInstance() {
        return sInstance;
    }
    
    private EffectManager(Context context) {
        mRefContext = new WeakReference<Context>(context);
    }
    
    private void destory() {
        
    }
    
    public final boolean initializeEffectEngine() {
        return OpenCVLoader.initDebug(true);
    }

}
