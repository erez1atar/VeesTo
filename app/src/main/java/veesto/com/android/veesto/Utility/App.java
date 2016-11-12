package veesto.com.android.veesto.Utility;

import android.app.Application;

import veesto.com.android.veesto.Data.IModel;
import veesto.com.android.veesto.Data.Model;

/**
 * Created by erez on 10/11/2016.
 */
public class App extends Application
{
    private static IModel model;
    private static App Instance;
    public App()
    {
        model = new Model();
        Instance = this;
    }

    public static App getInstance() {
        return Instance;
    }

    public static IModel getModel() {
        return model;
    }
}
