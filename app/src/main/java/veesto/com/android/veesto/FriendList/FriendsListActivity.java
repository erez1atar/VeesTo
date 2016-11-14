package veesto.com.android.veesto.FriendList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import veesto.com.android.veesto.BlurredImage.ImageActivity;
import veesto.com.android.veesto.BlurredPicture.BlurredPicActivity;
import veesto.com.android.veesto.Data.IModel;
import veesto.com.android.veesto.Location.LocationActivity;
import veesto.com.android.veesto.R;
import veesto.com.android.veesto.Record.RecordActivity;
import veesto.com.android.veesto.Utility.App;

public class FriendsListActivity extends AppCompatActivity {

    private IModel iModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        iModel = App.getModel();

        TextView imei = (TextView)findViewById(R.id.imei);
        imei.setText(iModel.getIMEI());

        TextView sn = (TextView)findViewById(R.id.sn);
        sn.setText(iModel.getSN());

        TextView email = (TextView)findViewById(R.id.email_text_friends_activity);
        email.setText(iModel.getUserEmail());

        ListView listView = (ListView)findViewById(R.id.friends_list);
        listView.setAdapter(new FriendsListAdapter(this,R.layout.friend_card,iModel.getFriends()));
        initButtons();
    }

    private void initButtons() {
        Button moveToRecordButton = (Button)findViewById(R.id.move_to_record_screen_button);
        moveToRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsListActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        Button moveToLocationButton = (Button)findViewById(R.id.move_to_map_screen_button);
        moveToLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsListActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        Button moveToImageButton = (Button)findViewById(R.id.move_to_image_screen_button);
        moveToImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsListActivity.this, BlurredPicActivity.class);
                startActivity(intent);
            }
        });
    }
}
