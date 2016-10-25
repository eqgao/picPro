package gdut.com.picpro.holder.pagerone;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import gdut.com.picpro.R;
import gdut.com.picpro.model.pagerone.HomeItem;

/**
 * Created by lchua on 2016/10/24.
 * <p>
 * 描述：
 */

public class PhotoListHolder {
    ImageView img;
    TextView tv;

    public PhotoListHolder(View view) {
        img = (ImageView) view.findViewById(R.id.item_img);
        tv = (TextView) view.findViewById(R.id.tv_name);
    }

    public void refreshUI(HomeItem homeItem) {
        ImageLoader.getInstance().displayImage("drawable://" + homeItem.getImgResrouce(),
                img);
        tv.setText(homeItem.getName());
    }
}
