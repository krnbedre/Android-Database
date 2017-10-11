package com.dhdigital.lms.glide;

import android.content.Context;
import android.net.Uri;

import com.kelltontech.volley.Constants;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by admin on 10/10/17.
 */

public class CustomPicasso {

    private static Picasso sPicasso;

    private CustomPicasso() {
    }

    public static Picasso getImageLoader(final Context context) {
        if (sPicasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);

            sPicasso = builder.build();
        }
        return sPicasso;
    }

}
