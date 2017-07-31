package fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import banksoftware.com.banksoftware.R;
import util.ConnectionDetector;

/**
 * Created by WPA2 on 4/22/2017.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {
    Context context;
    Context mContext;
    Activity mActivity;
    ConnectionDetector mConnectionDetector;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    private WebView wv_app_common;
    ProgressBar progressBar;

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
        View newsListView = inflater.inflate(R.layout.fragment_contact_us, container, false);

        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();
        init(newsListView);
        onClickListener();

        return newsListView;
    }


    private void init(View rootview) {
        typeFaceBold = Typeface.createFromAsset(getActivity().getAssets(), "opensans_bold.ttf");
        typeFaceBoldItalic = Typeface.createFromAsset(getActivity().getAssets(), "opensans_bolditalic.ttf");
        typeFaceExtraBold = Typeface.createFromAsset(getActivity().getAssets(), "opensans_extrabold.ttf");
        typeFaceExtraBoldItalic = Typeface.createFromAsset(getActivity().getAssets(), "opensans_extrabolditalic.ttf");
        typeFaceItalic = Typeface.createFromAsset(getActivity().getAssets(), "opensans_italic.ttf");
        typeFaceLight = Typeface.createFromAsset(getActivity().getAssets(), "opensans_light.ttf");
        typeFaceLightItalic = Typeface.createFromAsset(getActivity().getAssets(), "opensans_lightitalic.ttf");
        typeFaceRegular = Typeface.createFromAsset(getActivity().getAssets(), "opensans_regular.ttf");
        typeSemibold = Typeface.createFromAsset(getActivity().getAssets(), "opensans_semibold.ttf");
        typeSemiboldItalic = Typeface.createFromAsset(getActivity().getAssets(), "opensans_semibolditalic.ttf");



        wv_app_common = (WebView) rootview.findViewById(R.id.wv_app_common);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);

        webView("http://webplanex.co.in/projects/bank_software/public/");
    }

    private void onClickListener() {
        try {
//            btn_public_challanges.setOnClickListener(this);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
//        if (view.equals(btn_public_challanges)) {
//
//        }
    }

    private void webView(String url) {


        wv_app_common.loadUrl(url);
        wv_app_common.getSettings().setJavaScriptEnabled(true);
        wv_app_common.getSettings().setSupportZoom(true);
        wv_app_common.getSettings().setBuiltInZoomControls(true);

        wv_app_common.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //createWebPrintJob(view);
                wv_app_common = view;
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
