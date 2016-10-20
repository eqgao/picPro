package gdut.com.picpro.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;

import gdut.com.picpro.R;
import gdut.com.picpro.beans.tngoubeans.TngouJsonResponse;
import gdut.com.picpro.beans.tngoubeans.Tngous;
import gdut.com.picpro.utils.GetImageUtils;
import gdut.com.picpro.utils.JsonDataRequestUtils;

/**
 * Created by hasee on 2016/10/16.
 */

public class TwoFragment extends Fragment {
    private PullToRefreshGridView mGridView;
    private ArrayList<String> mBitmapsUrl;
    private GetImageUtils mGetImg;
    private ImageLoader mLoader;
    private String mRequestUrl = "http://www.tngou.net/tnfs/api/news?id=0&rows=10";
    private TngouJsonResponse mImgResponse;
    private JsonDataRequestUtils mDRUtils;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        final ImageWallAdapter MyAdapter = new ImageWallAdapter();
        mGetImg = new GetImageUtils(getActivity());
        mBitmapsUrl = new ArrayList<String>();
        mGridView = (PullToRefreshGridView) view.findViewById(R.id.gv_imgwall);
        mGridView.setMode(PullToRefreshBase.Mode.BOTH);
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mRequestUrl="http://www.tngou.net/tnfs/api/news?id=0&rows=10";
                mBitmapsUrl.clear();
                mDRUtils.setUrl(mRequestUrl);
                mDRUtils.JsonRequset();
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                int size=mImgResponse.getTngou().size();
                mRequestUrl="http://www.tngou.net/tnfs/api/news?id="+mImgResponse.getTngou().get(size-1).getId()+"&rows=10";
                mDRUtils.setUrl(mRequestUrl);
                mDRUtils.JsonRequset();
                new GetDataTask().execute();
            }
        });
        mGridView.setOnScrollListener(mGetImg);
        mLoader = mGetImg.getmLoader();
        mDRUtils = new JsonDataRequestUtils(getActivity()) {
            @Override
            public void AfterResponing() {
                mImgResponse = this.GsonToBean(TngouJsonResponse.class);
                for (Tngous t : mImgResponse.getTngou()) {
                    mBitmapsUrl.add("http://tnfs.tngou.net/img" + t.getImg());
                }
                MyAdapter.notifyDataSetChanged();
            }
        };
        mDRUtils.setUrl(mRequestUrl);
        mDRUtils.JsonRequset();
        mGridView.setAdapter(MyAdapter);
        return view;
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
                holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.iv_imagewall_unit);
                holder.networkImageView.setDefaultImageResId(R.drawable.img_imgwall_default);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //holder.networkImageView.setDefaultImageResId(R.drawable.img_imgwall_default);
            // holder.networkImageView.setErrorImageResId(R.drawable.img_imgwall_error);
            holder.networkImageView.setImageUrl(mBitmapsUrl.get(position), mLoader);
            return convertView;


       }
    }
    static class ViewHolder {
        public NetworkImageView networkImageView;
    }
    private class GetDataTask extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... params) {
            while(mDRUtils.getRequsetStatus()==JsonDataRequestUtils.STATUS_RESQUSET_ING){

            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mGridView.onRefreshComplete();
            super.onPostExecute(integer);
        }
    }
}
