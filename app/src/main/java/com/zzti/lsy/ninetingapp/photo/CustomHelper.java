package com.zzti.lsy.ninetingapp.photo;

import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import com.zzti.lsy.ninetingapp.R;

import java.io.File;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class CustomHelper {
    private View rootView;

    public static CustomHelper of(View rootView) {
        return new CustomHelper(rootView);
    }

    private CustomHelper(View rootView) {
        this.rootView = rootView;
    }


    public void onClick(int limit, boolean crop, View view, TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view.getId()) {
            case R.id.tv_photo:
                if (crop) {
                    takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromCapture(imageUri);
                }
                break;
            case R.id.tv_selectPhoto:
                if (limit > 1) {
                    if (crop) {
                        takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
                    } else {
                        takePhoto.onPickMultiple(limit);
                    }
                    return;
                }
                if (crop) {
                    takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                } else {
                    takePhoto.onPickFromGallery();
                }
                break;
            default:
                break;
        }
    }


    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     * 配置压缩
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(102400)
                .setMaxPixel(800)
                .enableReserveRaw(false)
                .create();
        takePhoto.onEnableCompress(config, false);
    }

    /**
     * 配置剪裁
     *
     * @return
     */
    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
//        builder.setOutputX(800).setOutputY(800);
        builder.setAspectX(800).setAspectY(800);
        builder.setWithOwnCrop(false);
        return builder.create();
    }


}
