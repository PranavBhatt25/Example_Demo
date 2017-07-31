package banksoftware.com.banksoftware;

import android.app.ListFragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import fragment.AboutUsFragment;
import fragment.CasesFragment;
import fragment.ContactUsFragment;
import fragment.DashBoardFragment;
import util.AppPreference;
import util.ApplicationClass;
import util.Common;
import util.ConnectionDetector;
import util.Constant;
import util.ServiceApi;

import static util.Common.showToast;


/**
 * Created by WPA2 on 2/22/2017.
 */

public class DrawerFillActivity extends ListFragment {
    Context context;
    String strPlayStoreUrl = "";
    ConnectionDetector mConnectionDetector;
    private TypedArray navMenuIcons;
    Typeface typeFaceBold, typeFaceBoldItalic, typeFaceExtraBold, typeFaceExtraBoldItalic,
            typeFaceItalic, typeFaceLight, typeFaceLightItalic, typeFaceRegular,
            typeSemibold, typeSemiboldItalic;
    Fragment newContent = null;
    TextView tv_app_name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.drawer_list, container, false);
        tv_app_name = (TextView) rootView.findViewById(R.id.tv_app_name);
        tv_app_name.setTypeface(typeFaceRegular);

        return rootView;
//        return inflater.inflate(R.layout.drawer_list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        mConnectionDetector = new ConnectionDetector(context);
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

        strPlayStoreUrl = "http://play.google.com/store/apps/details?id=" + context.getPackageName();
        String[] colors = getResources().getStringArray(R.array.menu_names);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        SampleAdapter adapter = new SampleAdapter(getActivity());
        for (int i = 0; i < colors.length; i++) {
            adapter.add(new SampleItem(colors[i], navMenuIcons.getResourceId(i, -1)));
        }
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {


        switch (position) {
            case 0:
                if (getActivity() instanceof NavigationDrawer) {
                    NavigationDrawer navigationDrawer = (NavigationDrawer) getActivity();
                    navigationDrawer.init();
                    navigationDrawer.iv_toolbar_right.setVisibility(View.GONE);
                    navigationDrawer.toolbar_title.setText("DashBoard");
//                    navigationDrawer.iv_toolbar_right.setImageResource(R.mipmap.ic_notifications);
//                    navigationDrawer.rr_toolbar_right.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent mainIntent = new Intent(context, NotificationsActivity.class);
//                            startActivity(mainIntent);
//                        }
//                    });
                }
                newContent = new DashBoardFragment();
                break;
            case 1:
                if (getActivity() instanceof NavigationDrawer) {
                    NavigationDrawer navigationDrawer = (NavigationDrawer) getActivity();
                    navigationDrawer.init();
                    navigationDrawer.iv_toolbar_right.setVisibility(View.GONE);
                    navigationDrawer.toolbar_title.setText("Cases");
//                    navigationDrawer.iv_toolbar_right.setImageResource(R.mipmap.ic_notifications);
//                    navigationDrawer.rr_toolbar_right.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent mainIntent = new Intent(context, NotificationsActivity.class);
//                            startActivity(mainIntent);
//                        }
//                    });
                }
                newContent = new CasesFragment();
                break;
            case 2:
                if (getActivity() instanceof NavigationDrawer) {
                    NavigationDrawer navigationDrawer = (NavigationDrawer) getActivity();
                    navigationDrawer.init();
                    navigationDrawer.iv_toolbar_right.setVisibility(View.GONE);
                    navigationDrawer.toolbar_title.setText("About Us");
//                    navigationDrawer.iv_toolbar_right.setImageResource(R.mipmap.ic_notifications);
//                    navigationDrawer.rr_toolbar_right.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent mainIntent = new Intent(context, NotificationsActivity.class);
//                            startActivity(mainIntent);
//                        }
//                    });
                }
                newContent = new AboutUsFragment();
                break;
            case 3:
                if (getActivity() instanceof NavigationDrawer) {
                    NavigationDrawer navigationDrawer = (NavigationDrawer) getActivity();
                    navigationDrawer.init();
                    navigationDrawer.iv_toolbar_right.setVisibility(View.GONE);
                    navigationDrawer.toolbar_title.setText("Contact Us");
//                    navigationDrawer.iv_toolbar_right.setImageResource(R.mipmap.ic_notifications);
//                    navigationDrawer.rr_toolbar_right.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent mainIntent = new Intent(context, NotificationsActivity.class);
//                            startActivity(mainIntent);
//                        }
//                    });
                }
                newContent = new ContactUsFragment();
                break;
            case 4:
                if (getActivity() instanceof NavigationDrawer) {
                    if (mConnectionDetector.isConnectingToInternet()) {

                        AppPreference.setStringPref(context, AppPreference.PREF_SENT_EMAIL, AppPreference.PREF_KEY.NEW_EMAIL, "");
                        AppPreference.setStringPref(context, AppPreference.PREF_IS_LOGIN, AppPreference.PREF_KEY.LOGIN_STATUS, "0");
                        AppPreference.setStringPref(context, AppPreference.PREF_USERID, AppPreference.PREF_KEY.USERID, "");

                        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.cancelAll();

                        Intent mainIntent = new Intent(context, LoginActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    } else {
                        showToast(context, getString(R.string.please_check_internet));
                    }
                }

                break;
        }


        if (newContent != null)
            switchFragment(newContent);
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof NavigationDrawer) {
            NavigationDrawer navigationDrawer = (NavigationDrawer) getActivity();
            navigationDrawer.switchContent(fragment);
        } /*else if (getActivity() instanceof ResponsiveUIActivity) {
            ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
			ra.switchContent(fragment);
		}*/
    }

    private class SampleItem {
        public String tag;
        public int iconRes;

        public SampleItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class SampleAdapter extends ArrayAdapter<SampleItem> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(getItem(position).iconRes);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(getItem(position).tag);
            typeFaceRegular = Typeface.createFromAsset(getContext().getAssets(), "opensans_regular.ttf");
            title.setTypeface(typeFaceRegular);

            return convertView;
        }
    }

}
