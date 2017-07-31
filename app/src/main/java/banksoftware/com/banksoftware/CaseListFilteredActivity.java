package banksoftware.com.banksoftware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import adapter.CasesListAdapter;
import adapter.CasesListFilterAdapter;
import interfaces.OnLoadMoreListener;
import model.CasesListClass;
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
public class CaseListFilteredActivity extends Activity implements View.OnClickListener {

    Context context;
    Activity mActivity;
    ConnectionDetector mConnectionDetector;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    private ImageView iv_toolbar_left = null, iv_toolbar_right = null;
    private TextView toolbar_title = null;
    private RelativeLayout rr_toolbar_left = null, rr_toolbar_right = null;


    RecyclerView rvNewsList;
    int page = 1;
    boolean isLoadMore = false;
    ArrayList<CasesListClass> CasesArrayList = new ArrayList<>();
    CasesListFilterAdapter casesListFilterAdapter;
    PullRefreshLayout pullRefreshLayout, pullRefreshLayoutAnother;
    TextView tv_record_found = null, tv_status = null;
    LinearLayout ll_main_fragment;
    public static boolean isVisitedDetailsFromFilter = false;
    String mStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slid_in_right, R.anim.slid_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        mActivity = this;
        mConnectionDetector = new ConnectionDetector(context);
        setContentView(R.layout.activity_filtered_cases_list);

        if (getIntent().getExtras() != null) {
            mStatus = getIntent().getStringExtra("STATUS");
        }

