package adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import banksoftware.com.banksoftware.CasesDetailsActivity;
import banksoftware.com.banksoftware.R;
import banksoftware.com.banksoftware.StatusListActivity;
import holders.CasesViewHolder;
import holders.StatusViewHolder;
import interfaces.OnLoadMoreListener;
import model.CasesListClass;
import model.StatusListClass;
import util.AppPreference;
import util.ApplicationClass;
import util.Common;
import util.ConnectionDetector;
import util.Constant;
import util.ServiceApi;

import static banksoftware.com.banksoftware.CasesDetailsActivity.isVisitedDetailsDetail;

/**
 * Created by Pranav on 4/22/2017.
 */

public class StatusListAdapter extends RecyclerView.Adapter<StatusViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<StatusListClass> casesArrayList = new ArrayList<>();
    ConnectionDetector mConnectionDetector;
    View view;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    RecyclerView recyclerViewNew;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;
    StatusListActivity statusListActivity;
    String case_id = "", StatusName = "";

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;
    EditText et_show_date_time;
    String mAppointmentDate = "";
    String mVisitDoneDate = "", mStatus = "";

    public StatusListAdapter(RecyclerView recyclerView, Context context, ArrayList<StatusListClass>
            casesArrayList, String case_id, String mStatus, StatusListActivity statusListActivity) {
        this.context = context;
        this.casesArrayList = casesArrayList;
        this.case_id = case_id;
        this.mStatus = mStatus;
        this.statusListActivity = statusListActivity;
        inflater = LayoutInflater.from(this.context);
        mConnectionDetector = new ConnectionDetector(context);

        this.recyclerViewNew = recyclerView;
        function(recyclerViewNew);

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

    }

    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_status, parent, false);

        StatusViewHolder sectionViewHolder = new StatusViewHolder(view);
        return sectionViewHolder;
    }

    @Override
    public void onBindViewHolder(StatusViewHolder mViewHolder, final int position) {
        final int pos = position;
        final StatusListClass statusListClass = casesArrayList.get(position);

        final String mStatusName = statusListClass.getStatusName();

        mViewHolder.tv_status_name.setText(mStatusName);
        mViewHolder.tv_status_name.setTypeface(typeSemibold);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");
        mViewHolder.tv_selected_status.setTypeface(typeface);
        if (mStatus.equals(mStatusName)) {
            mViewHolder.tv_selected_status.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.tv_selected_status.setVisibility(View.GONE);
        }


        mViewHolder.ll_cases_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusName = statusListClass.getStatusName();
                if (StatusName.equals("APPOINTMENT TAKEN")) {
                    addremarksDialog("APPOINTMENT TAKEN");
                } else if (StatusName.equals("SITE VISITED")) {
                    addremarksDialog("SITE VISITED");
                } else {
                    addremarksDialog("");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return casesArrayList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void function(RecyclerView mRecycler) {
        try {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecycler.getLayoutManager();
            mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = ((LinearLayoutManager) recyclerView.getLayoutManager()).getItemCount();
                    lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        } catch (Exception r) {
            r.getStackTrace();
        }
    }

    public void addremarksDialog(String StatusSelected) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_remarks);

        try {
            TextView tv_dialog_title_main = (TextView) dialog.findViewById(R.id.tv_dialog_title_main);
            et_show_date_time = (EditText) dialog.findViewById(R.id.et_show_date_time);
            et_show_date_time.setFocusable(false);
            et_show_date_time.setClickable(true);
            if (StatusSelected.equals("APPOINTMENT TAKEN")) {
                et_show_date_time.setVisibility(View.VISIBLE);
                et_show_date_time.setHint("Enter Appointment Date");
            } else if (StatusSelected.equals("SITE VISITED")) {
                et_show_date_time.setVisibility(View.VISIBLE);
                et_show_date_time.setHint("Enter Visit Done Date");
            } else {
                et_show_date_time.setVisibility(View.GONE);
            }

            et_show_date_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    datePicker();

                }
            });

            final EditText et_dialog_add_remarks = (EditText) dialog.findViewById(R.id.et_dialog_add_remarks);
            et_dialog_add_remarks.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            Button btn_dialog_add_remarks = (Button) dialog.findViewById(R.id.btn_dialog_add_remarks);
            Button btn_dialog_cancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
            dialog.getWindow().setLayout((int) (getScreenWidth(statusListActivity)), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            tv_dialog_title_main.setTypeface(typeFaceRegular);
            et_dialog_add_remarks.setTypeface(typeFaceRegular);
            btn_dialog_add_remarks.setTypeface(typeFaceRegular);
            btn_dialog_cancel.setTypeface(typeFaceRegular);

            ImageView iv_close_icon = (ImageView) dialog.findViewById(R.id.iv_close_icon);
            iv_close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_dialog_add_remarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mRemarks = et_dialog_add_remarks.getText().toString();

                    if (!StatusName.equals("null") && !StatusName.equals("")) {
                        if (StatusName.equals("APPOINTMENT TAKEN") || StatusName.equals("SITE VISITED")) {
                            String DateTime = et_show_date_time.getText().toString();
                            if (StatusName.equals("APPOINTMENT TAKEN")) {
                                mAppointmentDate = DateTime;
                                mVisitDoneDate = "";
                            } else {
                                mAppointmentDate = "";
                                mVisitDoneDate = DateTime;
                            }

                        } else {
                            mAppointmentDate = "";
                            mVisitDoneDate = "";
                        }
                        if (mConnectionDetector.isConnectingToInternet()) {
                            callSelectedStatusAPI(1, StatusName, mRemarks);
                        } else {
                            Toast.makeText(context, context.getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
                        }
                    }

                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date_time = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;
                //*************Call Time Picker Here ********************
                if (view.isShown()) {
                    tiemPicker();
                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                mHour = hourOfDay;
                mMinute = minute;

                et_show_date_time.setText(date_time + " " + String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void callSelectedStatusAPI(int isFirstLoad, String StatusName, String Remarks) {

        if (isFirstLoad == 1) {
            Common.ProgressDialogShow(context, Constant.MESSAGE.PROGRESS_PLEASE_WAIT_MSG);
        }
        Map<String, String> params = new HashMap<String, String>();
        String UserId = AppPreference.getStringPref(context, AppPreference.PREF_USERID, AppPreference.PREF_KEY.USERID);

        params.put(ServiceApi.WEB_SERVICE_KEY.USER_ID, UserId);
        params.put(ServiceApi.WEB_SERVICE_KEY.STATUS, StatusName);
        params.put(ServiceApi.WEB_SERVICE_KEY.CASE_ID, case_id);
        params.put(ServiceApi.WEB_SERVICE_KEY.REMARKS, Remarks);
        params.put(ServiceApi.WEB_SERVICE_KEY.APPOINTMENT_DATE, mAppointmentDate);
        params.put(ServiceApi.WEB_SERVICE_KEY.VISIT_DONE_DATE, mVisitDoneDate);

        String strValue = "";
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            String value = params.get(key);
            strValue = strValue + value + "|";
        }

        strValue = Common.hashKey(strValue);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_KEY, Constant.API_KEY);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_TOKEN, strValue);


        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.UPDATE_STATUS_URL, new JSONObject(params),
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
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    isVisitedDetailsDetail = true;
                    ((Activity) context).finish();
                }
            }
        } catch (Exception e) {
            Common.ProgressDialogDismiss();
            e.printStackTrace();
        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }
}
