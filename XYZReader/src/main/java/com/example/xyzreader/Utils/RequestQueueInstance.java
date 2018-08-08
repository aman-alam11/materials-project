package com.example.xyzreader.Utils;

import android.content.Context;
import android.net.NetworkRequest;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xyzreader.ui.main.XYZReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestQueueInstance {

    private static RequestQueueInstance mRequestQueueInstance;
    private RequestQueue mRequestQueue;
    private int INITIAL_TIMEOUT_MILLISECONDS = 3000;
    private static ArrayList<XYZReader> mArrayReaderObjects;
    private HasDataArrived mHasDataArrived;

    private RequestQueueInstance(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private RequestQueueInstance(Context context, HasDataArrived dataArrivedInterface) {
        this.mHasDataArrived = dataArrivedInterface;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static RequestQueueInstance getInstance(Context context) {
        if (mRequestQueueInstance == null) {
            mRequestQueueInstance = new RequestQueueInstance(context);
        }
        return mRequestQueueInstance;
    }


    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void commonNetworkRequest(String urlToHit) {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                urlToHit,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mArrayReaderObjects = getParsedArray(response.toString());
                        if (mHasDataArrived != null) {
                            mHasDataArrived.isDataAvailable(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.networkResponse != null) {
                            if (mHasDataArrived != null) {
                                mHasDataArrived.isDataAvailable(false);
                            }
                            Log.e("Error in response", String.valueOf(error.networkResponse.statusCode));
                        }
                    }
                }
        );


        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                INITIAL_TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        /** Start the request */
        mRequestQueueInstance.getRequestQueue().add(arrayRequest);
    }

    private static ArrayList<XYZReader> getParsedArray(String res) {
        Type responseListType = new TypeToken<ArrayList<XYZReader>>() {
        }.getType();
        return new GsonBuilder().create().fromJson(res, responseListType);
    }


    public static ArrayList<XYZReader> getCachedData(Context context, String urlString) {
        if (mArrayReaderObjects != null) {
            return mArrayReaderObjects;
        }

        RequestQueue queue = RequestQueueInstance.getInstance(context).getRequestQueue();
        if (queue.getCache() == null) {
            return null;
        }

        if (queue.getCache().get(urlString) == null) {
            return null;
        }

        // cachedData has all the data now
        // We still need to parse it
        Cache.Entry cachedData = queue.getCache().get(urlString);

        String cachedResult = new String(cachedData.data);
        return getParsedArray(cachedResult);
    }


    public interface HasDataArrived {
        void isDataAvailable(boolean isAvailable);
    }
}
