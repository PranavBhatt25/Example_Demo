package banksoftware.com.banksoftware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import adapter.CasesListAdapter;
import adapter.StatusListAdapter;
import interfaces.OnLoadMoreListener;
import model.CasesListClass;
import model.StatusListClass;
import util.AppPreference;
import util.ApplicationClass;
import util.Common;
import util.ConnectionDetector;
import util.Constant;
import util.ServiceApi;

import static banksoftware.com.banksoftware.CaseListFilteredActivity.isVisitedDetailsFromFilter;
import static fragment.CasesFragment.isVisitedDetails;


/**
 * Created by Pranav on 25/06/16.
 */
public class StatusListActivity extends Activity implements View.OnClickListener {

    public static Context context;
    ConnectionDetector mConnectionDetector;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    private ImageView iv_toolbar_left = null, iv_toolbar_right = null;
    private TextView toolbar_title = null;
    private RelativeLayout rr_toolbar_left = null, rr_toolbar_right = null;
    RecyclerView rvNewsList;
    ArrayList<StatusListClass> StatusArrayList = new ArrayList<>();
    StatusListAdapter statusListAdapter;
    PullRefreshLayout pullRefreshLayout, pullRefreshLayoutAnother;
    TextView tv_record_found = null;
    LinearLayout ll_main_fragment;
    String case_id = "", mStatus = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slid_in_right, R.anim.slid_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        mConnectionDetector = new ConnectionDetector(context);
        setContentView(R.layout.activity_edit_case);

        //case_id = getIntent().getStringExtra("case_id");
        case_id = getIntent().getStringExtra("caseId");
        mStatus = getIntent().getStringExtra("mStatus");
        init();
        onClickListener();
    }


    private void onClickListener() {
        // btn_sign_in.setOnClickListener(this);
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

            isVisitedDetailsFromFilter = true;
            isVisitedDetails = true;

            iv_toolbar_left = (ImageView) findViewById(R.id.iv_toolbar_left);
            iv_toolbar_left.setImageResource(R.mipmap.ic_back);
            iv_toolbar_left.setVisibility(View.VISIBLE);
            iv_toolbar_right = (ImageView) findViewById(R.id.iv_toolbar_right);
            toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setText("Edit Status");
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


            pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            pullRefreshLayoutAnother = (PullRefreshLayout) findViewById(R.id.pullRefreshLayoutAnother);

            if (mConnectionDetector.isConnectingToInternet()) {
                StatusArrayList = new ArrayList<>();
                callGetCasesDetailsAPI(1);
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
                                StatusArrayList = new ArrayList<>();
                                callGetCasesDetailsAPI(1);
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
                                StatusArrayList = new ArrayList<>();
                                callGetCasesDetailsAPI(1);
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

    private void callGetCasesDetailsAPI(int isFirstLoad) {

        if (isFirstLoad == 1) {
            Common.ProgressDialogShow(context, Constant.MESSAGE.PROGRESS_PLEASE_WAIT_MSG);
        }
        Map<String, String> params = new HashMap<String, String>();
        String UserId = AppPreference.getStringPref(context, AppPreference.PREF_USERID, AppPreference.PREF_KEY.USERID);

        params.put(ServiceApi.WEB_SERVICE_KEY.USER_ID, UserId);

        String strValue = "";
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            String value = params.get(key);
            strValue = strValue + value + "|";
        }

        strValue = Common.hashKey(strValue);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_KEY, Constant.API_KEY);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_TOKEN, strValue);


        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.STATUSES_URL, new JSONObject(params),
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
                        if (jsonObject.getJSONArray(Constant.JSON_KEY.RESPONSE).length() != 0) {
                            JSONArray responseArray = jsonObject.getJSONArray(Constant.JSON_KEY.RESPONSE);
                            StatusArrayList = new ArrayList<>();
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject responseUserFindFriends = responseArray.getJSONObject(i);

                                String mStatusAllowed = responseUserFindFriends.getString(Constant.JSON_KEY.STATUS_ALLOWED);
                                String[] namesList = mStatusAllowed.split(",");

                                for (String statusName : namesList) {
                                    System.out.println(statusName);
                                    StatusListClass statusListClass = new StatusListClass();
                                    statusListClass.setStatusName(statusName);
                                    StatusArrayList.add(statusListClass);
                                }
                            }

                            rvNewsList.setVisibility(View.VISIBLE);
                            statusListAdapter = new StatusListAdapter(rvNewsList, context, StatusArrayList, case_id,mStatus, StatusListActivity.this);
                            rvNewsList.setAdapter(statusListAdapter);
                            statusListAdapter.notifyDataSetChanged();
                        }
                        if (StatusArrayList.size() < 1) {
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


    /*
      Will receive the activity result and check which request we are responding to

     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

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
    protected void onPause() {
        super.onPause();
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
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}

