package veesto.com.android.veesto.Data;

import java.util.ArrayList;

/**
 * Created by erez on 10/11/2016.
 */
public interface IModel {

    void setUserEmail(String email);
    void setIMEI(String imei);
    void setSN(String SN);


    String getUserEmail();
    String getIMEI();
    String getSN();
    ArrayList<String> getFriends();
    void addFriend(String friendName);
}
