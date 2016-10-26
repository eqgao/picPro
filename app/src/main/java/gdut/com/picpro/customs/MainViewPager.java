package gdut.com.picpro.customs;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by helloworld on 2016/10/25.
 */

public class MainViewPager extends ViewPager {
    private boolean ScrollStatue = true;

    public boolean isScrollStatue() {
        return ScrollStatue;
    }

    public void setScrollStatue(boolean scrollStatue) {
        ScrollStatue = scrollStatue;
    }

    public MainViewPager(Context context) {
        super(context);
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!ScrollStatue) {
            return false;
        }
         return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!ScrollStatue) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
