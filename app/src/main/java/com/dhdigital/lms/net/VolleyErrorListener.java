package com.dhdigital.lms.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dhdigital.lms.modal.FailResponse;
import com.dhdigital.lms.util.AppConstants;
import com.google.gson.Gson;
import com.kelltontech.volley.ui.IScreen;

import java.io.UnsupportedEncodingException;


public class VolleyErrorListener implements Response.ErrorListener {
    private final String TAG = VolleyErrorListener.class.getSimpleName();
    private final int action;
    private final IScreen screen;
    private Context context;

    public VolleyErrorListener(final IScreen screen, Context context, final int action) {
        this.screen = screen;
        this.action = action;
        this.context = context;
    }

    private static boolean isServerProblem(VolleyError error) {
        return (error instanceof ServerError || error instanceof AuthFailureError || error instanceof ParseError || error instanceof TimeoutError);
    }

    private static boolean isNetworkProblem(VolleyError error) {
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;


        Log.e(TAG, "@UMESH onErrorResponse, networkResponse: " + ((networkResponse == null) ? null : networkResponse.statusCode + "Network Res: "+networkResponse.toString()) );
        Log.e(TAG, "@UMESH onErrorResponse, error: " + error.getStackTrace() +"\n" + "message : "+error.getLocalizedMessage() + "Message : "+error.getMessage());
        Log.e(TAG, "@UMESH onErrorResponse, error instance: " + error.getClass().getSimpleName());

        if (error instanceof NoConnectionError) {
            screen.updateUi(false, action, AppConstants.ERROR_MSG_N0_CONNECTION);
        } else if (error instanceof AuthFailureError) {
            screen.updateUi(false, action, AppConstants.ERROR_MSG_UNAUTHORIZED);
        } else if (error instanceof NetworkError) {
            screen.updateUi(false, action, AppConstants.ERROR_MSG_NETWORK);
        } else if (error instanceof ParseError) {
            screen.updateUi(false, action, AppConstants.ERROR_MSG_PARSE);
        } else if (error instanceof ServerError) {
            //screen.updateUi(false, action, AppConstants.ERROR_MSG_SERVER);
            try {
                FailResponse failResponse = new Gson().fromJson(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)), FailResponse.class);
                screen.updateUi(false, action, failResponse);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (error instanceof TimeoutError) {
            screen.updateUi(false, action, AppConstants.ERROR_MSG_TIMEOUT);
        } else {
            //screen.updateUi(false, action, AppConstants.ERROR_MSG_GENERIC);
            try {
                FailResponse failResponse = new Gson().fromJson(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)), FailResponse.class);
                screen.updateUi(false, action, failResponse);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


//    @Override
//    public void onErrorResponse(VolleyError error) {
//        Gson gson = new Gson();
//        NetworkResponse networkResponse = error.networkResponse;
//
//        Log.e(TAG, "@UMESH onErrorResponse, networkResponse: " + ((networkResponse == null) ? null : networkResponse.statusCode));
//        Log.e(TAG, "@UMESH onErrorResponse, error: " + error.getMessage());
//        Log.e(TAG, "@UMESH onErrorResponse, error instance: " + error.getClass().getSimpleName());
//        try {
//            if (networkResponse != null) {
//                switch (networkResponse.statusCode) {
//                    case HttpStatus.SC_BAD_REQUEST:
//                        FailResponse failResponse = gson.fromJson(new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers)), FailResponse.class);
//                        screen.updateUi(false, action, failResponse);
//                        break;
//                    case HttpStatus.SC_UNAUTHORIZED:
//                        screen.updateUi(false, action, AppConstants.ERROR_MSG_UNAUTHORIZED);
//                        break;
//                }
//            } else {
//                if (error instanceof NoConnectionError) {
//                    screen.updateUi(false, action, AppConstants.ERROR_MSG_N0_CONNECTION);
//                } else if (error instanceof AuthFailureError) {
//                    screen.updateUi(false, action, AppConstants.ERROR_MSG_AUTH_FAILURE);
//                } else if (error instanceof NetworkError) {
//                    Log.e("VollyError ", "NetworkError");
//                    screen.updateUi(false, action, AppConstants.ERROR_MSG_NETWORK);
//                } else if (error instanceof ParseError) {
//                    screen.updateUi(false, action, AppConstants.ERROR_MSG_PARSE);
//                } else if (error instanceof ServerError) {
//                    screen.updateUi(false, action, AppConstants.ERROR_MSG_SERVER);
//                } else if (error instanceof TimeoutError) {
//                    screen.updateUi(false, action, AppConstants.ERROR_MSG_TIMEOUT);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
