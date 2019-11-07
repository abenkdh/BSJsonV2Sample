package com.benkkstudio.bsjson;

import android.app.Activity;


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
    private RequestParams requestParams;
    private static boolean isLoading = false;

    private BSJson(Activity activity,
                   String server,
                   JsonObject jsObj,
                   RequestParams requestParams,
                   BSJsonOnSuccessListener bsJsonOnSuccessListener) {
        this.activity = activity;
        this.server = server;
        this.jsObj = jsObj;
        this.requestParams = requestParams;
        this.bsJsonOnSuccessListener = bsJsonOnSuccessListener;
        load();
    }

    private void load() {
        isLoading = true;
        if (jsObj != null) {
            Constant.client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("data", API.toBase64(jsObj.toString()));
            Constant.client.post(server, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onFiled(statusCode, responseBody, error);
                    }
                }
            });
        } else if (requestParams != null){
            Constant.client = new AsyncHttpClient();
            requestParams = new RequestParams();
            Constant.client.post(server, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onFiled(statusCode, responseBody, error);
                    }
                }
            });
        } else {
            Constant.client = new AsyncHttpClient();
            Constant.client.post(server, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onSuccess(statusCode, responseBody);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (bsJsonOnSuccessListener != null) {
                        bsJsonOnSuccessListener.onFiled(statusCode, responseBody, error);
                    }
                }
            });
        }
        isLoading = false;
    }

    public static class Builder {
        private Activity activity;
        private String server;
        private JsonObject jsObj;
        private BSJsonOnSuccessListener bsJsonOnSuccessListener;
        private RequestParams requestParams;
        public Builder(Activity activity) {
            this.activity = activity;
        }

        public BSJson.Builder setServer(String server) {
            this.server = server;
            return this;
        }

        public BSJson.Builder setObject(JsonObject jsObj) {
            this.jsObj = jsObj;
            return this;
        }

        public BSJson.Builder setObject(RequestParams requestParams) {
            this.requestParams = requestParams;
            return this;
        }


        public BSJson.Builder setListener(BSJsonOnSuccessListener bsJsonOnSuccessListener) {
            this.bsJsonOnSuccessListener = bsJsonOnSuccessListener;
            return this;
        }

        public BSJson load() {
            return new BSJson(activity, server, jsObj, requestParams, bsJsonOnSuccessListener);
        }
    }

    public static void cancelRequest() {
        if (isLoading){
            Constant.client.cancelAllRequests(true);
        }
    }
}
