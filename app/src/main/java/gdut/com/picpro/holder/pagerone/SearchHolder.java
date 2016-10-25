package gdut.com.picpro.holder.pagerone;

import android.view.View;
import android.widget.EditText;

import gdut.com.picpro.R;
import gdut.com.picpro.model.pagerone.HomeItem;

/**
 * Created by lchua on 2016/10/24.
 * <p>
 * 描述：
 */

public class SearchHolder {

    private EditText editText;

    public SearchHolder(View view) {
        editText = (EditText) view.findViewById(R.id.et_search);
    }

    public void refreshUI(HomeItem homeItem) {
        editText.setText(homeItem.getEdtext());
    }
}
