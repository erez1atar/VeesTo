package veesto.com.android.veesto.Data;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by erez on 10/11/2016.
 */
public class Model implements IModel
{
    private String email;
    private String IMEI;
    private String SN;
    private ArrayList<String> friends = new ArrayList<>();

    @Override
    public void setUserEmail(String email) {
        this.email = email;
        Log.d("setUserEmail", email);
    }

    @Override
    public void setIMEI(String imei) {
        this.IMEI = imei;
    }

    @Override
    public void setSN(String SN) {
        this.SN = SN;
    }

    @Override
    public String getUserEmail() {

        return email;
    }

    @Override
    public String getIMEI() {
        return IMEI;
    }

    @Override
    public String getSN() {
        return SN;
    }

    @Override
    public ArrayList<String> getFriends() {
        Log.d("getFriends", "" + friends.size());
        for (String s : friends) {
            Log.d("getFriends", s);
        }
        return friends;
    }

    @Override
    public void addFriend(String friendName) {
        Log.d("addFriend", friendName);
        friends.add(friendName);
    }

}
