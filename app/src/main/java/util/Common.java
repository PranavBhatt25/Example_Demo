package util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Pranav on 25/06/16.
 */
public class Common {
    // OPEN A DIALER
    public static void openDialer(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static String ConvertDateDDMMYYYY(String mDate) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(mDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String ConvertDateDDMMMYYYYKKMM(String mDate) {
        String[] items1 = mDate.split("-");
        String date1 = items1[0];
        String month = items1[1];
        String year = items1[2];
        String dayNumberSuffix = getDayNumberSuffix(Integer.parseInt(month));

        String inputPattern = "yyyy-MM-dd kk:mm:ss";
        String outputPattern = " d'" + dayNumberSuffix + "' MMMM yyyy kk:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(mDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * <b>Description</b> - Connection time out for calling API
     *
     * @param request
     */


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void halfNavigationDrawerOfScreen(Context context, View view) {

        Resources resources = context.getResources();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, resources.getDisplayMetrics());
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (width);
        params.height = DrawerLayout.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static String hashKey(String strValue) {
        String HMACKey = "";

        try {
            if (strValue.length() > 100)
                strValue = strValue.substring(0, 100);

//            if (strValue != null && strValue.length() > 0 && strValue.charAt(strValue.length() - 1) == '|') {
//                strValue = strValue.substring(0, strValue.length() - 1);
//            }
            HMACKey = Common.hmacSha1(strValue, Constant.API_KEY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return HMACKey;
    }


    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        int MAX_IMAGE_WIDTH = 1024;
        int MAX_IMAGE_HEIGHT = 1024;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_WIDTH || rotatedHeight > MAX_IMAGE_HEIGHT) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_WIDTH);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_HEIGHT);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    public static ProgressDialog authProgressDialog;

    /**
     * @param context
     * @param view    Set awesome font
     */
    public static void setAwesomeFont(Context context, View view) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(font);
        } else if (view instanceof EditText) {
            ((EditText) view).setTypeface(font);
        }
    }

