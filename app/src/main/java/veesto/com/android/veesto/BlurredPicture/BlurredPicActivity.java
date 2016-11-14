package veesto.com.android.veesto.BlurredPicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import veesto.com.android.veesto.R;


public class BlurredPicActivity extends AppCompatActivity implements PicCoveredModel.ProgressListener, MyClock.ClockEventListener
{
    private ProgressBar progressBar;
    private MyClock clock;
    private CircleOverlayView circleOverlayView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar_pic);
        progressBar.setProgress(0);

        circleOverlayView = (CircleOverlayView)findViewById(R.id.cicleOverlay);
        circleOverlayView.setListener(this);
        clock = new MyClock(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clock.start(5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clock.stop();
    }

    @Override
    public void onProgress(final int percent)
    {
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               progressBar.setProgress(percent);
           }
       });
    }

    @Override
    public void onTime()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BlurredPicActivity.this, "Click On Image",
                        Toast.LENGTH_LONG).show();
                circleOverlayView.setTouchable(true);
            }
        });

    }
}
