package gdut.com.picpro.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import gdut.com.picpro.interfaces.ImgWallScrollViewListener;

/**
 * Created by helloworld on 2016/10/26.
 */

public class ImgWallScrollView extends ScrollView {
    private ImgWallScrollViewListener mListener=null;
    public ImgWallScrollView(Context context) {
        super(context);
    }

    public ImgWallScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgWallScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnImgWallScrollViewChangedListener(ImgWallScrollViewListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(mListener!=null)
                mListener.ImgWallScrollViewChanged(this, l, t, oldl, oldt);

        super.onScrollChanged(l, t, oldl, oldt);
    }
}
