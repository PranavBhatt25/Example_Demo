package banksoftware.com.banksoftware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import model.LoginDetailsClass;
import util.AppPreference;
import util.ApplicationClass;
import util.Common;
import util.ConnectionDetector;
import util.Constant;
import util.ServiceApi;

import static util.Common.showToast;


/**
 * Created by Pranav & Sarfaraj on 25/06/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    public static Context context;
    private EditText et_login_username = null, et_login_password = null;
    private Button btn_sign_in = null, btn_sign_up = null;
    private TextView txt_forgot_password = null, txt_dont_have_account = null;
    private String strEmailId = "", deviceId = "", strUserName = "", strPassword = "";
    ConnectionDetector mConnectionDetector;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;
    TextView tv_app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slid_in_right, R.anim.slid_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        mConnectionDetector = new ConnectionDetector(context);
        setContentView(R.layout.activity_login);

        getDeviceId();
        init();
        onClickListener();
    }


    private void onClickListener() {
        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);
        txt_dont_have_account.setOnClickListener(this);
    }

    private void init() {
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

            et_login_username = (EditText) findViewById(R.id.et_login_username);
            et_login_password = (EditText) findViewById(R.id.et_login_password);
            btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
            btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
            txt_forgot_password = (TextView) findViewById(R.id.txt_forgot_password);
            txt_dont_have_account = (TextView) findViewById(R.id.txt_dont_have_account);
            tv_app_name = (TextView) findViewById(R.id.tv_app_name);

            tv_app_name.setTypeface(typeFaceRegular);
            et_login_username.setTypeface(typeFaceRegular);
            et_login_password.setTypeface(typeFaceRegular);
            btn_sign_in.setTypeface(typeFaceRegular);
            btn_sign_up.setTypeface(typeFaceRegular);
            txt_forgot_password.setTypeface(typeFaceRegular);
            txt_dont_have_account.setTypeface(typeFaceRegular);

        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    /*
      Will receive the activity result and check which request we are responding to

     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

    }

    @Override
    public void onClick(View view) {
        if (view.equals(btn_sign_in)) {
            strUserName = et_login_username.getText().toString().trim();
            strPassword = et_login_password.getText().toString().trim();

            if (strUserName != null && strUserName.equals("")) {
                showToast(context, getString(R.string.please_enter_username));
                return;
            }
            if (strPassword != null && strPassword.equals("")) {
                showToast(context, getString(R.string.please_enter_pw));
                return;
            }

            if (mConnectionDetector.isConnectingToInternet()) {
                callLogInAPI();
            } else {
                showToast(context, getString(R.string.please_check_internet));
            }
        }
//        else if (view.equals(btn_sign_up)) {
//            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
//            startActivity(i);
//        } else if (view.equals(txt_forgot_password)) {
//            ViewDialog alert = new ViewDialog();
//            alert.showDialog(LoginActivity.this);
//        } else if (view.equals(txt_dont_have_account)) {
//
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
    }

//    public class ViewDialog {
//        TextView txt_forgot_password = null;
//        EditText et_email = null;
//        Button btn_send_email = null;
//
//        public void showDialog(Activity activity) {
//            final Dialog dialog = new Dialog(activity);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.dialog_forget_password);
//
//            Window window = dialog.getWindow();
//            window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//            window.setGravity(Gravity.CENTER);
//
//            txt_forgot_password = (TextView) dialog.findViewById(R.id.txt_forgot_password);
//            et_email = (EditText) dialog.findViewById(R.id.et_email);
//            btn_send_email = (Button) dialog.findViewById(R.id.btn_send_email);
//
//            ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
//            img_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            btn_send_email.setOnClickListener(new View.OnClickListener() {
//                                                  @Override
//                                                  public void onClick(View v) {
//                                                      strEmailId = et_email.getText().toString().trim();
//                                                      if (strEmailId.trim().equals("") || strEmailId.length() == 0 || TextUtils.isEmpty(strEmailId)) {
//                                                          showToast(context, getString(R.string.please_enter_email));
//                                                      } else if (strEmailId.length() > 0 && !Common.isValidEmail(strEmailId)) {
//
//                                                          showToast(context, getString(R.string.please_enter_valid_email));
//                                                      } else {
//
//                                                          if (mConnectionDetector.isConnectingToInternet()) {
//                                                              AppPreference.setStringPref(context, AppPreference.PREF_SENT_EMAIL, AppPreference.PREF_KEY.NEW_EMAIL, strEmailId);
//                                                              if (mConnectionDetector.isConnectingToInternet()) {
//                                                                  callForgotEmailTokanAPI();
//                                                              } else {
//                                                                  showToast(context, getString(R.string.please_check_internet));
//                                                              }
//                                                          } else {
//                                                              showToast(context, getString(R.string.please_check_internet));
//                                                          }
//                                                          dialog.dismiss();
//                                                      }
//                                                  }
//                                              }
//
//            );
//
//
//            dialog.show();
//
//        }
//
//    }

//    private void callForgotEmailTokanAPI() {
//        Common.ProgressDialogShow(LoginActivity.this, Constant.MESSAGE.PROGRESS_PLEASE_WAIT_MSG);
//        Map<String, String> params = new HashMap<String, String>();
//
//
//        params.put(ServiceApi.WEB_SERVICE_KEY.EMAIL, strEmailId);
//
//        String strValue = "";
//        SortedSet<String> keys = new TreeSet<String>(params.keySet());
//        for (String key : keys) {
//            String value = params.get(key);
//            strValue = strValue + value + "|";
//        }
//
//        strValue = Common.hashKey(strValue);
//        params.put(ServiceApi.WEB_SERVICE_KEY.API_KEY, Constant.API_KEY);
//        params.put(ServiceApi.WEB_SERVICE_KEY.API_TOKEN, strValue);
//
//
//        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.FORGOT_PW_URL, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            parseJsonLoginTokenDetail(response);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Common.ProgressDialogDismiss();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error.networkResponse != null) {
//                            int statusCode = error.networkResponse.statusCode;
//                            NetworkResponse response = error.networkResponse;
//
//                            Log.d("testerror", "" + statusCode + " " + response.data);
//                            Toast.makeText(LoginActivity.this, Constant.MESSAGE.CONNECTION_TIMEOUT, Toast.LENGTH_LONG).show();
//                        }
//                        Common.ProgressDialogDismiss();
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("User-agent", "Mozilla/5.0 (TV; rv:44.0) Gecko/44.0 Firefox/44.0");
//                return headers;
//            }
//        };
//
//
//        Common.setVolleyConnectionTimeout(request);
//        ApplicationClass.getInstance().getRequestQueue().add(request);
//    }
//
//
//    /**
//     * <b>Description</b> - Get back response for calling  parseJsonLoginDetail API
//     *
//     * @param jsonObject - Pass API response
//     */
//    private void parseJsonLoginTokenDetail(JSONObject jsonObject) {
//        try {
//
//            Common.ProgressDialogDismiss();
//            Log.i("get response", "get response" + jsonObject);
//            if (jsonObject.toString().contains(Constant.JSON_KEY.MSG)) {
//                String message = jsonObject.getString(Constant.JSON_KEY.MSG);
//                String status = jsonObject.getString(Constant.JSON_KEY.CODE);
//
//                if (status.equals("0")) {
//                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
//                    i.setAction("Forgotemail");
//                    startActivity(i);
//                }
//            }
//        } catch (Exception e) {
//            Common.ProgressDialogDismiss();
//            e.printStackTrace();
//        }
//    }


    private void callLogInAPI() {

        Common.ProgressDialogShow(LoginActivity.this, Constant.MESSAGE.PROGRESS_PLEASE_WAIT_MSG);
        Map<String, String> params = new HashMap<String, String>();

        String refreshedToken = AppPreference.getStringPref(this, AppPreference.PREF_NAME, AppPreference.PREF_KEY.REGID);

        params.put(ServiceApi.WEB_SERVICE_KEY.EMAIL, strUserName);
        params.put(ServiceApi.WEB_SERVICE_KEY.PASSWORD, strPassword);

        String strValue = "";
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            String value = params.get(key);
            strValue = strValue + value + "|";
        }

        strValue = Common.hashKey(strValue);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_KEY, Constant.API_KEY);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_TOKEN, strValue);
        params.put(ServiceApi.WEB_SERVICE_KEY.GCM_ID, refreshedToken);


        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.LOGIN_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJsonLoginDetail(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Common.ProgressDialogDismiss();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            NetworkResponse response = error.networkResponse;

                            Log.d("testerror", "" + statusCode + " " + response.data);
                            Toast.makeText(LoginActivity.this, Constant.MESSAGE.CONNECTION_TIMEOUT, Toast.LENGTH_LONG).show();
                        }
                        Common.ProgressDialogDismiss();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", "Mozilla/5.0 (TV; rv:44.0) Gecko/44.0 Firefox/44.0");
                return headers;
            }
        };


        Common.setVolleyConnectionTimeout(request);
        ApplicationClass.getInstance().getRequestQueue().add(request);
    }


    /**
     * <b>Description</b> - Get back response for calling  parseJsonLoginDetail API
     *
     * @param jsonObject - Pass API response
     */
    private void parseJsonLoginDetail(JSONObject jsonObject) {
        try {

            Common.ProgressDialogDismiss();
            Log.i("get response", "get response" + jsonObject);
            if (jsonObject.toString().contains(Constant.JSON_KEY.MSG)) {
                String message = jsonObject.getString(Constant.JSON_KEY.MSG);
                String status = jsonObject.getString(Constant.JSON_KEY.CODE);

                if (status.equals("0")) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    if (jsonObject.toString().contains(Constant.JSON_KEY.RESPONSE)) {
                        if (jsonObject.toString().contains(Constant.JSON_KEY.RESPONSE)) {
                            JSONObject response = jsonObject.getJSONObject(Constant.JSON_KEY.RESPONSE);

                            String userId = response.getString(Constant.JSON_KEY.USER_ID);
                            String userName = response.getString(Constant.JSON_KEY.USER_NAME);

                            LoginDetailsClass loginDetailsClass = new LoginDetailsClass();
                            loginDetailsClass.setUserId(userId);
                            loginDetailsClass.setUserName(userName);

                            AppPreference.setStringPref(context, AppPreference.PREF_USERID, AppPreference.PREF_KEY.USERID, userId);
                            AppPreference.setStringPref(context, AppPreference.PREF_USERNAME, AppPreference.PREF_KEY.USERNAME, userName);
                            AppPreference.setStringPref(context, AppPreference.PREF_IS_LOGIN, AppPreference.PREF_KEY.LOGIN_STATUS, "1");

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, NavigationDrawer.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        } catch (Exception e) {
            Common.ProgressDialogDismiss();
            e.printStackTrace();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                View vieww = getCurrentFocus();
                if (vieww != null) {
                    InputMethodManager immmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    immmm.hideSoftInputFromWindow(vieww.getWindowToken(), 0);
                }
            }
        }
        return ret;
    }

    private String getDeviceId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();
        Log.i("deviceid", "deviceid : " + deviceId);
        return deviceId;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}

