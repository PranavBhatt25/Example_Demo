package util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import banksoftware.com.banksoftware.R;


public class ApplicationClass extends Application {
    public static final String TAG = ApplicationClass.class
            .getSimpleName();
    private static DisplayImageOptions displayImageOptions;
    private static ImageLoaderConfiguration config;
    private static ApplicationClass sInstance;
    public Handler mHandler;
    private Activity currentClass = null;
    private RequestQueue mRequestQueue;

    /**
     * Intitilize Image Loader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        if (config == null) {
            config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(100 * 1024 * 1024) // 50 Mb

                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs() // Remove for release app
                    .build();
        }
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    /**
     * Using singleton pattern to get product detail pattern
     *
     * @param context
     * @return options to display product image
     */

    public static DisplayImageOptions getProductImageDisplayOption(Context context) {

        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            return displayImageOptions;
        } else {
            return displayImageOptions;
        }
    }

    public static synchronized ApplicationClass getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);

        sInstance = this;
        initImageLoader(getApplicationContext());

        Log.d(TAG, "Setting up StrictMode policy checking.");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        System.gc();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
