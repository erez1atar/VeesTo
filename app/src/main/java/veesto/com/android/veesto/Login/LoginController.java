package veesto.com.android.veesto.Login;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;

import veesto.com.android.veesto.Data.IModel;
import veesto.com.android.veesto.Utility.App;

/**
 * Created by erez on 12/11/2016.
 */
public class LoginController implements ILoginController
{

    private IModel iModel;
    public LoginController()
    {
        iModel = App.getModel();
    }
    @Override
    public void setUserEmail(String email) {
        Log.d("setUserEmail", email);
        iModel.setUserEmail(email);
    }

    @Override
    public void updatePhoneDetails() {
        TelephonyManager telephonyManager = (TelephonyManager)App.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        iModel.setIMEI(telephonyManager.getDeviceId());
        String androidID = Settings.System.getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        iModel.setSN(androidID);
    }

    @Override
    public void addFriend(String friendName) {
        iModel.addFriend(friendName);
    }


}
