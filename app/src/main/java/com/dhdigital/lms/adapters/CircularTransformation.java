package com.dhdigital.lms.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by admin on 10/10/17.
 */

public class CircularTransformation extends BitmapTransformation {

    public CircularTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(null, toTransform);
        circularBitmapDrawable.setCircular(true);
        Bitmap bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        circularBitmapDrawable.setBounds(0, 0, outWidth, outHeight);
        circularBitmapDrawable.draw(canvas);
        return bitmap;
    }

    @Override
    public String getId() {
        // Return some id that uniquely identifies your transformation.
        return "CircularTransformation";
    }

}