package gdut.com.picpro.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by helloworld on 2016/10/26.
 */

public class ImgWallGridView extends GridView {
    public ImgWallGridView(Context context) {
        super(context);
    }

    public ImgWallGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImgWallGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
