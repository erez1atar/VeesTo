package veesto.com.android.veesto.BlurredImage;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by erez on 12/11/2016.
 */
public class ProgressBarController
{
    private static final long NUM_OF_MILLI_IN_SEC = 1000;
    private int numOfSeconds;
    private int numOfUpdates;
    private double percent;
    private WeakReference<ProgressBarPresentor> presentorWeakReference;
    private ExecutorService progressExecuter;

    public interface ProgressBarPresentor
    {
        void displayProgress(double percent);
    }

    public ProgressBarController(int numOfSeconds, int numOfUpdates, ProgressBarPresentor presentor)
    {
        this.numOfSeconds = numOfSeconds;
        this.numOfUpdates = numOfUpdates;
        presentorWeakReference = new WeakReference<>(presentor);
        percent = 0;
        progressExecuter = Executors.newSingleThreadExecutor();

    }

    public void start()
    {
        final long millisecInterval = numOfSeconds * NUM_OF_MILLI_IN_SEC / numOfUpdates;
        final double percentInterval = 100 * numOfSeconds / numOfUpdates;
        progressExecuter.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < numOfUpdates ; i++)
                {
                    try {
                        Thread.sleep(millisecInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(Thread.currentThread().isInterrupted())
                    {
                        break;
                    }
                    percent += i * percentInterval;
                    ProgressBarPresentor presentor = presentorWeakReference.get();
                    if(presentor != null)
                    {
                        presentor.displayProgress(percent);
                    }
                }
            }
        });

    }
    public void stop()
    {
        progressExecuter.shutdownNow();
    }
}
