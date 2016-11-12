package veesto.com.android.veesto.Record;

/**
 * Created by erez on 10/11/2016.
 */
public interface IMediaPlayerContoller
{
    void onRecord();
    void onPlay();

    void setPresentor(IRecordPresentor presentor);
}
