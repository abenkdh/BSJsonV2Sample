package com.benkkstudio.bsjson;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.benkkstudio.bsjson.Interface.BSJsonOnSuccessListener;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class BSJson {
    private Activity activity;
    private String server;
    private JsonObject jsObj;
    private BSJsonOnSuccessListener bsJsonOnSuccessListener;

    private BSJson(@NonNull Activity activity,
                       @NonNull String server,
                       @NonNull JsonObject jsObj,
                       @NonNull BSJsonOnSuccessListener bsJsonOnSuccessListener) {
        this.activity = activity;
        this.server = server;
        this.jsObj = jsObj;
        this.bsJsonOnSuccessListener = bsJsonOnSuccessListener;
        load();
    }

    private void load(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("data", API.toBase64(jsObj.toString()));
        client.post(server, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (bsJsonOnSuccessListener != null) {
                    bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public static class Builder {
        private Activity activity;
        private String server;
        private JsonObject jsObj;
        private BSJsonOnSuccessListener bsJsonOnSuccessListener;

        public Builder(@NonNull Activity activity) {
            this.activity = activity;
        }
        @NonNull
        public BSJson.Builder setServer(@NonNull String server) {
            this.server = server;
            return this;
        }
        @NonNull
        public BSJson.Builder setObject(@NonNull JsonObject jsObj) {
            this.jsObj = jsObj;
            return this;
        }
        @NonNull
        public BSJson.Builder setListener(@NonNull BSJsonOnSuccessListener bsJsonOnSuccessListener) {
            this.bsJsonOnSuccessListener = bsJsonOnSuccessListener;
            return this;
        }

        @NonNull
        public BSJson load() {
            return new BSJson(activity, server, jsObj, bsJsonOnSuccessListener);
        }
    }
}
