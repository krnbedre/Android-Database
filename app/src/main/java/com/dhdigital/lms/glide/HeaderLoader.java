package com.dhdigital.lms.glide;

import android.content.Context;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.util.PreferenceUtil;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 11/04/17.
 */

public class HeaderLoader extends BaseGlideUrlLoader<String> {

    private static final String AUTHORIZATION = "QWERTYUIOPASDFGHJKL";
    private static final Headers REQUEST_HEADERS = new LazyHeaders.Builder()
            .addHeader("Authorization", AUTHORIZATION)
            .build();

    HeaderLoader(Context context) {
        super(context);
    }

    public static GlideUrl getUrlWithHeaders(String url,Context context){

        Map<String, String> toReturn = new HashMap<String, String>();
        toReturn = PreferenceUtil.getCookie(context);

        LazyHeaders.Builder  builder= new LazyHeaders.Builder();

        for (String key : toReturn.keySet()) {

            builder.addHeader(key,toReturn.get(key));
            // ...
        }

        return new GlideUrl(url, builder
                .build());


    }

    @Override
    protected String getUrl(String model, int width, int height) {
        return model;
    }


}
