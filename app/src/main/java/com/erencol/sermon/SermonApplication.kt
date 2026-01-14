package com.erencol.sermon;

import android.app.Application;
import android.content.Context;

public class SermonApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        SermonApplication.context = getApplicationContext();
    }

    public Context getAppContext(){
        return SermonApplication.context;
    }
}
