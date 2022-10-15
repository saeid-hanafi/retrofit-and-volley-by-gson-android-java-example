package com.example.connecttoservertest;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonRequests<T> extends Request<T> {
    private static final String TAG = "GsonRequests";
    private T finalResponse;
    private Type type;
    private Response.Listener listener;
    private Gson gson = new Gson();
    private JSONObject body;
    private Map<String, String> headers;

    public GsonRequests(int method, Type type, String url, @Nullable JSONObject body, @Nullable Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, @Nullable Map<String, String> headers) {
        super(method, url, errorListener);
        this.type = type;
        this.listener = listener;
        this.body = body;
        this.headers = headers;
    }

    public GsonRequests(int method, Type type, String url, @Nullable Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener, @Nullable Map<String, String> headers) {
        this(method, type, url, null, listener, errorListener, headers);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String responseInString = new String(response.data);
        try {
            JSONObject jsonObject = new JSONObject(responseInString);
            if (jsonObject.has("data")) {
                JSONArray studentData = jsonObject.getJSONArray("data");
                finalResponse = gson.fromJson(studentData.toString(), type);
            }else{
                finalResponse = gson.fromJson(responseInString, type);
            }

            return Response.success(finalResponse, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (body == null)
            return super.getBody();
        else
            return body.toString().getBytes();
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (headers == null)
            return super.getParams();
        else
            return headers;
    }
}
