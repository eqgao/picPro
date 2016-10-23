package gdut.com.picpro.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

/**
 * Created by helloworld on 2016/10/18.
 */

public abstract class JsonDataRequestUtils {
    private Context context;
    private RequestQueue mQueue;
    private String url;
    private Response.Listener<JSONObject> mlistener;
    private Response.ErrorListener mErrorListener;
    private JSONObject ResponseJson;
    private int RequsetStatus=3;
    public static final int STATUS_RESQUSET_ING=1;
    public static final int STATUS_RESQUSET_ERROR=0;
    public static final int STATUS_RESQUSET_SUCCESS=2;
    public static final int STATUS_RESQUSET_WAIT=3;
    public JsonDataRequestUtils(final Context context) {
        this.context = context;
        mQueue = Volley.newRequestQueue(context);
        mlistener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setRequsetStatus(STATUS_RESQUSET_SUCCESS);
                ResponseJson = response;
                AfterResponing();
                setRequsetStatus(STATUS_RESQUSET_WAIT);
            }
        };
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRequsetStatus(STATUS_RESQUSET_ERROR);
                AfterResponing();
                Toast.makeText(context, "Internet Error", Toast.LENGTH_SHORT).show();
                setRequsetStatus(STATUS_RESQUSET_WAIT);
            }
        };

    }

    public String getUrl() {
        return url;
    }

    public int getRequsetStatus() {
        return RequsetStatus;
    }

    public void setRequsetStatus(int requsetStatus) {
        RequsetStatus = requsetStatus;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResponseJson(JSONObject responseJson) {
        ResponseJson = responseJson;
    }

    public JSONObject getResponseJson() {
        return ResponseJson;
    }

    public void JsonRequset(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        mQueue.add(request);
    }

    public void JsonRequset() {
        RequsetStatus=STATUS_RESQUSET_ING;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, mlistener, mErrorListener);
        mQueue.add(request);
    }

    public <T> T GsonToBean(Class<T> cls) {
        T t = null;
        if (ResponseJson != null) {
            Gson gson = new Gson();
            t = gson.fromJson(ResponseJson.toString(),cls);
        }
        return t;
    }
    public <T> T GsonToBean(JSONObject responseJson,Class<T> cls) {
        T t = null;
        if (responseJson != null) {
            Gson gson = new Gson();
            t = gson.fromJson(responseJson.toString(), cls);
        }
        return t;
    }

    public abstract void AfterResponing();

}
