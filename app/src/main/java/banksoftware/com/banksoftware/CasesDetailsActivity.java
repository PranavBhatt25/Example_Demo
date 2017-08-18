package banksoftware.com.banksoftware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import model.CasesListDetailsClass;
import util.AppPreference;
import util.ApplicationClass;
import util.Common;
import util.ConnectionDetector;
import util.Constant;
import util.ServiceApi;

import static banksoftware.com.banksoftware.CaseListFilteredActivity.isVisitedDetailsFromFilter;
import static fragment.CasesFragment.isVisitedDetails;
import static util.Common.openDialer;


/**
 * Created by Pranav on 25/06/16.
 */
public class CasesDetailsActivity extends Activity implements View.OnClickListener {

    public static Context context;
    ConnectionDetector mConnectionDetector;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    private ImageView iv_toolbar_left = null, iv_toolbar_right = null;
    private TextView toolbar_title = null;
    private RelativeLayout rr_toolbar_left = null, rr_toolbar_right = null, rrEditButton = null;
    public TextView tv_ref_no = null, tv_status_no_title = null, tv_status = null, tv_ref_no_title = null, tv_ref = null,
            tv_date_received_title = null, tv_date_received = null,
            tv_date_sent_title = null, tv_date_sent = null, tv_name_title = null, tv_name = null, tv_case_type_title = null,
            tv_case_type = null, tv_bank_title = null, tv_bank = null, tv_hub_title = null, tv_hub = null, tv_branch_title = null, tv_branch = null,
            tv_branch_contact_person_title = null, tv_branch_contact_person = null,
            tv_state_title = null, tv_state = null, tv_district_title = null, tv_district = null,
            tv_city_title = null, tv_city = null, tv_zone_title = null,
            tv_zone = null, tv_address_one_title = null, tv_address_one = null, tv_address_two_title = null, tv_address_two = null,
            tv_address_two_three_title = null, tv_address_three = null,
            tv_flat_no_title = null, tv_flat_no = null, tv_floor_title = null, tv_floor = null, tv_Plot_title = null, tv_Plot = null,
            tv_building_tower_title = null, tv_building_tower = null, tv_society_name_title = null, tv_society_name = null, tv_lane_phase_sector_poket_title = null,
            tv_lane_phase_sector_poket = null, tv_colony_title = null, tv_colony = null,
            tv_mobile_title = null, tv_mobile = null, tv_pin_code_title = null, tv_pin_code = null, tv_file_no_title = null,
            tv_file_no = null, tv_invoice_no_title = null, tv_invoice_no = null, tv_invoice_date_title = null, tv_invoice_date = null, tv_payment_title = null,
            tv_payment = null, tv_bank_fees_title = null, tv_bank_fees = null, tv_revision_fees_title = null, tv_revision_fees = null,
            tv_market_value_title = null, tv_market_value = null,
            tv_appontment_date_title = null, tv_appontment_date = null,
            tv_visit_done_date_title = null, tv_visit_done_date = null;
    public ArrayList<CasesListDetailsClass> CasesListDetailsArrayList = new ArrayList<>();
    String caseId = "";
    public static boolean isVisitedDetailsDetail = false;
    String mStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slid_in_right, R.anim.slid_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        mConnectionDetector = new ConnectionDetector(context);
        setContentView(R.layout.activity_case_details);

        caseId = getIntent().getStringExtra("caseId");
        isVisitedDetails = true;
        isVisitedDetailsFromFilter = true;

