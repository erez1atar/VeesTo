package veesto.com.android.veesto.Record;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by erez on 10/11/2016.
 */
public class MediaPlayerController implements IMediaPlayerContoller,MediaPlayer.OnCompletionListener
{
    private MediaRecorder mRecorder = null;
    private static final String[] sources = {"DEFAULT", "MIC", "VOICE_UPLINK", "VOICE_DOWNLINK", "VOICE_CALL" , "CAMCORDER"
            , "VOICE_RECOGNITION", "VOICE_COMMUNICATION", "REMOTE_SUBMIX", "UNPROCESSED"};
    private MediaPlayer mPlayer = null;
    private WeakReference<IRecordPresentor> iPresentorWeak;

    boolean mStartRecording = false;
    private static String mFileName = null;
    boolean mStartPlaying = false;
    private int audioSource;

    public MediaPlayerController()
    {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }



    private void startPlaying() {
        if(mStartPlaying || mStartRecording)
        {
            return;
        }
        mStartPlaying = true;
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        try
        {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        }
        catch (IOException e)
        {
            Log.e("", "prepare() failed");
        }
        IRecordPresentor presentor = iPresentorWeak.get();
        if(presentor != null)
        {
            presentor.onStartPlaying();
        }
    }

    private void stopPlaying() {
        if(! mStartPlaying)
        {
            return;
        }
        mPlayer.release();
        mPlayer = null;
        mStartPlaying = false;
        IRecordPresentor presentor = iPresentorWeak.get();
        if(presentor != null)
        {
            presentor.onStopPlaying();
        }
    }

    private void startRecording() {
        if(mStartPlaying || mStartRecording)
        {
            return;
        }
        mStartRecording = true;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(audioSource);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            mRecorder.prepare();
        }
        catch (IOException e)
        {
            Log.e("", "prepare() failed");
        }

        mRecorder.start();
        IRecordPresentor presentor = iPresentorWeak.get();
        if(presentor != null)
        {
            presentor.onStartRecording();
        }
    }

    private void stopRecording() {
        if(! mStartRecording)
        {
            return;
        }
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mStartRecording = false;
        IRecordPresentor presentor = iPresentorWeak.get();
        if(presentor != null)
        {
            presentor.onStopRecording();
        }
    }

    @Override
    public void onRecord()
    {
        if (! mStartRecording)
        {
            startRecording();
        }
        else
        {
            stopRecording();
        }
    }

    @Override
    public void onPlay() {
        if (! mStartPlaying)
        {
            startPlaying();
        }
        else
        {
            stopPlaying();
        }
    }

    @Override
    public void setAudioSource(int source) {
        audioSource = source;
    }

    @Override
    public ArrayList<String> getAudioSources() {
        ArrayList<String> sourcesList = new ArrayList<>();
        int max = MediaRecorder.getAudioSourceMax();
        for(int i = 0 ; i < max ; i++)
        {
            sourcesList.add(sources[i]);
        }
        return sourcesList;
    }

    @Override
    public void setPresentor(IRecordPresentor presentor) {
        iPresentorWeak = new WeakReference<>(presentor);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopPlaying();
    }

}
