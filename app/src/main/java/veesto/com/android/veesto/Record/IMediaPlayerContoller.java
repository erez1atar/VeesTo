package veesto.com.android.veesto.Record;

import java.util.ArrayList;

/**
 * Created by erez on 10/11/2016.
 */
public interface IMediaPlayerContoller
{
    void onRecord();
    void onPlay();

    void setAudioSource(AudioSourceE source);

    ArrayList<String> getAudioSources();

    void setPresentor(IRecordPresentor presentor);
}
