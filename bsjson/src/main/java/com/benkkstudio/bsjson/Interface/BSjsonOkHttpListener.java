package com.benkkstudio.bsjson.Interface;

public interface BSjsonOkHttpListener {
    void onStart();
    void onSuccess(String responseBody);
    void onFailed(String error);
    void onEnd();
}
