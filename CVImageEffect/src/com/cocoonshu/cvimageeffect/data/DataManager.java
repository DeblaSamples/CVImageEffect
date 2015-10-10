package com.cocoonshu.cvimageeffect.data;

import android.graphics.Bitmap;
import android.net.Uri;

public class DataManager {

    private static DataManager sInstance          = null;
    private        Uri         mPhotoUri          = null;
    private        Bitmap      mOrignalBitmap     = null;
    private        Bitmap      mAffectEffctBitmap = null;
    
    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }
    
    public final Uri getPhotoUri() {
        return mPhotoUri;
    }
    
    public final void setPhotoUri(Uri photoUri) {
        this.mPhotoUri = photoUri;
    }

    public final Bitmap getOrignalBitmap() {
        return mOrignalBitmap;
    }
    
    public final void setOrignalBitmap(Bitmap bitmap) {
        mOrignalBitmap = bitmap;
    }
    
    public final Bitmap getAffectEffctBitmap() {
        return mAffectEffctBitmap;
    }
    
    public final void setAffectEffctBitmap(Bitmap bitmap) {
        mAffectEffctBitmap = bitmap;
    }
    
    public void destory() {
        mPhotoUri = null;
        if (mOrignalBitmap != null) {
            mOrignalBitmap.recycle();
            mOrignalBitmap = null;
        }
        if (mAffectEffctBitmap != null) {
            mAffectEffctBitmap.recycle();
            mAffectEffctBitmap = null;
        }
    }
}
