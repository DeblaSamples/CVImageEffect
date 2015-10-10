
package com.cocoonshu.cvimageeffect;

import com.cocoonshu.cvimageeffect.data.BlurProcesser;
import com.cocoonshu.cvimageeffect.data.DataManager;
import com.cocoonshu.cvimageeffect.data.TaskManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BlurActivity extends AbsFilterActivity {

    private TextView  mTxvTitleTip         = null;
    private ImageView mImgPreviewImage     = null;
    private SeekBar   mSkbBlurPrecent      = null;
    private float     mCurrentBlurPrecent  = 0f;
    private int       mBitmapWidth         = 0;
    private int       mBitmapHeight        = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        
        setupViews();
        setupListeners();
    }

    private void setupViews() {
        mTxvTitleTip     = (TextView) findViewById(R.id.TextViewTitleTip);
        mImgPreviewImage = (ImageView) findViewById(R.id.ImageViewPicture);
        mSkbBlurPrecent  = (SeekBar) findViewById(R.id.SeekBarBlurPercent);
        
        Bitmap bitmap = DataManager.getInstance().getOrignalBitmap();
        mBitmapWidth  = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mImgPreviewImage.setImageBitmap(DataManager.getInstance().getOrignalBitmap());
        mTxvTitleTip.setText(String.format("Image Size: %d x %d\nBlur Radius: %dpx", 
                mBitmapWidth, mBitmapHeight, mSkbBlurPrecent.getProgress()));
    }

    private void setupListeners() {
        mSkbBlurPrecent.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float blurPercent = (float)seekBar.getProgress() / (float)seekBar.getMax();
                if (blurPercent != mCurrentBlurPrecent) {
                    BlurProcesser processer = new BlurProcesser(DataManager.getInstance().getOrignalBitmap()) {
                        
                        private long mStartTime = SystemClock.elapsedRealtime();
                        
                        @Override
                        protected void onTaskFinished(Object result) {
                            super.onTaskFinished(result);
                            mImgPreviewImage.setImageBitmap((Bitmap)result);
                            mTxvTitleTip.setText(String.format("Image Size: %d x %d\nBlur Radius: %dpx\nDuration = %dms", 
                                    mBitmapWidth, mBitmapHeight, mSkbBlurPrecent.getProgress(),
                                    SystemClock.elapsedRealtime() - mStartTime));
                        }
                        
                    };
                    processer.setParameters(blurPercent);
                    processer.needCallbackInMainThread(true);
                    TaskManager.getInstance().submitProcesser(processer);
                    mTxvTitleTip.setText(String.format("Image Size: %d x %d\nBlur Radius: %dpx", 
                            mBitmapWidth, mBitmapHeight, mSkbBlurPrecent.getProgress()));
                }
            }
            
        });
    }

}
