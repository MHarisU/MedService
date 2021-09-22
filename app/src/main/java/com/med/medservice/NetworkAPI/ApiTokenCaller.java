package com.med.medservice.NetworkAPI;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.med.medservice.Utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class ApiTokenCaller {

    String URL_Link;
    Context mContext;


    public interface AsyncApiResponse {
        void processFinish(String response);
    }

    public AsyncApiResponse delegate = null;

    public ApiTokenCaller(Context mContext, String URL_Link, ApiTokenCaller.AsyncApiResponse delegate){
        this.mContext = mContext;
        this.delegate = delegate;
        this.URL_Link = URL_Link;

        callApi();
    }


    private void callApi() {





        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        delegate.processFinish(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("api_error", error.toString());


                    }
                }) {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + new SessionManager(mContext).getToken();
                headers.put("Authorization", auth);
                return headers;
            }

        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


}