    /**
     * <b>Description</b> - Connection time out for calling API
     *
     * @param request
     */
    public static void setVolleyConnectionTimeout(MultipartRequestSort request) {
        int socketTimeout = 100000;//1 Meinute - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    public static void setVolleyConnectionTimeout(JsonObjectRequest request) {
        int socketTimeout = 100000;//1 Meinute - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    public static void setVolleyConnectionTimeout(JsonArrayRequest request) {
        int socketTimeout = 100000;//1 Meinute - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    public static void setVolleyConnectionTimeout(StringRequest request) {
        int socketTimeout = 100000;//1 Meinute - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    /**
     * <b>Description</> Dismiss Progress Dialog
     */
    public static void ProgressDialogDismiss() {
        authProgressDialog.dismiss();
    }

    /**
     * @param textview set underline to text within TextView
     */
    public static void setUnderLineToTextView(TextView textview) {
        textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * @param context
     * @param message
     * @return
     * @author Krunal
     * @description use to check internet newtwork connection if network
     * connection not available than alert for open network
     * settings
     */
    public static boolean isOnline(final Context context, boolean message) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
            if (netInfo != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }

            if (message) {
                //    Common.makeToastMessage(context, context.getResources().getString(R.string.msg_internet_not_available), Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <b>Description</> show progress dialog
     *
     * @param context
     * @param message
     */
    public static void ProgressDialogShow(Context context, String message) {
        if (authProgressDialog != null && authProgressDialog.isShowing()) {
            authProgressDialog.dismiss();
        }
        authProgressDialog = new ProgressDialog(context);
        authProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        authProgressDialog.setMessage(message);
        authProgressDialog.setCancelable(false);
        authProgressDialog.setIndeterminate(true);
        authProgressDialog.show();

    }

    /**
     * @param context
     * @param message
     * @return true if wifi connected or false
     */
    public boolean isWifiConnected(Context context, boolean message) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        } else {
            if (message) {
                // Common.makeToastMessage(context, context.getResources().getString(R.string.msg_internet_not_available), Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }

    /**
     * @return TimeStamp of current date
     * @Description getCurrentTimesamp method use to get current date UNIX
     * timestamp
     */
    public static String getCurrentTimesamp() {
        try {
            long timestamp = System.currentTimeMillis() / 1000L;
            return String.valueOf(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return TimeStamp of current date
     * @Description getCurrentTimesamp method use to get current date UNIX timestamp
     */
    public static long getCurrentTimesampLong() {
        try {
            long timestamp = System.currentTimeMillis() / 1000L;
            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String changeDateFormat(String inputDate, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        inputFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputDate);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param timeStamp
     * @param format
     * @return String (Date based on format)
     * @Description TimestempToDate method use to convert Unix TimeStamp to
     * specified date format from Constant.DATE_FORMAT class
     */
    public static final String TimestempToDate(String timeStamp, String format) {
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            TimeZone mTimeZone = TimeZone.getDefault();
            mSimpleDateFormat.setTimeZone(mTimeZone);
            return mSimpleDateFormat.format(new Date(Long.parseLong(timeStamp) * 1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    public static float dpToPixel(Activity activity, int dpi) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.density * dpi;

    }

    public static float pixelToDpi(Activity activity, int pixel) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return pixel / displayMetrics.density;

    }

   /* public static DisplayImageOptions getDisplayImageOptions(Context context, int defaultImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(defaultImg)
                .resetViewBeforeLoading(true).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true).build();

        return options;
    }*/

    // Decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    // Decodes image and scales it to reduce memory consumption
    public Bitmap decodeBitmapByteArray(byte[] data) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, o);// (new
            // FileInputStream(f),
            // null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeByteArray(data, 0, data.length, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <b>Description</b> - convert Base65Encode to String
     *
     * @param file
     * @return
     */
    public static String getBase64EnocdeString(File file) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];

            int n;
            while (-1 != (n = fis.read(bytes)))
                output.write(bytes, 0, n);

            byte[] audioBytes = output.toByteArray();

            return Base64.encodeToString(audioBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveFile(File file, Bitmap bitmap) {
        if (bitmap != null) {
            File parentFile = new File(file.getParent());
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }

            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * @param bitmap
     * @return return byte[] if there is no error then return null Convert
     * bitmap object to byte[]
     */
    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param context
     * @param assetsFile
     * @param destination
     * @param fileName    Copy assets file to storage directory
     */
    public void copyAssetsFileToFile(Context context, String assetsFile, String destination, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            File destFile = new File(destination);
            boolean isDirCreated = false;
            if (!(isDirCreated = destFile.exists())) {
                isDirCreated = destFile.mkdir();
            }

            if (isDirCreated) {
                in = assetManager.open(assetsFile);
                File outFile = new File(destination, fileName);
                if (!outFile.exists()) {
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                }
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + fileName, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
    }

    /**
     * @param in
     * @param out
     * @throws IOException
     */
    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * @param text
     * @param filename
     * @author Krunal
     * @description user to store string into text file
     */
    public static void saveTextFileToSD(String text, String path, String filename) {
        try {
            PrintWriter mPrintWriter = new PrintWriter(new FileWriter(new File(path, filename)));
            mPrintWriter.print(text);
            mPrintWriter.close();
            Log.i("SDCARD", "String Store In File");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readTextFileFromAssets(Context context, String fileName) {
        BufferedReader reader = null;
        StringBuffer sbText = new StringBuffer("");
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String mLine = reader.readLine();
            while (mLine != null) {
                //process line
                sbText.append(mLine);
                mLine = reader.readLine();
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return sbText.toString();
    }


    public static Toast makeToastMessage(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
        return toast;
    }

    public static StateListDrawable createButtonSelector(Context context, int defaultDrawable, int clickDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                context.getResources().getDrawable(clickDrawable));
        stateListDrawable.addState(new int[]{}, context.getResources().getDrawable(defaultDrawable));
        return stateListDrawable;
    }

  /*  *//**
     * @param mContext
     * @Description databaseBackup method use to get backup of database in
     *              sdcard
     *//*
    public static void databaseBackup(Context mContext) {
        try {
            File file = mContext.getDatabasePath(DBConstant.DATABASE_NAME);
            String appDir = mContext.getResources().getString(R.string.app_name);
            if (createFileDirectory(mContext, appDir)) {
                String fileName = TimestempToDate(getCurrentTimesamp(),
                        "ddmmyyyyhhmmss");
                Log.d("DB Backup File Name", fileName);
                FileInputStream fis = new FileInputStream(file);
                String outputDB = Environment.getExternalStorageDirectory() + "/appDir/" + fileName + ".db";
                OutputStream os = new FileOutputStream(outputDB);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                // Close the streams
                os.flush();
                os.close();
                fis.close();
                Toast.makeText(mContext, "Your database backup successfully with name " + fileName, Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    /**
     * @param context
     * @param fileName This method use to create file directory if file is created
     *                 then it return true, if not then return false.
     * @return Boolean
     */
    public static Boolean createFileDirectory(Context context, String fileName) {
        // String path = context.getFilesDir().toString();
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Catabase Path", path);
        File file = new File(path, fileName);

        if (!file.exists())
            return file.mkdir();
        else
            return true;
    }

    /**
     * <b>Description</b> - get back to devise default timezone
     *
     * @return
     */
    public static final TimeZone getDefaultTimeZone() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeZone();
    }

    /***
     * <b>Description</b> - use to convert date to timestamp
     *
     * @param date
     * @param format
     * @return
     */
    public static final long dateToTimestamp(String date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    format);

            /*TimeZone utcZone = TimeZone.getDefault();*/
            dateFormat.setTimeZone(getDefaultTimeZone());

            Date parsedDate = dateFormat.parse(date);

            Timestamp timestamp = new Timestamp(
                    parsedDate.getTime());
            return timestamp.getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param startDate Timestamp
     * @param endDate   Timestamp
     * @return duration difference between startdate to enddate in the format of
     * 2 minutes ago, 4 days ago, 3 months ago, 1 year ago
     */
    public static String getDateTimeDurationDiffInStyle(long startDate, long endDate) {
        try {
            long diff = endDate - startDate;

            long diffSeconds = (diff * 1000) / 1000 % 60;
            long diffMinutes = (diff * 1000) / (60 * 1000) % 60;
            long diffHours = (diff * 1000) / (60 * 60 * 1000);
            int diffInDays = (int) ((diff * 1000) / (1000 * 60 * 60 * 24));
            int diffMonths = getMonthDiff(Long.valueOf(startDate), Long.valueOf(endDate));
            int month = 0;
            if (diffMonths < 0)
                month = (diffMonths - 1) + 12;

            int diffYears = getYearDiff(startDate, endDate);

//            Log.d("diffSeconds diffMinutes diffHours diffMonths diffYears",
//                    diffSeconds + " " + diffMinutes + " " + diffHours + " "
//                            + diffInDays + " " + diffMonths + " " + diffYears);

            if (diffYears > 0 && diffMonths > 0)
                return diffYears + " year" + ((diffYears == 1) ? "" : "s") + " ago";
            else if (diffMonths > 0 || month > 0)//(diffYears == 1 && diffMonths < 0 && diffMonths > -11))
            {
                //diffMonths = (diffYears == 1 && diffMonths < 0) ? diffMonths + 12 : diffMonths;
                diffMonths = (month > 0) ? month : diffMonths;
                return diffMonths + " month" + ((diffMonths == 1) ? "" : "s") + " ago";
            } else if (diffInDays > 0)// || ((diffMonths + 12) == 1))
                return diffInDays + " day" + ((diffInDays == 1) ? "" : "s") + " ago";
            else if (diffHours > 0)
                return diffHours + " hour" + ((diffHours == 1) ? "" : "s") + " ago";
            else if (diffMinutes > 0)
                return diffMinutes + " minute" + ((diffMinutes == 1) ? "" : "s") + " ago";
            else if (diffSeconds > 0)
                return diffSeconds + " second" + ((diffSeconds == 1) ? "" : "s") + " ago";
            else
                return "now";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param startDate timestamp
     * @param endDate   timestamp
     * @return month difference between startDate to endDate
     */
    public static int getMonthDiff(long startDate, long endDate) {
        try {
            int startDateMonth = Integer.parseInt(Common.TimestampToDate(startDate, "MM"));
            int endDateMonth = Integer.parseInt(Common.TimestampToDate(endDate, "MM"));

            return endDateMonth - startDateMonth;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @param timeStamp
     * @param format
     * @return String (Date based on format)
     * @Description TimestempToDate method use to convert Unix TimeStamp to specified date format from
     * Constant.DATE_FORMAT class
     */
    public static final String TimestampToDate(long timeStamp, String format) {
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            TimeZone mTimeZone = TimeZone.getDefault();
            mSimpleDateFormat.setTimeZone(mTimeZone);
            return mSimpleDateFormat.format(new Date(timeStamp * 1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param startDate timestamp
     * @param endDate   timestamp
     * @return year difference between startDate to endDate
     */
    public static int getYearDiff(long startDate, long endDate) {
        try {
            int startDateYear = Integer.parseInt(Common.TimestampToDate(startDate, "yyyy"));
            int endDateYear = Integer.parseInt(Common.TimestampToDate(endDate, "yyyy"));

            return endDateYear - startDateYear;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //    celsius, fahrenheit
    public static int convertCelsiusToFahrenheit(int celsius) {
        return (celsius * 9 / 5) + 32;
    }

    public static int convertFahrenheitToCelsius(int fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    /**
     * @return string
     * <p/>
     * Rinkesh
     * @Description getting the device id
     */

    public static String getDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return deviceId;
    }


    /**
     * @param text
     * @param start
     * @param end
     * @return string
     * @Description superscript text
     */
    public static SpannableStringBuilder setSpannableSuperscript(String text, int start, int end) {
        SpannableStringBuilder cs = new SpannableStringBuilder(text);
        cs.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        cs.setSpan(new RelativeSizeSpan(0.30f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return cs;
    }

    public static SpannableStringBuilder setSpannableSuperscriptDate(String text, int start, int end) {
        SpannableStringBuilder cs = new SpannableStringBuilder(text);
        cs.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        cs.setSpan(new RelativeSizeSpan(0.40f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return cs;
    }


    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        // Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        // toast.setGravity(Gravity.CENTER, 0, 0);

    }


    /**
     * <b>Description</b> - hide soft keyboard
     *
     * @param context
     * @param view
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }


    /**
     * @param monthNumber Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
     * @return
     */
    public static int getDaysInMonthInPresentYear(int monthNumber) {
        int days = 0;
        if (monthNumber >= 0 && monthNumber < 12) {
            try {
                Calendar calendar = Calendar.getInstance();
                int date = 1;
                int year = calendar.get(Calendar.YEAR);
                calendar.set(year, monthNumber, date);
                days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        }
        return days;
    }

    public static void displayAlert(Context context, String respMsg) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                }
            });
            builder.setTitle("" + "Job Sheet");
            builder.setMessage(respMsg);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * ((int) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


    /////// SECURITY FUNCTION
    // HMAC SHA-1 FUNCTION
    public static String hmacSha1(String value, String key) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, InvalidKeyException {
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    public final static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static DisplayImageOptions getDisplayImageOptions(Context context, int defaultImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(defaultImg)
                .resetViewBeforeLoading(true).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true).build();

        return options;
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(20);
        toast.show();
    }

    /**
     * <b>Description</>  display image in rounded shape
     *
     * @param context
     * @param defaultImg - pass id for imagenview
     * @param radius     - pass radius
     * @return - return DisplayImageOptions object
     */
    public static DisplayImageOptions getRoundedCornerDisplayImageOptions(Context context, int defaultImg, int radius) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(defaultImg)
                .resetViewBeforeLoading(true).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg)
                .displayer(new RoundedBitmapDisplayer(radius))
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        return options;
    }

    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString();
        } else {
            return null;
        }
    }

    public static String getAppDir() {
        String appDir = Common.getSDPath();
        appDir += "/" + "BankSoftware";
        File file = new File(appDir);
        if (!file.exists()) {
            file.mkdir();
        }
        appDir += "/" + "videocompress";
        file = new File(appDir);
        if (!file.exists()) {
            file.mkdir();
        }
        return appDir;
    }
}
