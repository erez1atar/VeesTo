package veesto.com.android.veesto.Record;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import veesto.com.android.veesto.R;

public class RecordActivity extends AppCompatActivity implements IRecordPresentor
{

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID = 2;

    private Button mRecordButton = null;
    private Button mPlayButton = null;


    private IMediaPlayerContoller iMediaPlayerContoller;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_record);

        iMediaPlayerContoller = new MediaPlayerController();
        showDialog();
        iMediaPlayerContoller.setPresentor(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID);
            } else {
                Log.d("Home", "Already granted access");

            }
        }

        mRecordButton = (Button) findViewById(R.id.recording_button);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMediaPlayerContoller.onRecord();
            }
        });

        mPlayButton = (Button) findViewById(R.id.playing_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMediaPlayerContoller.onPlay();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_ID: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("Home", "Permission Granted");
                    finish();
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStartPlaying() {
        mPlayButton.setText(R.string.stop_text);
        mRecordButton.setEnabled(false);
    }

    @Override
    public void onStopPlaying() {
        mPlayButton.setText(R.string.start_play_text);
        mRecordButton.setEnabled(true);

    }

    @Override
    public void onStartRecording() {
        mRecordButton.setText(R.string.stop_text);
        mPlayButton.setEnabled(false);
    }

    @Override
    public void onStopRecording() {
        mRecordButton.setText(R.string.start_record_text);
        mPlayButton.setEnabled(true);
    }

    private void showDialog()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RecordActivity.this);

        builderSingle.setTitle("Select audio source");


        builderSingle.setAdapter(new AudioSourcesAdapter(this, R.layout.audio_source_card, iMediaPlayerContoller.getAudioSources()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iMediaPlayerContoller.setAudioSource(AudioSourceE.fromInteger(which));

            }
        });
        builderSingle.show();
    }

}
