
package com.cocoonshu.cvimageeffect;

import com.cocoonshu.cvimageeffect.data.DataManager;
import com.cocoonshu.cvimageeffect.data.PictureLoader;
import com.cocoonshu.cvimageeffect.data.TaskManager;
import com.cocoonshu.cvimageeffect.effect.EffectManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

public class LauncherActivity extends Activity implements OnClickListener {

    private static Class<?>[] sFilterActivities = {
        BlurActivity.class
    };
    
    private static String[] sFilterAcitivityNames = {
        "Blur"
    };
    
    private static int[] sFiliterActivityIcons = {
        R.drawable.tbtn_blur_checked,
        R.drawable.tbtn_mosaic_checked,
        R.drawable.tbtn_filter_checked
    };
    
    private TextView  mTxvTitleTip         = null;
    private ImageView mImgPreviewImage     = null;
    private ViewGroup mLytFiltersContainer = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        
        setupViews();
        setupListeners();
        initializeFromIntent();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setupEffectManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EffectManager.destoryInstance();
        DataManager.getInstance().destory();
    }
    
    private void setupEffectManager() {
        EffectManager effectManager = EffectManager.createInstance(getApplicationContext());
        if (effectManager.initializeEffectEngine()) {
            mTxvTitleTip.setText("OpenCV loading successed");
        } else {
            mTxvTitleTip.setText("OpenCV loading failed");
        }
    }

    private void setupViews() {
        mTxvTitleTip         = (TextView) findViewById(R.id.TextViewTitleTip);
        mImgPreviewImage     = (ImageView) findViewById(R.id.ImageViewPreview);
        mLytFiltersContainer = (ViewGroup) findViewById(R.id.LayoutFiltersContainer);
        
        for (int i = 0; i < sFilterActivities.length; i++) {
            View      itemView = LayoutInflater.from(LauncherActivity.this).inflate(R.layout.layout_filter_item, null);
            TextView  itemName = (TextView) itemView.findViewById(R.id.TextViewFilterName);
            ImageView itemIcon = (ImageView) itemView.findViewById(R.id.ImageViewFilterIcon);
            itemName.setText(sFilterAcitivityNames[i]);
            itemIcon.setImageResource(sFiliterActivityIcons[i]);
            itemView.setOnClickListener(LauncherActivity.this);
            itemView.setTag(sFilterActivities[i]);
            mLytFiltersContainer.addView(itemView);
        }
    }

    private void setupListeners() {
        // TODO Auto-generated method stub
        
    }

    private void initializeFromIntent() {
        Intent intent    = getIntent();
        String action    = intent.getAction();
        
        if (Intent.ACTION_EDIT.equals(action)) {
            Uri userPhoto = intent.getData();
            PictureLoader loader = new PictureLoader(getApplicationContext(), userPhoto) {
                
                @Override
                protected void onTaskFinished(Object result) {
                    super.onTaskFinished(result);
                    mImgPreviewImage.setImageBitmap((Bitmap)result);
                }
                
            };
            loader.needCallbackInMainThread(true);
            TaskManager.getInstance().sumbitTask(loader);
        }
    }

    @Override
    public void onClick(View filterItemView) {
        Class<?> activityClass = (Class<?>) filterItemView.getTag();
        Intent intent = new Intent(LauncherActivity.this, activityClass);
        startActivity(intent);
    }

}
