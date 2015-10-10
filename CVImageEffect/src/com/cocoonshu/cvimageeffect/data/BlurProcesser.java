package com.cocoonshu.cvimageeffect.data;

import java.lang.ref.WeakReference;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.cocoonshu.cvimageeffect.data.TaskManager.Loader;

public class BlurProcesser extends Loader {
    
    private WeakReference<Bitmap> mRefOrignalBitmap  = null;
    private float                 mBlurRadiusPercent = 0f;
    
    public BlurProcesser(Bitmap orignalBitmap) {
        mRefOrignalBitmap = new WeakReference<Bitmap>(orignalBitmap);
    }

    public void setParameters(float blurPercent) {
        mBlurRadiusPercent = blurPercent;
    }
    
    @Override
    protected Object doInBackground() {
        if (mBlurRadiusPercent <= 0f) {
            return mRefOrignalBitmap.get();
        }
        
        Bitmap orignalBitmap   = mRefOrignalBitmap.get();
        Bitmap processedBitmap = DataManager.getInstance().getAffectEffctBitmap();
        if (orignalBitmap == null) {
            return null;
        }
        if (processedBitmap == null) {
            processedBitmap = Bitmap.createBitmap(orignalBitmap.getWidth(), orignalBitmap.getHeight(), Config.ARGB_8888);
        }
        
        int   bitmapWidth    = orignalBitmap.getWidth();
        int   bitmapHeight   = orignalBitmap.getHeight();
        float blurSize       = mBlurRadiusPercent * 100;
        Mat   orignalImage   = new Mat();
        Mat   processedImage = new Mat(bitmapWidth, bitmapHeight, CvType.CV_8UC4);
        
        Utils.bitmapToMat(orignalBitmap, orignalImage);
        Imgproc.blur(orignalImage, processedImage, new Size(blurSize, blurSize));
        Utils.matToBitmap(processedImage, processedBitmap);
        
        orignalImage.release();
        processedImage.release();
        
        DataManager.getInstance().setAffectEffctBitmap(processedBitmap);
        return processedBitmap;
    }

    @Override
    protected void onTaskFinished(Object result) {
        // Let subclass instance to implement this method
    }

}
