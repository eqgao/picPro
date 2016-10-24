package gdut.com.picpro;

import android.app.Application;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import gdut.com.picpro.beans.BitmapCache;


/**
 * Created by hasee on 2016/10/15.
 */

public class MyApplication extends Application {
    private ImageLoader mLoader;
    private RequestQueue mImagesRequsetQueue;

    public ImageLoader getImgLoader() {
        if(mLoader==null){
            mImagesRequsetQueue = getImagesRequsetQueue();
            mLoader = new ImageLoader(mImagesRequsetQueue, new BitmapCache());
        }
        return mLoader;
    }

    public RequestQueue getImagesRequsetQueue() {
        if(mImagesRequsetQueue==null){
            mImagesRequsetQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mImagesRequsetQueue;
    }
}
