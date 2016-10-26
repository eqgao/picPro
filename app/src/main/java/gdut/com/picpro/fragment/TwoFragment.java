package gdut.com.picpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import gdut.com.picpro.MyApplication;
import gdut.com.picpro.R;
import gdut.com.picpro.activities.ShowImgActivity;
import gdut.com.picpro.beans.tngoubeans.TngouJsonResponse;
import gdut.com.picpro.beans.tngoubeans.Tngous;
import gdut.com.picpro.customs.ImgWallGridView;
import gdut.com.picpro.customs.ImgWallScrollView;
import gdut.com.picpro.customs.MainViewPager;
import gdut.com.picpro.interfaces.ImgWallScrollViewListener;
import gdut.com.picpro.utils.JsonDataRequestUtils;

/**
 * Created by hasee on 2016/10/16.
 */

public class TwoFragment extends Fragment {
    private ImgWallGridView mGridView;
    private ArrayList<String> mBitmapsUrl;
    private MyApplication mApplication;
    private ImageLoader mLoader;
    private String mRequestUrl = "http://www.tngou.net/tnfs/api/news?id=0&rows=10";
    private TngouJsonResponse mImgResponse;
    private JsonDataRequestUtils mDRUtils;
    private ImageWallAdapter MyAdapter;
    private Snackbar mDelImgSnackBar;
    private SwipeRefreshLayout mRefrshLayout;
    private ImgWallScrollView mScrollingView;
    private TextView mTextView;
    private ArrayList<String> mDelImgUrl;//要删除的图片地址
    private int ScreanWidth;
    private int ScreanHeight;
    private int LayoutStatus;//0为初始状态 1为带checkbox的状态
    private TranslateAnimation mTALeft;
    private TranslateAnimation mTARight;
    private ArrayList<String> mAnimateItem;//记录已播放动画的图片地址
    private int mLastItemIndex;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_two, container, false);
        mRefrshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_imgwall);
        mScrollingView = (ImgWallScrollView) view.findViewById(R.id.sv_imgwall);
        mTextView = (TextView) view.findViewById(R.id.tv_loadiing);
        MyAdapter = new ImageWallAdapter();
        mTALeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mTARight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mTALeft.setDuration(500);
        mTARight.setDuration(500);
        ScreanHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        ScreanWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        mApplication = (MyApplication) getActivity().getApplication();
        mBitmapsUrl = new ArrayList<String>();
        mDelImgUrl = new ArrayList<String>();
        mAnimateItem = new ArrayList<String>();
        mGridView = (ImgWallGridView) view.findViewById(R.id.gv_imgwall);

        mRefrshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestUrl = "http://www.tngou.net/tnfs/api/news?id=0&rows=10";
                mBitmapsUrl.clear();
                mDelImgUrl.clear();
                mAnimateItem.clear();
                mDRUtils.setUrl(mRequestUrl);
                mDRUtils.JsonRequset();
            }
        });

        mScrollingView.setOnImgWallScrollViewChangedListener(new ImgWallScrollViewListener() {
            View childView = mScrollingView.getChildAt(0);
            @Override
            public void ImgWallScrollViewChanged(ImgWallScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (childView.getMeasuredHeight() == y + scrollView.getHeight()) {
                    RequestNext();
                }
            }
        });
