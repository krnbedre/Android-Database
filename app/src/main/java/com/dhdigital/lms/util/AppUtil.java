package com.dhdigital.lms.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhdigital.lms.R;
import com.dhdigital.lms.activities.LoginActivity;
import com.dhdigital.lms.glide.HeaderLoader;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.DeviceInfoUtils;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Kiran Bedre on 6/11/15.
 * DarkHorse BOA
 */
public class AppUtil {



    /**
     * Checks if the passed email id is valid according to standard.
     *
     * @param email
     * @return
     */
    public final static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     * Checks if the mobile has internet connectivity or not
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Shows a snack bar in the context of any view ie. passed
     *
     * @param view
     * @param message
     * @param color
     */
    public static void showSnackBar(View view, String message, int color) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        TextView txtv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER);

        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }

    /**
     * Shows a snack bar in the context of any view ie. passed
     *
     * @param view
     * @param message
     * @param color
     */
    public static void showSnackBaPersistant(View view, String message, int color) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        TextView txtv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER);

        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }

    public static void showShortToast(Context context, String message, int color) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(color);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }


    public static String convertBase64String(String toConvert) {
        return Base64.encodeToString(toConvert.getBytes(), Base64.NO_WRAP);
    }

    public static void hideKeybord(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void makeEditTextScrollableInsideScrollView(EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public static void scrollToFocusOnView(final HorizontalScrollView scrollView, final View view) {
        scrollView.post(new Runnable() {
            public void run() {
                int vLeft = view.getLeft();
                int vRight = view.getRight();
                int sWidth = scrollView.getWidth();
                scrollView.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
            }
        });
    }

    public static int getTopRelativeTo(View view, final View parentView) {
        int top = 0;
        try {
            while (view != parentView) {
                top += view.getTop();
                view = (View) view.getParent();
            }
        } catch (Exception ex) {
            top = view.getTop();
        }
        return top;
    }

    public static void scrollTo(final ScrollView scrollView, final int dx, final int dy) {
        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo(dx, dy);
            }
        }, 300);
    }





    public static List<Intent> prepareCameraIntent(Context context, Uri outputFileUri) {
        List<Intent> cameraIntents = new ArrayList<Intent>();
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        for (ResolveInfo res : context.getPackageManager().queryIntentActivities(captureIntent, 0)) {
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }
        return cameraIntents;
    }


    /**
     * @param filePath
     * @param width
     * @param height
     * @return
     */
    public final static Bitmap getBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;
        }

        return bitmap;
    }

    /**
     * @param pBitmap
     * @param angle
     * @return
     */
    public static Bitmap rotateImage(Bitmap pBitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(pBitmap, 0, 0, pBitmap.getWidth(), pBitmap.getHeight(), matrix, true);
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public final static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) return 1;
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public final static void compressBitmapOnFile(File file, Bitmap bitmap, int qality) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, qality, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isMediaDocument(uri)) {
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                final String docId = DocumentsContract.getDocumentId(uri);
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{docId.split(":")[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else {
                return getDataColumn(context, uri, null, null);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /*to download the document file
    * */
    public static void startDownloadFile(Context context, String url, String fileName, String module, String refNo) {
        try {
            String urlL = url + "?fileName=" + fileName + "&moduleName=" + module + "&referenceNo=" + refNo;//1447850860_vespa.png&moduleName=TRAVELDRAFT&referenceNo=259";
            Log.d("@Shiv", "downlaod Url: " + urlL);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(urlL);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(true);
            request.setTitle(fileName);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            Map<String, String> stringStringMap = HeaderManager.prepareMasterDataHeaders(context);
            for (Map.Entry entry : stringStringMap.entrySet()) {
                request.addRequestHeader(entry.getKey().toString(), entry.getValue().toString());
            }
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String mimeType = getMimeFromFileName(fileName);
            if (mimeType != null) request.setMimeType(mimeType);
            downloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeFromFileName(String fileName) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(fileName);
        ext = ext.toLowerCase();
        String type = map.getMimeTypeFromExtension(ext);
        return type;
    }

    public static File getApplicationRootDir(Context context) {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name) + File.separator);
        if (!root.exists()) root.mkdirs();
        return root;
    }



   /* public static void executeLogoutAPI(final BaseActivity activity) {
        Type type = new TypeToken<String>() {
        }.getType();
        RequestManager.addRequest(new GsonObjectRequest<String>(APIUrls.LOGOUT_URL, HeaderManager.prepareMasterDataHeaders(activity), null, type, new VolleyErrorListener(activity, activity, NetworkEvents.INVALIDATE_USER)) {
            @Override
            public void deliverResponse(String response, Map<String, String> responseHeaders) {
                activity.updateUi(true, NetworkEvents.INVALIDATE_USER, response);
            }
        }, AppConstants.REQUEST_TIMEOUT_AVG);
    }*/

    public static void onNewTravelLayoutClicked(final Context context, Activity callingActivity, final Class cla, View view) {
        AppUtil.sendBroadcastToFinishMiddleActivities(context);
        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(context, cla);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(AppConstants.NAVIGATION, AppConstants.NEW_TRAVEL_REQUEST);

                context.startActivity(intent);
            }
        }, 500);



    }



    public static void goToLoginScreen(Context context) {
        PreferenceUtil.clearCookies(context);
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static String getUniqueImageFilename(Context context) {
        String uniqueId = DeviceInfoUtils.getUniqueId(context);
        if (uniqueId != null && uniqueId.length() > 3) {
            return String.valueOf(uniqueId.substring(0, 3) + "_" + System.currentTimeMillis() + ".jpg");
        }
        return String.valueOf(System.currentTimeMillis() + ".jpg");
    }


    public static void sendBroadcastToFinishMiddleActivities(Context context) {
        Intent intent = new Intent(AppConstants.ACTION_DH_FINISH_SELF);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    public static String getFormattedCurrency(Double sum){
        if (sum != null) {
            DecimalFormat df = new DecimalFormat("###.##");
            return df.format(sum);
        } else {
            return "0.00";
        }
    }



    public static void loadThumbNailImage(Context context,String url,ImageView image){
        Glide.with(context)
                .load(HeaderLoader.getUrlWithHeaders(url, context))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .placeholder(R.drawable.user_icon_disp)
                .into(image);
    }


}
