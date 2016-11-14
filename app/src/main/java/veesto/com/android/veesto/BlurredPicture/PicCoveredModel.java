package veesto.com.android.veesto.BlurredPicture;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by erez on 13/11/2016.
 */
public class PicCoveredModel
{
    private final int numOfPointsHorizontal = 5;
    private final int numOfPointsVertical = 5;
    private final int numOfPoints = numOfPointsHorizontal * numOfPointsVertical;
    private int numOfPointsCovered = 0;
    private WeakReference<ProgressListener> listener;
    private ArrayList<MyPoint> points;
    private Executor executor;

    public PicCoveredModel()
    {
        executor = Executors.newFixedThreadPool(5);
        listener = new WeakReference<ProgressListener>(null);
    }

    public void setDimensions(double h, double w)
    {
        double interValY = h / numOfPointsHorizontal;
        double interValX = w / numOfPointsVertical;
        points = new ArrayList<>();
        int currentY = 0;
        int currentX = 0;
        for(int i = 0 ; i < numOfPointsHorizontal; i++, currentY += interValY)
        {
            for(int j = 0 ; j < numOfPointsVertical; j++, currentX += interValX)
            {
                MyPoint myPoint = new MyPoint(currentX, currentY);
                points.add(myPoint);
            }
            currentX = 0;
        }
    }

    public void checkPoint(final double x,final double y , final double radius)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < points.size() ; i++)
                {
                    MyPoint point = points.get(i);
                    if(point.x < x + radius && point.x > x - radius && point.y < y + radius && point.y > y - radius)
                    {
                        if(! point.covered)
                        {
                            ++numOfPointsCovered;
                            point.covered = true;
                        }

                    }
                }
                ProgressListener progressListener = listener.get();
                if(progressListener != null)
                {
                    progressListener.onProgress(100 * numOfPointsCovered / numOfPoints);
                }
                Log.d("model", "checkPoint " + 100 * numOfPointsCovered / numOfPoints);
            }
        });

    }

    public void setListener(ProgressListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    public interface ProgressListener
    {
        void onProgress(int percent);
    }

    private class MyPoint
    {
        private double x;
        private double y;
        private boolean covered;

        public MyPoint(double x,double y)
        {
            this.x = x;
            this.y = y;
            covered = false;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean getIsCovered()
        {
            return covered;
        }

        public void setCovered(boolean covered) {
            this.covered = covered;
        }
    }
}
