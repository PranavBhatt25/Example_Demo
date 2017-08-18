package banksoftware.com.banksoftware;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import fragment.DashBoardFragment;

/**
 * Created by Pranav on 4/22/2017.
 */

public class NavigationDrawer extends SlidingActivity {


    public static ImageView iv_toolbar_left, iv_toolbar_right;
    public TextView toolbar_title = null;
    Context context;
    private Fragment mFragment;
    private DashBoardFragment mDashBoardFragment;
    public static RelativeLayout rr_toolbar_left, rr_toolbar_right;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;

    public NavigationDrawer() {
        //    super(R.string.changing_fragments);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slid_in_right, R.anim.slid_out_left);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
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

        if (savedInstanceState != null)
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "mFragment");

        if (mFragment == null)
            mFragment = new DashBoardFragment();

        if (mFragment instanceof DashBoardFragment) {
            DashBoardFragment dashBoardFragment = (DashBoardFragment) mFragment;
            mDashBoardFragment = dashBoardFragment;
        }

        // set the Above View
        setContentView(R.layout.drawer_navigation_layout);
        //    CrashReportHandler.attach(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, mFragment).commit();

        // set the Behind View
        setBehindContentView(R.layout.content_frame);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new DrawerFillActivity())
                .commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        init();
        rr_toolbar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

    }


    public void switchContent(Fragment fragment) {
        mFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, mFragment).commit();
        getSlidingMenu().showContent();
    }

    public void init() {

        rr_toolbar_left = (RelativeLayout) findViewById(R.id.rr_toolbar_left);
        rr_toolbar_right = (RelativeLayout) findViewById(R.id.rr_toolbar_right);
        iv_toolbar_left = (ImageView) findViewById(R.id.iv_toolbar_left);
        iv_toolbar_right = (ImageView) findViewById(R.id.iv_toolbar_right);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        iv_toolbar_left.setBackgroundResource(R.mipmap.ic_menu_icon);
        // iv_toolbar_right.setImageResource(R.mipmap.ic_launcher);
        iv_toolbar_right.setVisibility(View.GONE);
//        iv_toolbar_right.setImageResource(R.mipmap.ic_notifications);
//        rr_toolbar_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(context, NotificationsActivity.class);
//                startActivity(mainIntent);
//            }
//        });

        iv_toolbar_left.setVisibility(View.VISIBLE);
        toolbar_title.setText("DashBoard");
        toolbar_title.setTypeface(typeFaceRegular);
    }

    @Override
    public void onBackPressed() {
        ExitDailog();
    }

    public void ExitDailog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to exit?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
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
