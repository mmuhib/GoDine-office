package com.netreadystaging.godine.controllers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sony on 22-07-2016.
 */
public class ErrorController {

    public static void showError(Context context,String data,boolean success)
    {
        if(success) {
            try {

                JSONObject jObj = new JSONObject(data);
                JSONArray jArray = jObj.getJSONArray("errors");
                Toast.makeText(context, jArray.getString(0), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
        }
    }



}