        init();
        onClickListener();
    }


    private void onClickListener() {
        rrEditButton.setOnClickListener(this);
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
            rrEditButton = (RelativeLayout) findViewById(R.id.rr_done_button);
            rrEditButton.setVisibility(View.VISIBLE);

            isVisitedDetailsDetail = false;

            toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setText("Case Details");
            rr_toolbar_left = (RelativeLayout) findViewById(R.id.rr_toolbar_left);
            rr_toolbar_right = (RelativeLayout) findViewById(R.id.rr_toolbar_right);
            rr_toolbar_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overridePendingTransition(R.anim.slid_in_left, R.anim.slid_out_right);
                    finish();
                }
            });

            tv_ref_no = (TextView) findViewById(R.id.tv_ref_no);

            tv_status_no_title = (TextView) findViewById(R.id.tv_status_no_title);
            tv_status = (TextView) findViewById(R.id.tv_status);

            tv_ref_no_title = (TextView) findViewById(R.id.tv_ref_no_title);
            tv_ref = (TextView) findViewById(R.id.tv_ref);

            tv_date_received_title = (TextView) findViewById(R.id.tv_date_received_title);
            tv_date_received = (TextView) findViewById(R.id.tv_date_received);

            tv_date_sent_title = (TextView) findViewById(R.id.tv_date_sent_title);
            tv_date_sent = (TextView) findViewById(R.id.tv_date_sent);

            tv_name_title = (TextView) findViewById(R.id.tv_name_title);
            tv_name = (TextView) findViewById(R.id.tv_name);

            tv_case_type_title = (TextView) findViewById(R.id.tv_case_type_title);
            tv_case_type = (TextView) findViewById(R.id.tv_case_type);

            tv_bank_title = (TextView) findViewById(R.id.tv_bank_title);
            tv_bank = (TextView) findViewById(R.id.tv_bank);

            tv_hub_title = (TextView) findViewById(R.id.tv_hub_title);
            tv_hub = (TextView) findViewById(R.id.tv_hub);

            tv_branch_title = (TextView) findViewById(R.id.tv_branch_title);
            tv_branch = (TextView) findViewById(R.id.tv_branch);

            tv_branch_contact_person_title = (TextView) findViewById(R.id.tv_branch_contact_person_title);
            tv_branch_contact_person = (TextView) findViewById(R.id.tv_branch_contact_person);

            tv_state_title = (TextView) findViewById(R.id.tv_state_title);
            tv_state = (TextView) findViewById(R.id.tv_state);

            tv_district_title = (TextView) findViewById(R.id.tv_district_title);
            tv_district = (TextView) findViewById(R.id.tv_district);

            tv_city_title = (TextView) findViewById(R.id.tv_city_title);
            tv_city = (TextView) findViewById(R.id.tv_city);

            tv_zone_title = (TextView) findViewById(R.id.tv_zone_title);
            tv_zone = (TextView) findViewById(R.id.tv_zone);

            tv_address_one_title = (TextView) findViewById(R.id.tv_address_one_title);
            tv_address_one = (TextView) findViewById(R.id.tv_address_one);

            tv_address_two_title = (TextView) findViewById(R.id.tv_address_two_title);
            tv_address_two = (TextView) findViewById(R.id.tv_address_two);

            tv_address_two_three_title = (TextView) findViewById(R.id.tv_address_two_three_title);
            tv_address_three = (TextView) findViewById(R.id.tv_address_three);

            tv_flat_no_title = (TextView) findViewById(R.id.tv_flat_no_title);
            tv_flat_no = (TextView) findViewById(R.id.tv_flat_no);

            tv_floor_title = (TextView) findViewById(R.id.tv_floor_title);
            tv_floor = (TextView) findViewById(R.id.tv_floor);

            tv_Plot_title = (TextView) findViewById(R.id.tv_Plot_title);
            tv_Plot = (TextView) findViewById(R.id.tv_Plot);

            tv_building_tower_title = (TextView) findViewById(R.id.tv_building_tower_title);
            tv_building_tower = (TextView) findViewById(R.id.tv_building_tower);

            tv_society_name_title = (TextView) findViewById(R.id.tv_society_name_title);
            tv_society_name = (TextView) findViewById(R.id.tv_society_name);

            tv_lane_phase_sector_poket_title = (TextView) findViewById(R.id.tv_lane_phase_sector_poket_title);
            tv_lane_phase_sector_poket = (TextView) findViewById(R.id.tv_lane_phase_sector_poket);

            tv_colony_title = (TextView) findViewById(R.id.tv_colony_title);
            tv_colony = (TextView) findViewById(R.id.tv_colony);

            tv_mobile_title = (TextView) findViewById(R.id.tv_mobile_title);
            tv_mobile = (TextView) findViewById(R.id.tv_mobile);

            tv_pin_code_title = (TextView) findViewById(R.id.tv_pin_code_title);
            tv_pin_code = (TextView) findViewById(R.id.tv_pin_code);

            tv_file_no_title = (TextView) findViewById(R.id.tv_file_no_title);
            tv_file_no = (TextView) findViewById(R.id.tv_file_no);

            tv_invoice_no_title = (TextView) findViewById(R.id.tv_invoice_no_title);
            tv_invoice_no = (TextView) findViewById(R.id.tv_invoice_no);

            tv_invoice_date_title = (TextView) findViewById(R.id.tv_invoice_date_title);
            tv_invoice_date = (TextView) findViewById(R.id.tv_invoice_date);

            tv_payment_title = (TextView) findViewById(R.id.tv_payment_title);
            tv_payment = (TextView) findViewById(R.id.tv_payment);

            tv_bank_fees_title = (TextView) findViewById(R.id.tv_bank_fees_title);
            tv_bank_fees = (TextView) findViewById(R.id.tv_bank_fees);

            tv_revision_fees_title = (TextView) findViewById(R.id.tv_revision_fees_title);
            tv_revision_fees = (TextView) findViewById(R.id.tv_revision_fees);

            tv_market_value_title = (TextView) findViewById(R.id.tv_market_value_title);
            tv_market_value = (TextView) findViewById(R.id.tv_market_value);

            tv_appontment_date_title = (TextView) findViewById(R.id.tv_appontment_date_title);
            tv_appontment_date = (TextView) findViewById(R.id.tv_appontment_date);

            tv_visit_done_date_title = (TextView) findViewById(R.id.tv_visit_done_date_title);
            tv_visit_done_date = (TextView) findViewById(R.id.tv_visit_done_date);

            if (mConnectionDetector.isConnectingToInternet()) {
                CasesListDetailsArrayList = new ArrayList<>();
                callGetCasesDetailsAPI(1);
            } else {
                Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
            }

            tv_mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tvContactNumber = tv_mobile.getText().toString();
                    openDialer(context, tvContactNumber);
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

        params.put(ServiceApi.WEB_SERVICE_KEY.CASE_ID, caseId);

        String strValue = "";
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            String value = params.get(key);
            strValue = strValue + value + "|";
        }

        strValue = Common.hashKey(strValue);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_KEY, Constant.API_KEY);
        params.put(ServiceApi.WEB_SERVICE_KEY.API_TOKEN, strValue);


        JsonObjectRequest request = new JsonObjectRequest(ServiceApi.URL.CASE_DETAILS_URL, new JSONObject(params),
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
                        CasesListDetailsArrayList = new ArrayList<>();

                        try {
                            JSONObject responseCaseDetails = jsonObject.getJSONObject(Constant.JSON_KEY.RESPONSE);
                            String mID = responseCaseDetails.getString(Constant.JSON_KEY.ID);
                            String mFileNumber = responseCaseDetails.getString(Constant.JSON_KEY.FILE_NO);
                            mStatus = responseCaseDetails.getString(Constant.JSON_KEY.STATUS);
                            String mCaseTepe = responseCaseDetails.getString(Constant.JSON_KEY.TYPE);
                            String mRefNo = responseCaseDetails.getString(Constant.JSON_KEY.REFERENCE_NO);
                            String mName = responseCaseDetails.getString(Constant.JSON_KEY.APPLICANT_NAME);
                            String mBankId = responseCaseDetails.getString(Constant.JSON_KEY.BANK_ID);
                            String mHub = responseCaseDetails.getString(Constant.JSON_KEY.HUB_ID);
                            String mBranchId = responseCaseDetails.getString(Constant.JSON_KEY.BRANCH_ID);
                            String mAddressOne = responseCaseDetails.getString(Constant.JSON_KEY.ADDRESS_ONE);
                            String mAddressTwo = responseCaseDetails.getString(Constant.JSON_KEY.ADDRESS_TWO);
                            String mAddressThree = responseCaseDetails.getString(Constant.JSON_KEY.ADDRESS_THREE);
                            String mFlatNumber = responseCaseDetails.getString(Constant.JSON_KEY.FLAT_NO);
                            String mFloor = responseCaseDetails.getString(Constant.JSON_KEY.FLOOR);
                            String mPlotNumber = responseCaseDetails.getString(Constant.JSON_KEY.PLOT_NO);
                            String mBuildingBlock = responseCaseDetails.getString(Constant.JSON_KEY.BUILDING_BLOCK);
                            String mSocietyName = responseCaseDetails.getString(Constant.JSON_KEY.SOCIETY_PROJECT_NAME);
                            String mLanePhaseSector = responseCaseDetails.getString(Constant.JSON_KEY.SECTOR_OR_POCKET);
                            String mColony = responseCaseDetails.getString(Constant.JSON_KEY.COLONY);
                            String mCity = responseCaseDetails.getString(Constant.JSON_KEY.CITY);
                            String mStateId = responseCaseDetails.getString(Constant.JSON_KEY.STATE_ID);
                            String mDistrictId = responseCaseDetails.getString(Constant.JSON_KEY.DISTRICT_ID);
                            String mMobile = responseCaseDetails.getString(Constant.JSON_KEY.MOBILE);
                            String mPinCode = responseCaseDetails.getString(Constant.JSON_KEY.PINCODE);
                            String mZoneId = responseCaseDetails.getString(Constant.JSON_KEY.ZONE_ID);
                            String mDateReceived = responseCaseDetails.getString(Constant.JSON_KEY.DATE_RECEIVED);
                            String mDateSent = responseCaseDetails.getString(Constant.JSON_KEY.DATE_SENT);
                            String mInvoiceNumber = responseCaseDetails.getString(Constant.JSON_KEY.INVOICE_NO);
                            String mInvoiceDate = responseCaseDetails.getString(Constant.JSON_KEY.INVOICE_DATE);
                            String mPayment = responseCaseDetails.getString(Constant.JSON_KEY.PAYMENT_DONE);
                            String mBankFees = responseCaseDetails.getString(Constant.JSON_KEY.BANK_FEES);
                            String mMarketValue = responseCaseDetails.getString(Constant.JSON_KEY.MARKET_VELUE);
                            String mBankName = responseCaseDetails.getString(Constant.JSON_KEY.BANK_NAME);
                            String mHubName = responseCaseDetails.getString(Constant.JSON_KEY.HUB_NAME);
                            String mBranchName = responseCaseDetails.getString(Constant.JSON_KEY.BRANCH_NAME);
                            String mBranchPersonName = responseCaseDetails.getString(Constant.JSON_KEY.BRANCH_CONTACT_PERSION);
                            String mZoneName = responseCaseDetails.getString(Constant.JSON_KEY.ZONE_NAME);
                            String mStateName = responseCaseDetails.getString(Constant.JSON_KEY.STATE_NAME);
                            String mDistrictName = responseCaseDetails.getString(Constant.JSON_KEY.DISTRICT_NAME);
                            String mRevisionFees = responseCaseDetails.getString(Constant.JSON_KEY.REVISION_FEES);
                            String mAppointmentDate = responseCaseDetails.getString(Constant.JSON_KEY.APPOINTMENT_DATE);
                            String mVisitDoneDate = responseCaseDetails.getString(Constant.JSON_KEY.VISIT_DONE_DATE);


                            tv_status_no_title.setTypeface(typeSemibold);
                            tv_status.setTypeface(typeFaceRegular);
                            tv_ref_no_title.setTypeface(typeSemibold);
                            tv_ref.setTypeface(typeFaceRegular);
                            tv_date_received_title.setTypeface(typeSemibold);
                            tv_date_received.setTypeface(typeFaceRegular);
                            tv_date_sent_title.setTypeface(typeSemibold);
                            tv_date_sent.setTypeface(typeFaceRegular);
                            tv_name_title.setTypeface(typeSemibold);
                            tv_name.setTypeface(typeFaceRegular);
                            tv_case_type_title.setTypeface(typeSemibold);
                            tv_case_type.setTypeface(typeFaceRegular);
                            tv_bank_title.setTypeface(typeSemibold);
                            tv_bank.setTypeface(typeFaceRegular);
                            tv_hub_title.setTypeface(typeSemibold);
                            tv_hub.setTypeface(typeFaceRegular);
                            tv_branch_title.setTypeface(typeSemibold);
                            tv_branch.setTypeface(typeFaceRegular);
                            tv_branch_contact_person_title.setTypeface(typeSemibold);
                            tv_branch_contact_person.setTypeface(typeFaceRegular);
                            tv_state_title.setTypeface(typeSemibold);
                            tv_state.setTypeface(typeFaceRegular);
                            tv_district_title.setTypeface(typeSemibold);
                            tv_district.setTypeface(typeFaceRegular);
                            tv_city_title.setTypeface(typeSemibold);
                            tv_city.setTypeface(typeFaceRegular);
                            tv_zone_title.setTypeface(typeSemibold);
                            tv_zone.setTypeface(typeFaceRegular);
                            tv_address_one_title.setTypeface(typeSemibold);
                            tv_address_one.setTypeface(typeFaceRegular);
                            tv_address_two_title.setTypeface(typeSemibold);
                            tv_address_two.setTypeface(typeFaceRegular);
                            tv_address_two_three_title.setTypeface(typeSemibold);
                            tv_address_three.setTypeface(typeFaceRegular);
                            tv_flat_no_title.setTypeface(typeSemibold);
                            tv_flat_no.setTypeface(typeFaceRegular);
                            tv_floor_title.setTypeface(typeSemibold);
                            tv_floor.setTypeface(typeFaceRegular);
                            tv_Plot_title.setTypeface(typeSemibold);
                            tv_Plot.setTypeface(typeFaceRegular);
                            tv_building_tower_title.setTypeface(typeSemibold);
                            tv_building_tower.setTypeface(typeFaceRegular);
                            tv_society_name_title.setTypeface(typeSemibold);
                            tv_society_name.setTypeface(typeFaceRegular);
                            tv_lane_phase_sector_poket_title.setTypeface(typeSemibold);
                            tv_lane_phase_sector_poket.setTypeface(typeFaceRegular);
                            tv_colony_title.setTypeface(typeSemibold);
                            tv_colony.setTypeface(typeFaceRegular);
                            tv_mobile_title.setTypeface(typeSemibold);
                            tv_mobile.setTypeface(typeFaceRegular);
                            tv_pin_code_title.setTypeface(typeSemibold);
                            tv_pin_code.setTypeface(typeFaceRegular);
                            tv_file_no_title.setTypeface(typeSemibold);
                            tv_file_no.setTypeface(typeFaceRegular);
                            tv_invoice_no_title.setTypeface(typeSemibold);
                            tv_invoice_no.setTypeface(typeFaceRegular);
                            tv_invoice_date_title.setTypeface(typeSemibold);
                            tv_invoice_date.setTypeface(typeFaceRegular);
                            tv_payment_title.setTypeface(typeSemibold);
                            tv_payment.setTypeface(typeFaceRegular);
                            tv_bank_fees_title.setTypeface(typeSemibold);
                            tv_bank_fees.setTypeface(typeFaceRegular);
                            tv_revision_fees_title.setTypeface(typeSemibold);
                            tv_revision_fees.setTypeface(typeFaceRegular);
                            tv_market_value_title.setTypeface(typeSemibold);
                            tv_market_value.setTypeface(typeFaceRegular);

                            tv_appontment_date_title.setTypeface(typeSemibold);
                            tv_appontment_date.setTypeface(typeFaceRegular);
                            tv_visit_done_date_title.setTypeface(typeSemibold);
                            tv_visit_done_date.setTypeface(typeFaceRegular);


                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                                if (!mRefNo.equals("null") && !mRefNo.equals("")) {
                                    tv_ref_no.setText(Html.fromHtml(mRefNo, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_ref_no.setText("-");
                                }

                                if (!mStatus.equals("null") && !mStatus.equals("")) {
                                    tv_status.setText(Html.fromHtml(mStatus, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_status.setText("-");
                                }

                                if (!mRefNo.equals("null") && !mRefNo.equals("")) {
                                    tv_ref.setText(Html.fromHtml(mRefNo, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_ref.setText("-");
                                }

                                if (!mDateReceived.equals("null") && !mDateReceived.equals("")) {
                                    tv_date_received.setText(Html.fromHtml(mDateReceived, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_date_received.setText("-");
                                }

                                if (!mDateSent.equals("null") && !mDateSent.equals("")) {
                                    tv_date_sent.setText(Html.fromHtml(mDateSent, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_date_sent.setText("-");
                                }

                                if (!mName.equals("null") && !mName.equals("")) {
                                    tv_name.setText(Html.fromHtml(mName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_name.setText("-");
                                }

                                if (!mCaseTepe.equals("null") && !mCaseTepe.equals("")) {
                                    tv_case_type.setText(Html.fromHtml(mCaseTepe, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_case_type.setText("-");
                                }

                                if (!mBranchName.equals("null") && !mBranchName.equals("")) {
                                    tv_branch.setText(Html.fromHtml(mBranchName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_branch.setText("-");
                                }

                                if (!mBankName.equals("null") && !mBankName.equals("")) {
                                    tv_bank.setText(Html.fromHtml(mBankName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_bank.setText("-");
                                }
                                if (!mBranchPersonName.equals("null") && !mBranchPersonName.equals("")) {
                                    tv_branch_contact_person.setText(Html.fromHtml(mBranchPersonName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_branch_contact_person.setText("-");
                                }
                                if (!mStateName.equals("null") && !mStateName.equals("")) {
                                    tv_state.setText(Html.fromHtml(mStateName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_state.setText("-");
                                }

                                if (!mDistrictName.equals("null") && !mDistrictName.equals("")) {
                                    tv_district.setText(Html.fromHtml(mDistrictName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_district.setText("-");
                                }
                                if (!mCity.equals("") && !mCity.equals("")) {
                                    tv_city.setText(Html.fromHtml(mCity, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_city.setText("-");
                                }
                                if (!mZoneName.equals("null") && !mZoneName.equals("")) {
                                    tv_zone.setText(Html.fromHtml(mZoneName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_zone.setText("-");
                                }


                                if (!mAddressOne.equals("null") && !mAddressOne.equals("")) {
                                    tv_address_one.setText(Html.fromHtml(mAddressOne, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_address_one.setText("-");
                                }
                                if (!mAddressTwo.equals("") && !mAddressTwo.equals("")) {
                                    tv_address_two.setText(Html.fromHtml(mAddressTwo, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_address_two.setText("-");
                                }
                                if (!mAddressThree.equals("null") && !mAddressThree.equals("")) {
                                    tv_address_three.setText(Html.fromHtml(mAddressThree, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_address_three.setText("-");
                                }

                                if (!mFlatNumber.equals("null") && !mFlatNumber.equals("")) {
                                    tv_flat_no.setText(Html.fromHtml(mFlatNumber, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_flat_no.setText("-");
                                }
                                if (!mFloor.equals("null") && !mFloor.equals("")) {
                                    tv_floor.setText(Html.fromHtml(mFloor, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_floor.setText("-");
                                }
                                if (!mPlotNumber.equals("null") && !mPlotNumber.equals("")) {
                                    tv_Plot.setText(Html.fromHtml(mPlotNumber, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_Plot.setText("-");
                                }


                                if (!mBuildingBlock.equals("null") && !mBuildingBlock.equals("")) {
                                    tv_building_tower.setText(Html.fromHtml(mBuildingBlock, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_building_tower.setText("-");
                                }
                                if (!mSocietyName.equals("null") && !mSocietyName.equals("")) {
                                    tv_society_name.setText(Html.fromHtml(mSocietyName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_society_name.setText("-");
                                }
                                if (!mLanePhaseSector.equals("null") && !mLanePhaseSector.equals("")) {
                                    tv_lane_phase_sector_poket.setText(Html.fromHtml(mLanePhaseSector, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_lane_phase_sector_poket.setText("-");
                                }


                                if (!mColony.equals("null") && !mColony.equals("")) {
                                    tv_colony.setText(Html.fromHtml(mColony, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_colony.setText("-");
                                }
                                if (!mMobile.equals("null") && !mMobile.equals("")) {
                                    tv_mobile.setText(Html.fromHtml(mMobile, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_mobile.setText("-");
                                }
                                if (!mPinCode.equals("null") && !mPinCode.equals("")) {
                                    tv_pin_code.setText(Html.fromHtml(mPinCode, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_pin_code.setText("-");
                                }

                                if (!mFileNumber.equals("null") && !mFileNumber.equals("")) {
                                    tv_file_no.setText(Html.fromHtml(mFileNumber, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_file_no.setText("-");
                                }
                                if (!mInvoiceNumber.equals("null") && !mInvoiceNumber.equals("")) {
                                    tv_invoice_no.setText(Html.fromHtml(mInvoiceNumber, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_invoice_no.setText("-");
                                }
                                if (!mInvoiceDate.equals("null") && !mInvoiceDate.equals("")) {
                                    tv_invoice_date.setText(Html.fromHtml(mInvoiceDate, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_invoice_date.setText("-");
                                }
                                if (!mPayment.equals("null") && !mPayment.equals("")) {
                                    if (mPayment.equals("0")) {
                                        tv_payment.setText(Html.fromHtml("No", Html.FROM_HTML_MODE_LEGACY));
                                    } else {
                                        tv_payment.setText(Html.fromHtml("Yes", Html.FROM_HTML_MODE_LEGACY));
                                    }
                                } else {
                                    tv_payment.setText("-");
                                }

                                if (!mBankFees.equals("null") && !mBankFees.equals("")) {
                                    tv_bank_fees.setText(Html.fromHtml(mBankFees, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_bank_fees.setText("-");
                                }
                                if (!mRevisionFees.equals("null") && !mRevisionFees.equals("")) {
                                    tv_revision_fees.setText(Html.fromHtml(mRevisionFees, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_revision_fees.setText("-");
                                }
                                if (!mMarketValue.equals("null") && !mMarketValue.equals("")) {
                                    tv_market_value.setText(Html.fromHtml(mMarketValue, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_market_value.setText("-");
                                }
                                if (!mHubName.equals("null") && !mHubName.equals("")) {
                                    tv_hub.setText(Html.fromHtml(mHubName, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_hub.setText("-");
                                }


                                if (!mAppointmentDate.equals("null") && !mAppointmentDate.equals("")) {
                                    tv_appontment_date.setText(Html.fromHtml(mAppointmentDate, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_appontment_date.setText("-");
                                }
                                if (!mVisitDoneDate.equals("null") && !mVisitDoneDate.equals("")) {
                                    tv_visit_done_date.setText(Html.fromHtml(mVisitDoneDate, Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    tv_visit_done_date.setText("-");
                                }

                            } else {

                                if (!mRefNo.equals("null") && !mRefNo.equals("")) {
                                    tv_ref_no.setText(Html.fromHtml(mRefNo));
                                } else {
                                    tv_ref_no.setText("-");
                                }

                                if (!mStatus.equals("null") && !mStatus.equals("")) {
                                    tv_status.setText(Html.fromHtml(mStatus));
                                } else {
                                    tv_status.setText("-");
                                }

                                if (!mRefNo.equals("null") && !mRefNo.equals("")) {
                                    tv_ref.setText(Html.fromHtml(mRefNo));
                                } else {
                                    tv_ref.setText("-");
                                }

                                if (!mDateReceived.equals("null") && !mDateReceived.equals("")) {
                                    tv_date_received.setText(Html.fromHtml(mDateReceived));
                                } else {
                                    tv_date_received.setText("-");
                                }

                                if (!mDateSent.equals("null") && !mDateSent.equals("")) {
                                    tv_date_sent.setText(Html.fromHtml(mDateSent));
                                } else {
                                    tv_date_sent.setText("-");
                                }

                                if (!mName.equals("null") && !mName.equals("")) {
                                    tv_name.setText(Html.fromHtml(mName));
                                } else {
                                    tv_name.setText("-");
                                }

                                if (!mCaseTepe.equals("null") && !mCaseTepe.equals("")) {
                                    tv_case_type.setText(Html.fromHtml(mCaseTepe));
                                } else {
                                    tv_case_type.setText("-");
                                }

                                if (!mBranchName.equals("null") && !mBranchName.equals("")) {
                                    tv_branch.setText(Html.fromHtml(mBranchName));
                                } else {
                                    tv_branch.setText("-");
                                }

                                if (!mBankName.equals("null") && !mBankName.equals("")) {
                                    tv_bank.setText(Html.fromHtml(mBankName));
                                } else {
                                    tv_bank.setText("-");
                                }
                                if (!mBranchPersonName.equals("null") && !mBranchPersonName.equals("")) {
                                    tv_branch_contact_person.setText(Html.fromHtml(mBranchPersonName));
                                } else {
                                    tv_branch_contact_person.setText("-");
                                }
                                if (!mStateName.equals("null") && !mStateName.equals("")) {
                                    tv_state.setText(Html.fromHtml(mStateName));
                                } else {
                                    tv_state.setText("-");
                                }

                                if (!mDistrictName.equals("null") && !mDistrictName.equals("")) {
                                    tv_district.setText(Html.fromHtml(mDistrictName));
                                } else {
                                    tv_district.setText("-");
                                }
                                if (!mCity.equals("null") && !mCity.equals("")) {
                                    tv_city.setText(Html.fromHtml(mCity));
                                } else {
                                    tv_city.setText("-");
                                }
                                if (!mZoneName.equals("null") && !mZoneName.equals("")) {
                                    tv_zone.setText(Html.fromHtml(mZoneName));
                                } else {
                                    tv_zone.setText("-");
                                }


                                if (!mAddressOne.equals("null") && !mAddressOne.equals("")) {
                                    tv_address_one.setText(Html.fromHtml(mAddressOne));
                                } else {
                                    tv_address_one.setText("-");
                                }
                                if (!mAddressTwo.equals("null") && !mAddressTwo.equals("")) {
                                    tv_address_two.setText(Html.fromHtml(mAddressTwo));
                                } else {
                                    tv_address_two.setText("-");
                                }
                                if (!mAddressThree.equals("null") && !mAddressThree.equals("")) {
                                    tv_address_three.setText(Html.fromHtml(mAddressThree));
                                } else {
                                    tv_address_three.setText("-");
                                }

                                if (!mFlatNumber.equals("null") && !mFlatNumber.equals("")) {
                                    tv_flat_no.setText(Html.fromHtml(mFlatNumber));
                                } else {
                                    tv_flat_no.setText("-");
                                }
                                if (!mFloor.equals("null") && !mFloor.equals("")) {
                                    tv_floor.setText(Html.fromHtml(mFloor));
                                } else {
                                    tv_floor.setText("-");
                                }
                                if (!mPlotNumber.equals("null") && !mPlotNumber.equals("")) {
                                    tv_Plot.setText(Html.fromHtml(mPlotNumber));
                                } else {
                                    tv_Plot.setText("-");
                                }


                                if (!mBuildingBlock.equals("null") && !mBuildingBlock.equals("")) {
                                    tv_building_tower.setText(Html.fromHtml(mBuildingBlock));
                                } else {
                                    tv_building_tower.setText("-");
                                }
                                if (!mSocietyName.equals("null") && !mSocietyName.equals("")) {
                                    tv_society_name.setText(Html.fromHtml(mSocietyName));
                                } else {
                                    tv_society_name.setText("-");
                                }
                                if (!mLanePhaseSector.equals("null") && !mLanePhaseSector.equals("")) {
                                    tv_lane_phase_sector_poket.setText(Html.fromHtml(mLanePhaseSector));
                                } else {
                                    tv_lane_phase_sector_poket.setText("-");
                                }


                                if (!mColony.equals("null") && !mColony.equals("")) {
                                    tv_colony.setText(Html.fromHtml(mColony));
                                } else {
                                    tv_colony.setText("-");
                                }
                                if (!mMobile.equals("null") && !mMobile.equals("")) {
                                    tv_mobile.setText(Html.fromHtml(mMobile));
                                } else {
                                    tv_mobile.setText("-");
                                }
                                if (!mPinCode.equals("null") && !mPinCode.equals("")) {
                                    tv_pin_code.setText(Html.fromHtml(mPinCode));
                                } else {
                                    tv_pin_code.setText("-");
                                }

                                if (!mFileNumber.equals("null") && !mFileNumber.equals("")) {
                                    tv_file_no.setText(Html.fromHtml(mFileNumber));
                                } else {
                                    tv_file_no.setText("-");
                                }
                                if (!mInvoiceNumber.equals("null") && !mInvoiceNumber.equals("")) {
                                    tv_invoice_no.setText(Html.fromHtml(mInvoiceNumber));
                                } else {
                                    tv_invoice_no.setText("-");
                                }
                                if (!mInvoiceDate.equals("null") && !mInvoiceDate.equals("")) {
                                    tv_invoice_date.setText(Html.fromHtml(mInvoiceDate));
                                } else {
                                    tv_invoice_date.setText("-");
                                }
                                if (!mPayment.equals("null") && !mPayment.equals("")) {
                                    if (mPayment.equals("0")) {
                                        tv_payment.setText(Html.fromHtml("No"));
                                    } else {
                                        tv_payment.setText(Html.fromHtml("Yes"));
                                    }
                                } else {
                                    tv_payment.setText("-");
                                }

                                if (!mBankFees.equals("null") && !mBankFees.equals("")) {
                                    tv_bank_fees.setText(Html.fromHtml(mBankFees));
                                } else {
                                    tv_bank_fees.setText("-");
                                }
                                if (!mRevisionFees.equals("null") && !mRevisionFees.equals("")) {
                                    tv_revision_fees.setText(Html.fromHtml(mRevisionFees));
                                } else {
                                    tv_revision_fees.setText("-");
                                }
                                if (!mMarketValue.equals("null") && !mMarketValue.equals("")) {
                                    tv_market_value.setText(Html.fromHtml(mMarketValue));
                                } else {
                                    tv_market_value.setText("-");
                                }
                                if (!mHubName.equals("null") && !mHubName.equals("")) {
                                    tv_hub.setText(Html.fromHtml(mHubName));
                                } else {
                                    tv_hub.setText("-");
                                }

                                if (!mAppointmentDate.equals("null") && !mAppointmentDate.equals("")) {
                                    tv_appontment_date.setText(Html.fromHtml(mAppointmentDate));
                                } else {
                                    tv_appontment_date.setText("-");
                                }

                                if (!mVisitDoneDate.equals("null") && !mVisitDoneDate.equals("")) {
                                    tv_visit_done_date.setText(Html.fromHtml(mVisitDoneDate));
                                } else {
                                    tv_visit_done_date.setText("-");
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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
    public void onClick(View view) {
        if (view.equals(rrEditButton)) {
            Intent i = new Intent(CasesDetailsActivity.this, StatusListActivity.class);
            i.putExtra("case_id", caseId);
            startActivity(i);
        }
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (isVisitedDetailsDetail) {
            if (mConnectionDetector.isConnectingToInternet()) {
                CasesListDetailsArrayList = new ArrayList<>();
                callGetCasesDetailsAPI(0);
            } else {
                Toast.makeText(context, getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
            }
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
}

