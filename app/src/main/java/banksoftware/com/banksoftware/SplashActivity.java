package banksoftware.com.banksoftware;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import util.AppPreference;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Pranav on 07/02/17.
 */
public class SplashActivity extends Activity {


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private static Activity activity;
    private static Context context;
    Typeface typeface;
    TextView tv_bottom_splash;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;
    TextView tv_app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //overridePendingTransition(R.anim.slid_in_right, R.anim.slid_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_splash);
        context = activity = this;

        try {
            typeFaceBold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
            typeFaceBoldItalic = Typeface.createFromAsset(getAssets(), "opensans_bolditalic.ttf");
            typeFaceExtraBold = Typeface.createFromAsset(getAssets(), "opensans_extrabold.ttf");
            typeFaceExtraBoldItalic = Typeface.createFromAsset(getAssets(), "opensans_extrabolditalic.ttf");
            typeFaceItalic = Typeface.createFromAsset(getAssets(), "opensans_italic.ttf");
            typeFaceLight = Typeface.createFromAsset(getAssets(), "opensans_light.ttf");
            typeFaceLightItalic = Typeface.createFromAsset(getAssets(), "opensans_lightitalic.ttf");
            typeFaceRegular = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
            typeSemibold = Typeface.createFromAsset(getAssets(), "opensans_semibold.ttf");
            typeSemiboldItalic = Typeface.createFromAsset(getAssets(), "opensans_semibolditalic.ttf");
        } catch (Exception e) {
            e.getStackTrace();
        }

        tv_app_name = (TextView) findViewById(R.id.tv_app_name);
        tv_app_name.setTypeface(typeFaceRegular);
        tv_bottom_splash = (TextView) findViewById(R.id.tv_bottom_splash);
        tv_bottom_splash.setTypeface(typeFaceRegular);

        if (!checkPermission()) {
            openActivity();
        } else {
            if (checkPermission()) {
               // requestPermissionAndContinue();
                openActivity();
            } else {
                openActivity();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, READ_CONTACTS) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_CONTACTS)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(getString(R.string.permission_necessary));
                alertBuilder.setMessage(R.string.storage_permission_is_encessary_to_wrote_event);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{ACCESS_FINE_LOCATION, CAMERA,
                                READ_CONTACTS, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
                                , WRITE_CONTACTS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA,
                        READ_CONTACTS, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
                        , WRITE_CONTACTS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            openActivity();
        }
    }


    private void openActivity() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.banksoftware", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String LoginStatus = AppPreference.getStringPref(context, AppPreference.PREF_IS_LOGIN, AppPreference.PREF_KEY.LOGIN_STATUS);
                if (LoginStatus.equals("") || LoginStatus.equals("0")) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, NavigationDrawer.class);
                    startActivity(i);
                    finish();
                }
            }

        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 2; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    openActivity();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;
    }

}

