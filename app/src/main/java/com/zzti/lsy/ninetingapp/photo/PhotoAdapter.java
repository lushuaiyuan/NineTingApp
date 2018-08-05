package com.zzti.lsy.ninetingapp.photo;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.utils.MyTransformation;
import com.zzti.lsy.ninetingapp.utils.StringUtil;

import java.util.List;

public class PhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PhotoAdapter(List<String> pics) {
        super(R.layout.item_imageview, pics);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (StringUtil.isNullOrEmpty(item)) {
            ((ImageView) helper.getView(R.id.imageView)).setImageResource(R.mipmap.photo);
        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(App.get())
                    .load(item).apply(requestOptions)
                    .into((ImageView) helper.getView(R.id.imageView));
        }
    }
}
