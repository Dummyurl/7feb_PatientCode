package com.ziffytech.activities;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by UserBean on 10/27/2015.
 */
public class CustomRequestForString extends Request<String> {

    private Listener<String> listener;
    private Map<String, String> params;

    public CustomRequestForString(String url, Map<String, String> params, Listener<String> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomRequestForString(int method, String url, Map<String, String> params, Listener<String> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }



    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        //Log.e("#####params",params.toString());
        return params;
    };

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));


            return Response.success(new String(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            // return jsonString;

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }


}
