package notification;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import util.AppPreference;

/**
 * Created by Pranav on 4/1/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseID";
    Context context;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token:- " + refreshedToken);
        AppPreference.setStringPref(this, AppPreference.PREF_NAME, AppPreference.PREF_KEY.REGID, refreshedToken);
        context = getApplicationContext();
    }
}