//        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                //滑动时不加载 静止时加载
////                if (scrollState == SCROLL_STATE_FLING) {
////                    mGetImg.getmImagesRequsetQueue().stop();
////                } else if (scrollState == SCROLL_STATE_IDLE) {
////                    mGetImg.getmImagesRequsetQueue().start();
////                }
//                if (scrollState == SCROLL_STATE_IDLE && mLastItemIndex == MyAdapter.getCount() - 1) {
//                    int size = mImgResponse.getTngou().size();
//                    String LastId = mImgResponse.getTngou().get(size - 1).getId();
//                    if (mDRUtils.getRequsetStatus() != JsonDataRequestUtils.STATUS_RESQUSET_WAIT)
//                        return;
//                    if (mImgResponse.getTotal().equals("0")) {
//                        Toast.makeText(getActivity(), "已经没有新图了，喵！", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    mRequestUrl = "http://www.tngou.net/tnfs/api/news?id=" + LastId + "&rows=10";
//                    mDRUtils.setUrl(mRequestUrl);
//                    mDRUtils.JsonRequset();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                mLastItemIndex = firstVisibleItem + visibleItemCount - 1;
//            }
//        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestNext();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (LayoutStatus == 1) {
                    CheckBox cb = (CheckBox) view.findViewById(R.id.cb_imgwall_choose);
                    cb.setChecked(!cb.isChecked());
                    if (cb.isChecked()) {
                        mDelImgUrl.add(mBitmapsUrl.get(position));
                    } else if (mDelImgUrl.contains(mBitmapsUrl.get(position))) {
                        mDelImgUrl.remove(mBitmapsUrl.get(position));
                    }
                } else {
                    Intent intent = new Intent(getActivity(), ShowImgActivity.class);
                    intent.putExtra("url", mBitmapsUrl.get(position));
                    startActivity(intent);

                }

            }
        });
        final MainViewPager mainViewPager = (MainViewPager) getActivity().findViewById(R.id.viewPager);
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (LayoutStatus == 0) {
                    LayoutStatus = 1;
                    mainViewPager.setScrollStatue(false);
                    MyAdapter.notifyDataSetChanged();
                    CoordinatorLayout cl = (CoordinatorLayout) getActivity().findViewById(R.id.cl_container);
                    mDelImgSnackBar = Snackbar.make(cl, "请选中需要删除的图片", Snackbar.LENGTH_INDEFINITE).setAction("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutStatus = 0;
                            for (String s : mDelImgUrl) {
                                if (mBitmapsUrl.contains(s)) {
                                    mBitmapsUrl.remove(s);
                                }
                                if (mAnimateItem.contains(s)) {
                                    int size = mAnimateItem.size();
                                    int index = mAnimateItem.indexOf(s);
                                    for (int i = size - 1; i > index - 1; i--) {
                                        mAnimateItem.remove(i);
                                    }
                                }
                            }
                            setFooterText();
                            MyAdapter.notifyDataSetChanged();
                            mDelImgUrl.clear();
                        }
                    });

                    mDelImgSnackBar.getView().setBackgroundColor(getResources().getColor(R.color.colorAzure));
                    //设置snackbar dismiss后的回调
                    mDelImgSnackBar.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            if (LayoutStatus == 1) {
                                LayoutStatus = 0;
                                mainViewPager.setScrollStatue(true);
                                MyAdapter.notifyDataSetChanged();
                                mDelImgUrl.clear();
                            }
                            super.onDismissed(snackbar, event);
                        }
                    });
                    mDelImgSnackBar.show();
                } else {
                    LayoutStatus = 0;
                    mainViewPager.setScrollStatue(true);
                    if (mDelImgSnackBar != null) {
                        mDelImgSnackBar.dismiss();
                    }
                    MyAdapter.notifyDataSetChanged();
                    mDelImgUrl.clear();

                }
                return true;
            }
        });

        mLoader = mApplication.getImgLoader();
        mDRUtils = new JsonDataRequestUtils(getActivity()) {
            @Override
            public void AfterResponing() {
                mRefrshLayout.setRefreshing(false);
                if (mDRUtils.getRequsetStatus() == STATUS_RESQUSET_SUCCESS) {
                    mImgResponse = this.GsonToBean(TngouJsonResponse.class);
                    if (mImgResponse.getTotal().equals("0")) {
                        Toast.makeText(getActivity(), "已经没有新图了，喵！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (Tngous t : mImgResponse.getTngou()) {
                        mBitmapsUrl.add("http://tnfs.tngou.net/img" + t.getImg());
                    }

                    MyAdapter.notifyDataSetChanged();
                }
                setFooterText();
            }
        };
        mDRUtils.setUrl(mRequestUrl);
        mDRUtils.JsonRequset();
        mGridView.setAdapter(MyAdapter);
        return view;
    }

    private void RequestNext() {
        int size;
        String LastId;
        if (mImgResponse != null) {
            size = mImgResponse.getTngou().size();
            LastId = mImgResponse.getTngou().get(size - 1).getId();
        } else {
            LastId = "0";
        }
        if (mDRUtils.getRequsetStatus() != JsonDataRequestUtils.STATUS_RESQUSET_WAIT)
            return;
        mRequestUrl = "http://www.tngou.net/tnfs/api/news?id=" + LastId + "&rows=10";
        mDRUtils.setUrl(mRequestUrl);
        mDRUtils.JsonRequset();
    }

    private void setFooterText() {
        if (mDRUtils.getRequsetStatus() == JsonDataRequestUtils.STATUS_RESQUSET_ERROR) {
            mTextView.setText("网络错误了，喵");
            return;
        }
        if (mBitmapsUrl.size() <= 4) {
            mTextView.setText("轻按继续加载，喵");
        } else {
            mTextView.setText("正在加载了，喵");
        }
    }

    class ImageWallAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mBitmapsUrl.size();
        }

        @Override
        public Object getItem(int position) {
            return mBitmapsUrl.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.fragment_two_item, null);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_imgwall_choose);
                holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.iv_imagewall_unit);
                holder.networkImageView.setDefaultImageResId(R.drawable.img_imgwall_default);
                holder.networkImageView.setErrorImageResId(R.drawable.img_imgwall_error);
                AbsListView.LayoutParams lp = (AbsListView.LayoutParams) convertView.getLayoutParams();
                if (lp == null) {
                    lp = new AbsListView.LayoutParams(ScreanWidth / 2 - 1, ScreanHeight / 3 - 1);
                    convertView.setLayoutParams(lp);
                } else {
                    lp.height = ScreanHeight / 3 - 1;
                    lp.width = ScreanWidth / 2 - 1;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (LayoutStatus == 1) {
                holder.checkBox.setVisibility(View.VISIBLE);
                if (mDelImgUrl.contains(mBitmapsUrl.get(position)))
                    holder.checkBox.setChecked(true);
                else
                    holder.checkBox.setChecked(false);
            } else {
                if (!mAnimateItem.contains(mBitmapsUrl.get(position))) {
                    mAnimateItem.add(mBitmapsUrl.get(position));
                    if (position % 2 == 0) {
                        convertView.startAnimation(mTARight);
                    } else {
                        convertView.startAnimation(mTALeft);
                    }
                }
                holder.checkBox.setVisibility(View.INVISIBLE);
            }
            holder.networkImageView.setImageUrl(mBitmapsUrl.get(position), mLoader);
            return convertView;
        }
    }

    static class ViewHolder {
        public NetworkImageView networkImageView;
        public CheckBox checkBox;
    }


}
