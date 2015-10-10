package com.cocoonshu.cvimageeffect.data;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.cocoonshu.cvimageeffect.data.TaskManager.Loader;

public class PictureLoader extends Loader {

    private WeakReference<Context> mRefContext = null;
    private Uri                    mPictureUri = null;
    
    public PictureLoader(Context context, Uri pictureUri) {
        mRefContext = new WeakReference<Context>(context);
        mPictureUri = pictureUri;
    }
    
    @Override
    protected Object doInBackground() {
        Bitmap          bitmap   = null;
        Context         context  = mRefContext.get();
        ContentResolver resolver = context != null ? context.getContentResolver() : null;
        if (resolver != null) {
            ParcelFileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = resolver.openFileDescriptor(mPictureUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fileDescriptor = null;
            }
            
            try {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, null);
            } catch (OutOfMemoryError error) {
                bitmap = null;
            }
        }
        return bitmap;
    }

    @Override
    protected void onTaskFinished(Object result) {
        DataManager.getInstance().setOrignalBitmap((Bitmap)result);
    }

}
