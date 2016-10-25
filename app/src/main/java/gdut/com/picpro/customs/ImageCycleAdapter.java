package gdut.com.picpro.customs;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import gdut.com.picpro.utils.Logger;

/**
 * Created by lchua on 2016/10/23.
 * <p>
 * 描述：
 */
public abstract class ImageCycleAdapter<T> extends PagerAdapter {

    private static final String TAG = "ImageCycleAdapter";

    private List<T> data = new ArrayList<T>();

    private Context mContext;


    public ImageCycleAdapter(Context context) {
        mContext = context;
    }

    public ImageCycleAdapter(Context context, List<T> data) {
        mContext = context;
        this.data = data;
    }

    public void notifyData(List<T> data){
        this.data = data;
        notifyDataSetChanged();
//        notifyRadio(data.size());
    }

//    public abstract void notifyRadio (int num) ;

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Logger.d(TAG, "instantiateItem-" + position);
        int mPosition = position % getRealCount();
        ImageView img = new ImageView(mContext);
        loadImg(img, mPosition, data.get(mPosition));
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        Logger.d(TAG, "destroyItem-" + position + "--" + object);
        container.removeView((View) object);
    }

    public abstract void loadImg(ImageView img, int position, T data);
}
