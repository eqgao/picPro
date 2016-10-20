package gdut.com.picpro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.AbsListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by helloworld on 2016/10/17.
 */

public class GetImageUtils implements AbsListView.OnScrollListener{
    private Context context;
    private RequestQueue mImagesRequsetQueue;
    private ImageLoader mLoader;
    public GetImageUtils(Context context) {
        this.context = context;
        initData();
    }

    private void initData() {
        mImagesRequsetQueue = Volley.newRequestQueue(context);
        mLoader = new ImageLoader(mImagesRequsetQueue, new BitmapCache());


    }

    public ImageLoader getmLoader() {
        return mLoader;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_FLING){
           mImagesRequsetQueue.stop();
        }else if(scrollState==SCROLL_STATE_IDLE){
            mImagesRequsetQueue.start();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = (int) Runtime.getRuntime().maxMemory()/8;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
           // Log.i(TAG, "getBitmap: running");
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }


}
