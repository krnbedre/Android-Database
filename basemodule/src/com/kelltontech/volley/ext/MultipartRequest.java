package com.kelltontech.volley.ext;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Umesh Agrawal on 18/1/16.
 * Kellton Tech
 */
public abstract class MultipartRequest<T> extends Request<T> {
    private static final String TAG = MultipartRequest.class.getSimpleName();
    protected NetworkResponse mResponse;
    private HttpEntity mHttpEntity;
    private Priority mPriority;
    private final Gson mGson;
    private Type type;

    private static final String FILE_PART_NAME = "file";

    public MultipartRequest(String url, String filePath, Map<String, String> stringPartMap, Type type, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        File file = new File(filePath);
        mHttpEntity = buildMultipartEntity(file, stringPartMap);
        mGson = new Gson();
        this.type = type;
    }

    public MultipartRequest(String url, File file, Map<String, String> stringPartMap, Type type, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mHttpEntity = buildMultipartEntity(file, stringPartMap);
        mGson = new Gson();
        this.type = type;
    }

    private HttpEntity buildMultipartEntity(File file, Map<String, String> stringPartMap) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody(FILE_PART_NAME, file);
        if (stringPartMap != null && stringPartMap.size() > 0) {
            for (Map.Entry entry : stringPartMap.entrySet()) {
                builder.addTextBody(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        return builder.build();
    }

    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    @Override
    public Priority getPriority() {
        return this.mPriority;
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            this.mResponse = response;
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "Response: " + json.toString());
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

    public abstract void deliverResponse(T response, Map<String, String> responseHeaders);

    @Override
    protected void deliverResponse(T response) {
        deliverResponse(response, mResponse.headers);
    }
}
