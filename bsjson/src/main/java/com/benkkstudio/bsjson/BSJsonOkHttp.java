package com.benkkstudio.bsjson;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.benkkstudio.bsjson.Interface.BSJsonOnSuccessListener;
import com.benkkstudio.bsjson.Interface.BSjsonOkHttpListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

import cz.msebera.android.httpclient.Header;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BSJsonOkHttp {
    private Activity activity;
    private String server;
    private JsonObject jsObj;
    private BSjsonOkHttpListener bSjsonOkHttpListener;
    private String purchaseCode;

    private BSJsonOkHttp(Activity activity,
                   String server,
                   JsonObject jsObj,
                         BSjsonOkHttpListener bSjsonOkHttpListener,
                   String purchaseCode) {
        this.activity = activity;
        this.server = server;
        this.jsObj = jsObj;
        this.bSjsonOkHttpListener = bSjsonOkHttpListener;
        this.purchaseCode = purchaseCode;
        verifyNow();
    }

    private void verifyNow() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("code", purchaseCode);
        client.addHeader("Authorization", "Bearer 031Cm94VBFWVIwOGuyvfTcvvmvF3EM9b");
        client.addHeader("User-Agent", "Purchase code verification on benkkstudio.xyz");
        client.get("https://api.envato.com/v3/market/author/sale", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                new Loader().execute();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activity, "Your purchase code not valid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class Loader extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            bSjsonOkHttpListener.onStart();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                RequestBody requestBody = new  MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("data", API.toBase64(jsObj.toString()))
                        .build();
                bSjsonOkHttpListener.onSuccess(ParseJSON.okhttpPost(server, requestBody));
            } catch (Exception e) {
                e.printStackTrace();
                bSjsonOkHttpListener.onFailed(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            bSjsonOkHttpListener.onEnd();
            super.onPostExecute(s);
        }
    }

    public static class Builder {
        private Activity activity;
        private String server;
        private JsonObject jsObj;
        private BSjsonOkHttpListener bSjsonOkHttpListener;
        private RequestParams requestParams;
        private String purchaseCode;
        public Builder(Activity activity) {
            this.activity = activity;
        }

        public BSJsonOkHttp.Builder setServer(String server) {
            this.server = server;
            return this;
        }

        public BSJsonOkHttp.Builder setObject(JsonObject jsObj) {
            this.jsObj = jsObj;
            return this;
        }


        public BSJsonOkHttp.Builder setPurchaseCode(String purchaseCode) {
            this.purchaseCode = purchaseCode;
            return this;
        }

        public BSJsonOkHttp.Builder setListener(BSjsonOkHttpListener bSjsonOkHttpListener) {
            this.bSjsonOkHttpListener = bSjsonOkHttpListener;
            return this;
        }

        public BSJsonOkHttp load() {
            return new BSJsonOkHttp(activity, server, jsObj, bSjsonOkHttpListener, purchaseCode);
        }
    }
}
