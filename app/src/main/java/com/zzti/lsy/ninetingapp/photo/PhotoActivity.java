package com.zzti.lsy.ninetingapp.photo;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.base.BaseActivity;
import com.zzti.lsy.ninetingapp.utils.ImageUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import butterknife.BindView;

/**
 * author：anxin on 2018/10/15 16:22
 */
public class PhotoActivity extends BaseActivity {
    @BindView(R.id.iamgeView)
    ImageView imageView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setTitle("大图");
        String url = UIUtils.getStr4Intent(this, "url");
        Glide.with(this).load(url).into(imageView);
    }
}
