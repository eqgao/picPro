package gdut.com.picpro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gdut.com.picpro.R;
import gdut.com.picpro.holder.pagerone.CycleImgHolder;
import gdut.com.picpro.holder.pagerone.PhotoListHolder;
import gdut.com.picpro.holder.pagerone.SearchHolder;
import gdut.com.picpro.model.pagerone.HomeItem;
import gdut.com.picpro.model.pagerone.ItemType;

public class OneFragment extends Fragment {

    private static final String TAG = "OneFragment";

//    @BindView(R.id.auto_viewpager)
//    AutoScrollViewPager scrollViewPager;

    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    @BindView(R.id.listview)
    ListView listView;
    @BindView(R.id.rl_search)
    View relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, v);
        initData();
        return v;
    }

    private void initData() {
        List<HomeItem> a = new ArrayList<HomeItem>();
        HomeItem cycle = new HomeItem();
        int c[] = new int[]{R.drawable.img_imgwall_default, R.drawable.img_imgwall_error, R.drawable.img1};
        cycle.setResd(c);
        cycle.setType(ItemType.CYCLEIMG);
        a.add(cycle);


        HomeItem seacrh = new HomeItem();
        seacrh.setEdtext("呵呵");
        seacrh.setType(ItemType.REARCH);
        a.add(seacrh);


        HomeItem imgs = new HomeItem();
        imgs.setType(ItemType.ITEMPHOTO);
        imgs.setName("111");
        imgs.setImgResrouce(R.drawable.img1);
        a.add(imgs);
        HomeItem imgs1 = new HomeItem();
        imgs1.setType(ItemType.ITEMPHOTO);
        imgs1.setName("111");
        imgs1.setImgResrouce(R.drawable.img1);
        a.add(imgs1);


        HomeItem imgs2 = new HomeItem();
        imgs2.setType(ItemType.ITEMPHOTO);
        imgs2.setName("111");
        imgs2.setImgResrouce(R.drawable.img1);
        a.add(imgs2);

        HomeItem imgs3 = new HomeItem();
        imgs3.setType(ItemType.ITEMPHOTO);
        imgs3.setName("111");
        imgs3.setImgResrouce(R.drawable.img1);
        a.add(imgs3);

        HomeItem imgs4 = new HomeItem();
        imgs4.setType(ItemType.ITEMPHOTO);
        imgs4.setName("111");
        imgs4.setImgResrouce(R.drawable.img1);
        a.add(imgs4);

        listView.setAdapter(new PagerOneAdapter(getContext(), a));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    class ImgData {
        String url;
    }

    class PagerOneAdapter extends BaseAdapter {
        private Context mContext;

        private List<HomeItem> homeItemList;

        private final static int CYCLEIMG = 0;
        private final static int REARCH = 1;
        private final static int ITEMPHOTO = 2;

        public PagerOneAdapter(Context context, List<HomeItem> list) {
            this.mContext = context;
            this.homeItemList = list;
        }

        @Override
        public int getCount() {
            return homeItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return homeItemList == null ? null : homeItemList;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HomeItem homeItem = homeItemList.get(position);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            CycleImgHolder cycleImgHolder;
            SearchHolder searchHolder;
            PhotoListHolder photoListHolder;

            int type = homeItem.getType().getValue();
            switch (type) {
                case CYCLEIMG:
                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.item_recyclerview, null);
                        cycleImgHolder = new CycleImgHolder(convertView);
                        convertView.setTag(cycleImgHolder);
                    } else {
                        cycleImgHolder = (CycleImgHolder) convertView.getTag();
                    }
                    cycleImgHolder.refreshUI(mContext, homeItem);
                    break;
                case REARCH:
                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.item_search, null);
                        searchHolder = new SearchHolder(convertView);
                        convertView.setTag(searchHolder);
                    } else {
                        searchHolder = (SearchHolder) convertView.getTag();
                    }
                    searchHolder.refreshUI(homeItem);
                    break;
                case ITEMPHOTO:
                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.item_photo_list, null);
                        photoListHolder = new PhotoListHolder(convertView);
                        convertView.setTag(photoListHolder);
                    } else {
                        photoListHolder = (PhotoListHolder) convertView.getTag();
                    }
                    photoListHolder.refreshUI(homeItem);
                    break;
            }
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            if (homeItemList != null && position < homeItemList.size()) {
                return homeItemList.get(position).getType().getValue();
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }


    class ReViewHolder extends RecyclerView.ViewHolder {

        ImageView img;

        public ReViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }

}