        init();
        onClickListener();
    }


    private void onClickListener() {
        //   btn_sign_in.setOnClickListener(this);
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

            iv_toolbar_left = (ImageView) findViewById(R.id.iv_toolbar_left);
            iv_toolbar_left.setImageResource(R.mipmap.ic_back);
            iv_toolbar_left.setVisibility(View.VISIBLE);
            iv_toolbar_right = (ImageView) findViewById(R.id.iv_toolbar_right);

            isVisitedDetailsFromFilter = false;

            toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setText("Filtered Case List");
            rr_toolbar_left = (RelativeLayout) findViewById(R.id.rr_toolbar_left);
            rr_toolbar_right = (RelativeLayout) findViewById(R.id.rr_toolbar_right);
            rr_toolbar_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
                    finish();
                }
            });


            ll_main_fragment = (LinearLayout) findViewById(R.id.ll_main_fragment);
            rvNewsList = (RecyclerView) findViewById(R.id.rvNewsList);
            rvNewsList.setLayoutManager(new LinearLayoutManager(context));
            tv_record_found = (TextView) findViewById(R.id.tv_record_found);
            tv_record_found.setVisibility(View.GONE);
            tv_record_found.setTypeface(typeFaceRegular);

            tv_status = (TextView) findViewById(R.id.tv_status);
            tv_status.setTypeface(typeFaceRegular);
            tv_status.setText(mStatus);

            pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            pullRefreshLayoutAnother = (PullRefreshLayout) findViewById(R.id.pullRefreshLayoutAnother);
            isVisitedDetailsFromFilter = false;

            if (mConnectionDetector.isConnectingToInternet()) {
                CasesArrayList = new ArrayList<>();
                page = 1;
                callGetCasesFilteredListAPI(1, mStatus);
            } else {
                Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
            }

            pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
            pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mConnectionDetector.isConnectingToInternet()) {
                                CasesArrayList = new ArrayList<>();
                                page = 1;
                                callGetCasesFilteredListAPI(1, mStatus);
                            } else {
                                Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                            }
                            pullRefreshLayout.setRefreshing(false);
                        }
                    }, 3000);
                }
            });

            pullRefreshLayoutAnother.setRefreshStyle(PullRefreshLayout.STYLE_RING);
            pullRefreshLayoutAnother.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullRefreshLayoutAnother.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mConnectionDetector.isConnectingToInternet()) {
                                CasesArrayList = new ArrayList<>();
                                page = 1;
                                callGetCasesFilteredListAPI(1, mStatus);
                            } else {
                                Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                            }
                            pullRefreshLayoutAnother.setRefreshing(false);
                        }
                    }, 3000);
                }
            });


        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void callGetCasesFilteredListAPI(int isFirstLoad, String mStatus) {

        if (isFirstLoad == 1) {
            Common.ProgressDialogShow(context, Constant.MESSAGE.PROGRESS_PLEASE_WAIT_MSG);
        }
        Map<String, String> params = new HashMap<String, String>();
        String UserId = AppPreference.getStringPref(context, AppPreference.PREF_USERID, AppPreference.PREF_KEY.USERID);

        params.put(ServiceApi.WEB_SERVICE_KEY.PAGE, String.valueOf(page));
        params.put(ServiceApi.WEB_SERVICE_KEY.USER_ID, UserId);
        params.put(ServiceApi.WEB_SERVICE_KEY.STATUS, mStatus);

        String strValue = "";
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            String value = params.get(key);
            strValue = strValue + value + "|";
        }

        strValue = Common.hashKey(strValue);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_KEY, Constant.API_KEY);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_TOKEN, strValue);


        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.CASES_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parseJsonGetTournamentList(response);
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
                            Toast.makeText(context, Constant.MESSAGE.CONNECTION_TIMEOUT, Toast.LENGTH_LONG).show();
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
    private void parseJsonGetTournamentList(JSONObject jsonObject) {
        try {

            Common.ProgressDialogDismiss();
            Log.i("get response", "get response" + jsonObject);
            if (jsonObject.toString().contains(Constant.JSON_KEY.MSG)) {
                String message = jsonObject.getString(Constant.JSON_KEY.MSG);
                String status = jsonObject.getString(Constant.JSON_KEY.CODE);

                if (status.equals("0")) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    if (jsonObject.toString().contains(Constant.JSON_KEY.RESPONSE)) {
                        String syncresponse = jsonObject.getString(Constant.JSON_KEY.RESPONSE);
                        JSONObject jsonObject1 = new JSONObject(syncresponse);

                        if (jsonObject1.getJSONArray(Constant.JSON_KEY.DATA).length() != 0) {
                            JSONArray responseArray = jsonObject1.getJSONArray(Constant.JSON_KEY.DATA);
                            // CasesArrayList = new ArrayList<>();
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject responseUserFindFriends = responseArray.getJSONObject(i);

                                String mID = responseUserFindFriends.getString(Constant.JSON_KEY.ID);
                                String mContactNo = responseUserFindFriends.getString(Constant.JSON_KEY.CONTACT_NUMBER);
                                String mBank = responseUserFindFriends.getString(Constant.JSON_KEY.BANK);
                                String mRefNo = responseUserFindFriends.getString(Constant.JSON_KEY.REFERENCE_NO);
                                String mAddress = responseUserFindFriends.getString(Constant.JSON_KEY.ADDRESS);
                                String mApplicantName = responseUserFindFriends.getString(Constant.JSON_KEY.APPLICANT_NAME);

                                CasesListClass casesListClass = new CasesListClass();
                                casesListClass.setId(mID);
                                casesListClass.setContactNo(mContactNo);
                                casesListClass.setBank(mBank);
                                casesListClass.setRefNo(mRefNo);
                                casesListClass.setAddress(mAddress);
                                casesListClass.setApplicantName(mApplicantName);
                                CasesArrayList.add(casesListClass);
                            }

                            if (page > 1) {
                                casesListFilterAdapter.notifyDataSetChanged();
                            } else {
                                rvNewsList.setVisibility(View.VISIBLE);
                                casesListFilterAdapter = new CasesListFilterAdapter(rvNewsList, context, CasesArrayList, mActivity);
                                rvNewsList.setAdapter(casesListFilterAdapter);
                                casesListFilterAdapter.notifyDataSetChanged();
                                casesListFilterAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        addItems();
                                    }
                                });
                            }
                            casesListFilterAdapter.setLoaded();
                        }
                        if (CasesArrayList.size() < 1) {
                            tv_record_found.setVisibility(View.VISIBLE);
                            pullRefreshLayoutAnother.setVisibility(View.VISIBLE);
                            pullRefreshLayout.setVisibility(View.GONE);
                            rvNewsList.setVisibility(View.GONE);
                            ll_main_fragment.setVisibility(View.GONE);

                        } else {
                            tv_record_found.setVisibility(View.GONE);
                            rvNewsList.setVisibility(View.VISIBLE);
                            pullRefreshLayoutAnother.setVisibility(View.GONE);
                            pullRefreshLayout.setVisibility(View.VISIBLE);
                            ll_main_fragment.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Common.ProgressDialogDismiss();
            e.printStackTrace();
        }
    }

    private void addItems() {
        try {
            page++;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (mConnectionDetector.isConnectingToInternet()) {
            isLoadMore = false;
            callGetCasesFilteredListAPI(0, mStatus);
        } else {
            Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
//        if (view.equals(btn_sign_in)) {
//
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (isVisitedDetailsFromFilter) {
            if (mConnectionDetector.isConnectingToInternet()) {
                CasesArrayList = new ArrayList<>();
                page = 1;
                callGetCasesFilteredListAPI(0, mStatus);
            } else {
                Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

