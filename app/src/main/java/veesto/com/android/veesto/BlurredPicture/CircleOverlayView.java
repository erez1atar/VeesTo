package veesto.com.android.veesto.BlurredPicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import veesto.com.android.veesto.R;
import veesto.com.android.veesto.Utility.App;


/**
 * Created by erez on 13/11/2016.
 */
public class CircleOverlayView extends View {
    private Bitmap bitmap;
    private Canvas osCanvas;
    private PicCoveredModel model;
    private Paint circlePaint;
    private boolean touchable;

    public CircleOverlayView(Context context) {
        super(context);
        init();
    }

    public CircleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void setListener(PicCoveredModel.ProgressListener listener)
    {
        model.setListener(listener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        model.setDimensions(getHeight(), getWidth());

    }

    private void init()
    {
        model = new PicCoveredModel();
        circlePaint = new Paint();
        circlePaint.setColor(Color.TRANSPARENT);
        circlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        circlePaint.setAntiAlias(true);
        touchable = false;
    }

    public void setTouchable(boolean touchable)
    {
        this.touchable = touchable;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    protected void createWindowFrame()
    {
        // put a blurred pic on canvas
        bitmap = BlurBuilder.blur(App.getInstance(), BitmapFactory.decodeResource(App.getInstance().getResources(), R.drawable.dog));
        bitmap = Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
        osCanvas = new Canvas(bitmap);

    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(! touchable)
        {
            return true;
        }
        osCanvas.drawCircle(event.getX(), event.getY(), 100, circlePaint);
        draw(osCanvas);
        model.checkPoint(event.getX(), event.getY(), 100);
        invalidate();
        return true;
    }
}
