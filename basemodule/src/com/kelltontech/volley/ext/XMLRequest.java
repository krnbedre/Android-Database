/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.kelltontech.volley.ext;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kelltontech.volley.parser.IParser;


import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

public abstract class XMLRequest<T> extends Request<T> {
    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/xml; charset=%s", PROTOCOL_CHARSET);

    private final String mRequestBody;

    /**
     * Response headers.
     */
    protected Map<String, String> mResponseHeaders;
    /**
     * Request headers.
     */
    protected Map<String, String> mRequestHeaders;
    private Priority mPriority;
    /**
     * The HTTP status code.
     */

    private IParser<T> mParser;

    public XMLRequest(String url, String xmlPayload, IParser<T> parser, ErrorListener errorListener) {
        this(url, Collections.<String, String>emptyMap(), url, errorListener);
        this.mParser = parser;
    }

    public XMLRequest(String url, Map<String, String> mRequestHeaders, String xmlPayload, ErrorListener errorListener) {
        super(xmlPayload == null ? Method.GET : Method.POST, url, errorListener);
        mRequestBody = xmlPayload;
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            this.mResponseHeaders = response.headers;
            String payload = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mParser.parse(payload), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }


    public void setPriority(Priority mPriority) {
        this.mPriority = mPriority;
    }

    @Override
    public Priority getPriority() {
        return this.mPriority;
    }

    public Map<String, String> getResponseHeader() {
        return this.mResponseHeaders;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return mRequestHeaders;
    }

}
