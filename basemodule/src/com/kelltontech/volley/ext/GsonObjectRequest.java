/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.ext;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vijay.Kumar
 */
public abstract class GsonObjectRequest<T> extends JsonRequest<T> {
    private final Gson mGson;
    private Type type;
    private static final String TAG = GsonObjectRequest.class.getSimpleName();

    public GsonObjectRequest(String url, String jsonPayload, Type type, ErrorListener errorListener) {
        this(url, new HashMap<String, String>(), jsonPayload, type, errorListener);

    }

    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Type type, ErrorListener errorListener) {
        this(url, mRequestHeaders, jsonPayload, type, errorListener, new Gson());
    }

    public GsonObjectRequest(String url, String jsonPayload, Class clazz, Type type, ErrorListener errorListener, Gson gson) {
        this(url, null, jsonPayload, type, errorListener, gson);

    }

    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Type type, ErrorListener errorListener, Gson gson) {
        super(url, mRequestHeaders, jsonPayload, errorListener);
        this.type = type;
        mGson = gson;
    }

    public GsonObjectRequest(int method, String url, Map<String, String> mRequestHeaders, String jsonPayload, ErrorListener errorListener, Gson gson) {
        super(method, url, mRequestHeaders, jsonPayload, errorListener);
        this.type = type;
        mGson = gson;
    }

    public abstract void deliverResponse(T response, Map<String, String> responseHeaders);

    @Override
    protected void deliverResponse(T response) {
        deliverResponse(response, mResponse.headers);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            this.mResponse = response;
                String json = null;
                json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "Response: " + json.toString());
            if (json != null && json.isEmpty()) {
                return (Response<T>) Response.success("SUCCESS", HttpHeaderParser.parseCacheHeaders(response));
            }
            if (json != null && json.contains("login.js")) {
                return (Response<T>) Response.success("SUCCESS", HttpHeaderParser.parseCacheHeaders(response));
            }
            return (Response<T>) Response.success(mGson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "Response: " + new String(response.data));
//            if(context!=null){
//                StringUtils.writeToFile(context,new String(response.data));
//            }
            return Response.error(new ParseError(e));
        }
    }

//    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload,  Class clazz, ErrorListener errorListener, Context context) {
//        this(url, mRequestHeaders, jsonPayload, clazz, errorListener, new Gson());
//        this.context=context;
//    }
//    private Context context=null;
}

