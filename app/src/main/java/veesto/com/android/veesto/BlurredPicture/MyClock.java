package veesto.com.android.veesto.BlurredPicture;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by erez on 10/11/2016.
 */
public class MyClock
{
    private ExecutorService clockExecuter;
    private WeakReference<ClockEventListener> listenerWeakReference;

    public MyClock(ClockEventListener listener)
    {
        listenerWeakReference = new WeakReference<>(listener);
        clockExecuter = Executors.newSingleThreadExecutor();
    }

    public void start(final int numOfSeconds)
    {
        clockExecuter.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(numOfSeconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                ClockEventListener listener = listenerWeakReference.get();
                if (listener != null)
                {
                    listener.onTime();
                }
            }
        });
    }

    public void stop()
    {
        clockExecuter.shutdownNow();
    }

    public interface ClockEventListener
    {
        void onTime();
    }
}
