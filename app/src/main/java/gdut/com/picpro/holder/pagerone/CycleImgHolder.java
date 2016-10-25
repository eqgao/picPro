package gdut.com.picpro.holder.pagerone;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import gdut.com.picpro.R;
import gdut.com.picpro.customs.AutoScrollViewPager;
import gdut.com.picpro.customs.ImageCycleAdapter;
import gdut.com.picpro.model.pagerone.HomeItem;

/**
 * Created by lchua on 2016/10/24.
 * <p>
 * 描述：
 */

public class CycleImgHolder {
    AutoScrollViewPager viewPager;
    ImageCycleAdapter adapter = null;

    public CycleImgHolder(View view) {
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.auto_viewpager);
    }

    public void refreshUI(Context context, HomeItem homeItem) {
        int len = homeItem.getResd().length;
        List<ImgData> al = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            al.add(new ImgData(homeItem.getResd()[i]));
        }

        if (viewPager != null && adapter == null) {
            adapter = new ImageCycleAdapter<ImgData>(context, al) {
                @Override
                public void loadImg(final ImageView img, int position, final ImgData data) {
                    ImageLoader.getInstance().displayImage("drawable://" + data.resd,
                            img);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            };
            viewPager.setAdapter(adapter);
        } else if (viewPager != null && adapter != null) {
            adapter.notifyData(al);
        }
    }

    class ImgData {
        public ImgData(int r) {
            this.resd = r;
        }

        int resd;
    }

}
