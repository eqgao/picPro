package gdut.com.picpro.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import gdut.com.picpro.MyApplication;
import gdut.com.picpro.R;

public class ShowImgActivity extends AppCompatActivity implements View.OnTouchListener {
    private NetworkImageView mImageView;
    private LinearLayout mMainView;
    private int mTouchMode = 1;//1:单指拖动状态 2：双指放大缩小3：单指长按


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_img);
        MyApplication myApplication = (MyApplication) getApplication();
        mMainView = (LinearLayout) findViewById(R.id.activity_show_img);
        mImageView = (NetworkImageView) findViewById(R.id.iv_show_img);
        mImageView.setImageUrl(getIntent().getStringExtra("url"), myApplication.getImgLoader());
        mImageView.setOnTouchListener(this);
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mTouchMode == 3) {
                    AlertDialog.Builder bulider = new AlertDialog.Builder(ShowImgActivity.this).setItems(new String[]{"保存图片"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mImageView.setDrawingCacheEnabled(true);
                            Bitmap image = mImageView.getDrawingCache();
                            if (image == null) {
                                Toast.makeText(ShowImgActivity.this, "无法获取图片缓存", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //new ImageDownloadTask().execute(image);
                        }
                    });
                    bulider.show();
                }

                return true;
            }
        });
    }


    ///滑动事件控制
    float CurrDistance;
    float LastDistance = -1;

    float dx;
    float dy;
    int left;
    int right;
    int top;
    int bottom;
    int oldX = 0, oldY = 0;
    int newX = 0, newY = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println(mTouchMode);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1 && mTouchMode != 2) {
                    oldX = (int) event.getRawX();
                    oldY = (int) event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    mTouchMode = 2;
                    float offsetX = event.getX(0) - event.getX(1);
                    float offsetY = event.getY(0) - event.getY(1);
                    CurrDistance = (float) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
                    if (LastDistance < 0) {
                        LastDistance = CurrDistance;
                    } else if (CurrDistance - LastDistance > 10) {
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
                        if (lp == null) {
                            lp = new LinearLayout.LayoutParams(mImageView.getWidth() + 1, mImageView.getHeight() + 1);
                        } else {
                            lp.height = (int) (mImageView.getHeight() * 1.05f);
                            lp.width = (int) (mImageView.getWidth() * 1.05f);
                        }
                        mImageView.setLayoutParams(lp);
                        LastDistance = CurrDistance;
                    } else if (LastDistance - CurrDistance > 10) {
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
                        if (lp == null) {
                            lp = new LinearLayout.LayoutParams(mImageView.getWidth() - 1, mImageView.getHeight() - 1);
                        } else {
                            lp.height = (int) (mImageView.getHeight() * 0.95f);
                            lp.width = (int) (mImageView.getWidth() * 0.95f);
                        }
                        mImageView.setLayoutParams(lp);
                        LastDistance = CurrDistance;
                    }
                } else if (event.getPointerCount() == 1 && mTouchMode != 2) {
                    newX = (int) event.getRawX();
                    newY = (int) event.getRawY();
                    dx = newX - oldX;
                    dy = newY - oldY;
                    if (Math.abs(dx) > 40 || Math.abs(dy) > 40) {
                        left = (int) (mImageView.getLeft() + dx);
                        top = (int) (mImageView.getTop() + dy);
                        bottom = top + mImageView.getHeight();
                        right = left + mImageView.getWidth();
                        mImageView.layout(left, top, right, bottom);
                        oldX = newX;
                        oldY = newY;
                        mTouchMode = 1;
                    } else if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
                        mTouchMode = 3;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchMode = 1;
                break;

        }
        return false;
    }

    class ImageDownloadTask extends AsyncTask<Bitmap, Void, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();
                File file = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/Download/Image");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File imageFile = new File(file.getAbsolutePath(), new Date().getTime() + ".jpg");
                FileOutputStream outStream = null;
                outStream = new FileOutputStream(imageFile);
                Bitmap image = params[0];
                image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("error")) {
                Toast.makeText(ShowImgActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShowImgActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStop() {
        mImageView.destroyDrawingCache();
        finish();
        super.onStop();
    }
}
