package fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
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

import banksoftware.com.banksoftware.R;
import adapter.DashBoardListAdapter;
import interfaces.OnLoadMoreListener;
import model.DashBoardListClass;
import util.AppPreference;
import util.ApplicationClass;
import util.Common;
import util.ConnectionDetector;
import util.Constant;
import util.ServiceApi;

/**
 * Created by WPA2 on 4/22/2017.
 */

public class DashBoardFragment extends Fragment implements View.OnClickListener {
    Context context;
    Context mContext;
    Activity mActivity;
    ConnectionDetector mConnectionDetector;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    RecyclerView rvNewsList;
    int page = 1;
    boolean isLoadMore = false;
    ArrayList<DashBoardListClass> DashBoardArrayList = new ArrayList<>();
    DashBoardListAdapter dashBoardListAdapter;
    PullRefreshLayout pullRefreshLayout, pullRefreshLayoutAnother;
    TextView tv_record_found = null;
    LinearLayout ll_main_fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        mConnectionDetector = new ConnectionDetector(context);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View newsListView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();

        init(newsListView);
        onClickListener();

        return newsListView;
    }


    private void init(View rootview) {
        typeFaceBold = Typeface.createFromAsset(context.getAssets(), "opensans_bold.ttf");
        typeFaceBoldItalic = Typeface.createFromAsset(context.getAssets(), "opensans_bolditalic.ttf");
        typeFaceExtraBold = Typeface.createFromAsset(context.getAssets(), "opensans_extrabold.ttf");
        typeFaceExtraBoldItalic = Typeface.createFromAsset(context.getAssets(), "opensans_extrabolditalic.ttf");
        typeFaceItalic = Typeface.createFromAsset(context.getAssets(), "opensans_italic.ttf");
        typeFaceLight = Typeface.createFromAsset(context.getAssets(), "opensans_light.ttf");
        typeFaceLightItalic = Typeface.createFromAsset(context.getAssets(), "opensans_lightitalic.ttf");
        typeFaceRegular = Typeface.createFromAsset(context.getAssets(), "opensans_regular.ttf");
        typeSemibold = Typeface.createFromAsset(context.getAssets(), "opensans_semibold.ttf");
        typeSemiboldItalic = Typeface.createFromAsset(context.getAssets(), "opensans_semibolditalic.ttf");

        ll_main_fragment = (LinearLayout) rootview.findViewById(R.id.ll_main_fragment);
        rvNewsList = (RecyclerView) rootview.findViewById(R.id.rvNewsList);
        rvNewsList.setLayoutManager(new LinearLayoutManager(context));
        tv_record_found = (TextView) rootview.findViewById(R.id.tv_record_found);
        tv_record_found.setVisibility(View.GONE);
        tv_record_found.setTypeface(typeFaceRegular);


        pullRefreshLayout = (PullRefreshLayout) rootview.findViewById(R.id.swipeRefreshLayout);
        pullRefreshLayoutAnother = (PullRefreshLayout) rootview.findViewById(R.id.pullRefreshLayoutAnother);

        if (mConnectionDetector.isConnectingToInternet()) {
            DashBoardArrayList = new ArrayList<>();
            callGetDashBoardDetailsAPI(1);

            // setData();
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
                            DashBoardArrayList = new ArrayList<>();
                            callGetDashBoardDetailsAPI(1);
                           //setData();
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
                            DashBoardArrayList = new ArrayList<>();
                            callGetDashBoardDetailsAPI(1);
                           // setData();
                        } else {
                            Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                        }
                        pullRefreshLayoutAnother.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }

    private void onClickListener() {
        try {
            //ll_post_image.setOnClickListener(this);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
//        if (view.equals(btn_public_challanges)) {
//
//        } else if (view.equals(btn_private_challanges)) {

    }

    private void callGetDashBoardDetailsAPI(int isFirstLoad) {

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


        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.CASE_COUNT_URL, new JSONObject(params),
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
                // SyncTime = jsonObject.getString(Constant.JSON_KEY.SYNCTIME);

                if (status.equals("0")) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    if (jsonObject.toString().contains(Constant.JSON_KEY.RESPONSE)) {
                        if (jsonObject.getJSONArray(Constant.JSON_KEY.RESPONSE).length() != 0) {
                            JSONArray responseArray = jsonObject.getJSONArray(Constant.JSON_KEY.RESPONSE);
                            DashBoardArrayList = new ArrayList<>();
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject responseUserFindFriends = responseArray.getJSONObject(i);

                                String mStatus = responseUserFindFriends.getString(Constant.JSON_KEY.STATUS);
                                String mCaseCount = responseUserFindFriends.getString(Constant.JSON_KEY.COUNT);

                                DashBoardListClass dashBoardListClass = new DashBoardListClass();
                                dashBoardListClass.setStatus(mStatus);
                                dashBoardListClass.setCaseCount(mCaseCount);
                                DashBoardArrayList.add(dashBoardListClass);
                            }

                            rvNewsList.setVisibility(View.VISIBLE);
                            dashBoardListAdapter = new DashBoardListAdapter(rvNewsList, context, DashBoardArrayList, mActivity);
                            rvNewsList.setAdapter(dashBoardListAdapter);
                            dashBoardListAdapter.notifyDataSetChanged();
                        }
                        if (DashBoardArrayList.size() < 1) {
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

    public void setData() {
        DashBoardListClass dashBoardListClass = new DashBoardListClass();
        dashBoardListClass.setStatus("TO FINALIZING");
        dashBoardListClass.setCaseCount("(5)");
        DashBoardArrayList.add(dashBoardListClass);

        dashBoardListClass = new DashBoardListClass();
        dashBoardListClass.setStatus("REPORT COMPLETE");
        dashBoardListClass.setCaseCount("(8)");
        DashBoardArrayList.add(dashBoardListClass);

        dashBoardListClass = new DashBoardListClass();
        dashBoardListClass.setStatus("CASE TO ENGINEER");
        dashBoardListClass.setCaseCount("(11)");
        DashBoardArrayList.add(dashBoardListClass);

        dashBoardListClass = new DashBoardListClass();
        dashBoardListClass.setStatus("DISPATCHED");
        dashBoardListClass.setCaseCount("(10)");
        DashBoardArrayList.add(dashBoardListClass);

        dashBoardListClass = new DashBoardListClass();
        dashBoardListClass.setStatus("CHECKLIST RECEIVED");
        dashBoardListClass.setCaseCount("(12)");
        DashBoardArrayList.add(dashBoardListClass);

        dashBoardListClass = new DashBoardListClass();
        dashBoardListClass.setStatus("DISPATCHED");
        dashBoardListClass.setCaseCount("(33)");
        DashBoardArrayList.add(dashBoardListClass);

        if (DashBoardArrayList.size() < 1) {
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

        rvNewsList.setVisibility(View.VISIBLE);
        dashBoardListAdapter = new DashBoardListAdapter(rvNewsList, context, DashBoardArrayList, mActivity);
        rvNewsList.setAdapter(dashBoardListAdapter);
        dashBoardListAdapter.notifyDataSetChanged();
    }
}
