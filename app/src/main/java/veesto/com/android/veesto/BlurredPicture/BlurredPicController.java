package veesto.com.android.veesto.BlurredPicture;

import java.lang.ref.WeakReference;


/**
 * Created by erez on 14/11/2016.
 */
public class BlurredPicController implements IBlurredPicController,MyClock.ClockEventListener {

    private WeakReference<IBlurredPicPresentor> presentorWeak;
    private MyClock clock;

    public BlurredPicController(IBlurredPicPresentor presentor)
    {
        this.presentorWeak = new WeakReference<>(presentor);
        clock = new MyClock(this);
    }
    @Override
    public void onStart() {
        clock.start(5);
    }

    @Override
    public void onStop() {
        clock.stop();
    }

    @Override
    public void onTime() {
        IBlurredPicPresentor presentor = presentorWeak.get();
        if(presentor != null)
        {
            presentor.onReady();
        }
    }
}
