package com.benkkstudio.bsjsonsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.benkkstudio.bsjson.API;
import com.benkkstudio.bsjson.BSJson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
        jsObj.addProperty("method_name", "HOME_SLIDER");
        BSJson bsJson = new BSJson.Builder(this)
                .setObject(jsObj)
                .setServer("")
                .load();
    }
}
