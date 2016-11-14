package veesto.com.android.veesto.BlurredPicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import veesto.com.android.veesto.R;


public class BlurredPicActivity extends AppCompatActivity implements PicCoveredModel.ProgressListener, IBlurredPicPresentor
{
    private ProgressBar progressBar;
    private IBlurredPicController controller;
    private BlurredView blurredView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar_pic);
        progressBar.setProgress(0);

        blurredView = (BlurredView)findViewById(R.id.cicleOverlay);
        blurredView.setListener(this);
        controller = new BlurredPicController(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.onStop();
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
    public void onReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BlurredPicActivity.this, "Click On Image",
                        Toast.LENGTH_LONG).show();
                blurredView.setTouchable(true);
            }
        });
    }
}
