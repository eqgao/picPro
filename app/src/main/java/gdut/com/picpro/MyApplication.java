package gdut.com.picpro;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import gdut.com.picpro.beans.BitmapCache;

/**
 * Created by hasee on 2016/10/15.
 */

public class MyApplication extends Application {

    private com.android.volley.toolbox.ImageLoader mLoader;
    private RequestQueue mImagesRequsetQueue;

    public com.android.volley.toolbox.ImageLoader getImgLoader() {
        if(mLoader==null){
            mImagesRequsetQueue = getImagesRequsetQueue();
            mLoader = new com.android.volley.toolbox.ImageLoader(mImagesRequsetQueue, new BitmapCache());
        }
        return mLoader;
    }

    public RequestQueue getImagesRequsetQueue() {
        if(mImagesRequsetQueue==null){
            mImagesRequsetQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mImagesRequsetQueue;
    }
    /**
     * 是否开启日志输出,在Debug状态下开启
     * 在Release状态下关闭以提示程序性能
     */
    public final static boolean DEBUG = true;

    @Override
    public void onCreate() {

        super.onCreate();

        initImageLoader(getApplicationContext());
    }

    public void initImageLoader(Context context) {
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.icon_empty)
                // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(false) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }
}
