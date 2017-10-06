/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;

import java.io.File;

/**
 * @author Vijay.Kumar
 */
public class RequestManager {
    /**
     * TODO:
     * 1: Cache Images should be cleaned once application is uninstalled.
     * 2: Do, I have to start the queue on queue() and loader() functions
     * 3: DiskCache in RequestQueue Vs LRUCache in ImageLoader
     * 4: Request Priority order
     * 5: can we use only one requestQueue.
     */
    private static RequestManager instance;
    private static ImageLoader mImageLoader;
    private RequestQueue mDataRequestQueue;
    private RequestQueue mImageQueue;

    private Context mContext;
    private Config mConfig;


    private DefaultHttpClient mDefaultHttpClient;// = new DefaultHttpClient();;

    //	private static String mDefaultRequestTag;
    public static class Config {
        private String mImageCachePath;
        private int mDefaultDiskUsageBytes;
        private int mThreadPoolSize;

        public Config(final String imageCachePath, final int defaultDiskUsageBytes, final int threadPoolSize) {
            this.mDefaultDiskUsageBytes = defaultDiskUsageBytes;
            this.mImageCachePath = imageCachePath;
            this.mThreadPoolSize = threadPoolSize;

        }
    }


    private RequestManager(Context context, Config config) {
        this.mContext = context;
        this.mConfig = config;
    }

    //TODO: Initialize this on application onCreate()
    public static synchronized RequestManager initializeWith(Context context, Config config) {
        if (instance == null) {
            instance = new RequestManager(context, config);
        }
        return instance;
    }

    private synchronized RequestQueue getDataRequestQueue() {
        if (mDataRequestQueue == null) {
            Log.d("RequestManager", "@UMESH creating new httpClient...");
            Log.e("", "new request QUEUE");
//            mDefaultHttpClient = new DefaultHttpClient();
            mDefaultHttpClient = getThreadSafeClient();
            mDataRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(), new HttpClientStack(mDefaultHttpClient));
            mDataRequestQueue.start();
        }
        return mDataRequestQueue;
    }

    private DefaultHttpClient getThreadSafeClient() {
        BasicHttpParams params = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        return new DefaultHttpClient(cm, params);
    }

    private synchronized RequestQueue loader() {
        if (this.mConfig == null) {
            throw new IllegalStateException(RequestManager.Config.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        if (mImageQueue == null) {
            File rootCache = mContext.getExternalCacheDir();
            if (rootCache == null) {
                rootCache = mContext.getCacheDir();
            }

            File cacheDir = new File(rootCache, mConfig.mImageCachePath);
            cacheDir.mkdirs();

            HttpStack stack = new HurlStack();
            Network network = new BasicNetwork(stack);
            DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, mConfig.mDefaultDiskUsageBytes);
            mImageQueue = new RequestQueue(diskBasedCache, network, mConfig.mThreadPoolSize);
            mImageQueue.start();
        }
        return mImageQueue;
    }

    public static <T> void addRequest(Request<T> pRequest, int pRequestTimeout) {
        if (instance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        if (pRequest.getTag() == null) {
            new IllegalArgumentException("Request Object Tag is not specified.");
        }
        pRequest.setRetryPolicy(new DefaultRetryPolicy(
                pRequestTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = instance.getDataRequestQueue();

        queue.add(pRequest);
    }

    public DefaultHttpClient getDefaultHttpClient() {
        return mDefaultHttpClient;
    }

    /**
     * @param
     */
    public static <T> void getImage(String url, ImageListener listener) {
        if (instance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(instance.loader(), new DiskCache(instance.mContext));
        }
        mImageLoader.get(url, listener);
    }


    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param pRequestTag
     */
    public static void cancelPendingRequests(Object pRequestTag) {
        if (instance == null) {
            throw new IllegalStateException(RequestManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        if (instance.getDataRequestQueue() != null) {
            instance.getDataRequestQueue().cancelAll(pRequestTag);
        }
    }


    /**
     * Implementation of volley's {@link ImageCache} interface.
     *
     * @author sachin.gupta
     */
    private static class DiskCache implements ImageCache {

        private static DiskLruImageCache mDiskLruImageCache;

        public DiskCache(Context context) {
            String cacheName = context.getPackageCodePath();
            int cacheSize = 1024 * 1024 * 10;
            mDiskLruImageCache = new DiskLruImageCache(context, cacheName, cacheSize, CompressFormat.PNG, 100);
        }

        @Override
        public Bitmap getBitmap(String pImageUrl) {
            try {
                return mDiskLruImageCache.getBitmap(createKey(pImageUrl));
            } catch (NullPointerException e) {
                throw new IllegalStateException("Disk Cache Not initialized");
            }
        }

        @Override
        public void putBitmap(String pImageUrl, Bitmap pBitmap) {
            try {
                mDiskLruImageCache.put(createKey(pImageUrl), pBitmap);
            } catch (NullPointerException e) {
                throw new IllegalStateException("Disk Cache Not initialized");
            }
        }

        /**
         * Creates a unique cache key based on a url value
         *
         * @param pImageUrl url to be used in key creation
         * @return cache key value
         */
        private String createKey(String pImageUrl) {
            return String.valueOf(pImageUrl.hashCode());
        }
    }

}
