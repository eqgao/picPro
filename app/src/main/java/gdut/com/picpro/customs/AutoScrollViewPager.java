package gdut.com.picpro.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import gdut.com.picpro.R;
import gdut.com.picpro.utils.Logger;

/**
 * Created by lchua on 2016/10/21.
 */

public class AutoScrollViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "AutoScrollViewPager";

    private int currentPage;

    private int totalpage;

    @BindView(R.id.customs_viewPager)
    ViewPager viewPager;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    ImageView simg;

    private int radioDistance;

    private RadioStyle radioStyle;

    private static int RADIO_POSISTION_LEFT = 0;
    private static int RADIO_POSISTION_CENTER = 1;
    private static int RADIO_POSISTION_RIGHT = 2;

    class RadioStyle {
        float marginBottom;
        float marginTop;
        float marginLeft;
        float marginRight;

        float radioSize;
        float marginSpace;

        int position;
    }

    public AutoScrollViewPager(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.customs_cycle_image, this);
        ButterKnife.bind(this);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.customs_cycle_image, this);

        ButterKnife.bind(this);
    }

    public AutoScrollViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.customs_cycle_image, this);
        ButterKnife.bind(this);
    }


    private void initData(Context context, AttributeSet attrs) {
        radioStyle = new RadioStyle();
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.radio_style);
        radioStyle.marginBottom = types.getDimension(R.styleable.radio_style_radio_margin_bottom, 0);
        radioStyle.marginTop = types.getDimension(R.styleable.radio_style_radio_margin_top, 0);
        radioStyle.marginLeft = types.getDimension(R.styleable.radio_style_radio_margin_left, 0);
        radioStyle.marginRight = types.getDimension(R.styleable.radio_style_radio_margin_right, 0);

        radioStyle.radioSize = types.getDimension(R.styleable.radio_style_radio_size, getResources().getDimensionPixelSize(R.dimen.radio_height));
        radioStyle.marginSpace = types.getDimension(R.styleable.radio_style_radio_space, getResources().getDimensionPixelSize(R.dimen.radio_margin) / 2);

        radioStyle.position = types.getInt(R.styleable.radio_style_radio_position, 0);
        Logger.d(TAG, "radioStyle.position == " + radioStyle.position);

    }

    public void setAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        totalpage = ((ImageCycleAdapter) adapter).getRealCount();
        drawRadio(totalpage);
        viewPager.setOnPageChangeListener(this);
    }


    private void drawRadio(int num) {
        for (int i = 0; i < num; i++) {
            ImageView pointImage = new ImageView(getContext());
            pointImage.setImageResource(R.drawable.radio_unselect);
            // 设置小圆点的布局参数
            int PointSize = (int) radioStyle.radioSize;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PointSize, PointSize);
            params.leftMargin = (int) radioStyle.marginSpace;
            pointImage.setLayoutParams(params);
            linearLayout.addView(pointImage);
        }
        simg = new ImageView(getContext());
        simg.setImageResource(R.drawable.radio_select);
        // 设置小圆点的布局参数
        int PointSize = (int) radioStyle.radioSize;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(PointSize, PointSize);
        params.leftMargin = (int) radioStyle.marginSpace;
        simg.setLayoutParams(params);
        relativeLayout.addView(simg);

        RelativeLayout.LayoutParams rparam = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        rparam.topMargin = (int) radioStyle.marginTop;
        rparam.bottomMargin = (int) radioStyle.marginBottom;
        rparam.leftMargin = (int) radioStyle.marginLeft;
        rparam.rightMargin = (int) radioStyle.marginRight;
        if (radioStyle.position == RADIO_POSISTION_CENTER) {
            rparam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (radioStyle.position == RADIO_POSISTION_LEFT) {
            rparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (radioStyle.position == RADIO_POSISTION_RIGHT) {
            rparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        rparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); // 放置到底部
        relativeLayout.setLayoutParams(rparam);

        // 获得视图树的观察者, 监听全部布局的回调
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // 只执行一次, 把当前的事件从视图树的观察者中移除掉
                linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // 取出两个点之间的宽度
                radioDistance = linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int mPosition = position % totalpage;
        Logger.d(TAG, "onPageScrolled - " + position + "- " + positionOffset + "- " + positionOffsetPixels + "");
        if (mPosition + positionOffset <= totalpage - 1) { // 最后一张图片时是radio才移动
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) simg.getLayoutParams();
            int leftMargin = getResources().getDimensionPixelOffset(R.dimen.radio_margin);
            params.leftMargin = (int) ((mPosition + positionOffset) * radioDistance) + (int) radioStyle.marginSpace;
            simg.setLayoutParams(params);
        }
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(TAG, "onPageSelected - " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged - " + state);
    }
}
