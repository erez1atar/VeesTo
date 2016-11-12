package veesto.com.android.veesto.BlurredImage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import veesto.com.android.veesto.R;

public class ImageActivity extends AppCompatActivity implements ClockEventListener,ProgressBarController.ProgressBarPresentor
{

    private ImageView picture;
    private MyClock myClock;
    private boolean isImageUpdated = false;
    private ProgressBar progressBar;
    private TextView waitingText;
    private ProgressBarController progressBarController;

    private boolean timePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        waitingText = (TextView)findViewById(R.id.image_text);

        progressBarController = new ProgressBarController(1,5,this);

        timePass = false;
        myClock = new MyClock(this);


        picture = (ImageView)findViewById(R.id.picture);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ball);
        picture.setImageBitmap(BlurBuilder.blur(getApplicationContext(),image));

    }

    @Override
    protected void onResume() {
        super.onResume();
        myClock.start(5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(timePass && !isImageUpdated)
        {
            Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ball);
            picture.setImageBitmap(image);
            isImageUpdated = true;
            progressBarAction();
        }
        return super.onTouchEvent(event);

    }

    private void progressBarAction()
    {
        progressBarController.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBarController.stop();
        myClock.stop();
    }

    @Override
    public void onTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ImageActivity.this, "Click On Image",
                        Toast.LENGTH_LONG).show();
                waitingText.setVisibility(View.INVISIBLE);
            }
        });

        timePass = true;

    }

    @Override
    public void displayProgress(double percent) {
        progressBar.setProgress((int)percent);
    }
}
