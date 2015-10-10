package com.cocoonshu.cvimageeffect.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;


public class TaskManager {

    private static final int             LoaderThreadSize          = 4;
    private static       TaskManager     sInstance                 = null;
    private              ExecutorService mLoaderExecutorService    = null;
    private              ExecutorService mProcesserExecutorService = null;
    private              Handler         mMainThreadHandler        = null;
    
    public static TaskManager getInstance() {
        if (sInstance == null) {
            sInstance = new TaskManager();
        }
        return sInstance;
    }
 
    private TaskManager() {
        mMainThreadHandler        = new Handler(Looper.getMainLooper());
        mLoaderExecutorService    = Executors.newFixedThreadPool(LoaderThreadSize);
        mProcesserExecutorService = Executors.newSingleThreadExecutor();
    }
    
    public final void sumbitTask(Loader loader) {
        loader.setupMainThreadHandler(mMainThreadHandler);
        mLoaderExecutorService.submit(loader);
    }
    
    public final void submitProcesser(Loader loader) {
        loader.setupMainThreadHandler(mMainThreadHandler);
        mProcesserExecutorService.submit(loader);
    }
    
    public final void destory() {
        mLoaderExecutorService.shutdownNow();
    }
    
    public static abstract class Loader implements Runnable {

        private boolean mIsNeedCallbackInMainThread = false;
        private Handler mMainThreadHandler          = null;
        
        public void needCallbackInMainThread(boolean isNeeded) {
            mIsNeedCallbackInMainThread = isNeeded;
        }
        
        protected final void setupMainThreadHandler(Handler handler) {
            mMainThreadHandler = handler;
        }
        
        @Override
        public void run() {
            final Object result = doInBackground();
            
            if (mIsNeedCallbackInMainThread) {
                mMainThreadHandler.post(new Runnable() {
                    
                    @Override
                    public void run() {
                        onTaskFinished(result);
                    }
                    
                });
            } else {
                onTaskFinished(result);
            }
        }
        
        abstract protected Object doInBackground();
        
        abstract protected void onTaskFinished(Object result);
    }
}
