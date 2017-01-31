package com.netreadystaging.godine.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

import static com.netreadystaging.godine.activities.main.Join_GoDine.ProductVariantIDD;

public class ChoosePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);
        loadPlans();
    }

    private void loadPlans() {

        Utility.showLoadingPopup(ChoosePlanActivity.this);

        HashMap<String,String> params=new HashMap<>();
        new ServiceController(ChoosePlanActivity.this, new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data) {
                Utility.hideLoadingPopup();
                if(success)
                {

                    JSONArray jsonArray ;
                    try {
                        jsonArray = new JSONArray(data);
                        final Intent intent =  new Intent();
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObjects = jsonArray.getJSONObject(i);
                            ProductVariantIDD=jsonObjects.getInt("ProductVariantID");
                            if(ProductVariantIDD==20)
                            {
                                final Button btjustone= (Button) findViewById(R.id.bt_justone);
                                btjustone.setFocusable(false);
                                btjustone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("product_id","20");
                                        setResult(201,intent);
                                        finish();
                                    }
                                });

                            }
                            else if (ProductVariantIDD==21)
                            {
                                final Button bt_plusone= (Button) findViewById(R.id.bt_plusone);
                                bt_plusone.setFocusable(false);
                                bt_plusone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("product_id","21");
                                        setResult(201,intent);
                                        finish();
                                    }
                                });

                            }
                            else if(ProductVariantIDD==22)
                            {
                                final Button bt_plusthee= (Button) findViewById(R.id.bt_plusthee);
                                bt_plusthee.setFocusable(false);
                                bt_plusthee.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("product_id","22");
                                        setResult(201,intent);
                                        finish();
                                    }
                                });

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                else
                {
                    ErrorController.showError(getApplicationContext(),data,success);
                }
            }
        }).request(ServiceMod.ProductVariants,params);
    }

    @Override
    public void onDestroy() {
        try {
            Utility.hideLoadingPopup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
