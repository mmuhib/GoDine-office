package com.netreadystaging.godine.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.adapters.ListViewAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.BillingHistory;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

/**
 * Created by sony on 19-07-2016.
 */
public class BillingPageFragment extends Fragment
{
    ListView listView;
    View view ;
    ArrayList<BillingHistory> billingHistories;
    String[] date,paid,status;
    AppGlobal appGlobal=AppGlobal.getInatance();
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.billing_page_fragment,container,false);

        setupToolBar();
        setuplist();

        return view ;
    }

    private void setuplist() {
        listView= (ListView) view.findViewById(R.id.billinglist);

        date=getResources().getStringArray(R.array.States);
        paid=getResources().getStringArray(R.array.paid);
        status=getResources().getStringArray(R.array.Status);

        billingHistories=new ArrayList<>();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadBillingHistory();

    }

    private void loadBillingHistory() {

        final ListViewAdapter arrayAdapter=new ListViewAdapter(getActivity(),R.layout.billing_custom_row,billingHistories);
        listView.setAdapter(arrayAdapter);
        HashMap<String,String> params =new HashMap<>();
        params.put("UserId",appGlobal.getUserId()+"");
        Utility.showLoadingPopup(getActivity());
        new ServiceController(getActivity(), new HttpResponseCallback() {
            @Override
            public void response(boolean success, boolean fail, String data)
            {
                if(success)
                {
                    Utility.hideLoadingPopup();
                    JSONArray jsonArray=null;
                    try {
                        jsonArray = new JSONArray(data);

                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObjects = jsonArray.getJSONObject(i);
                            BillingHistory bill=new BillingHistory();
                            bill.setOrderDate(jsonObjects.getString("OrderDate"));
                            bill.setTotalAmount(jsonObjects.getString("TotalAmount"));
                            bill.setSalesPaymentStatus(jsonObjects.getString("SalesPaymentStatus"));
                            bill.setShipingStatus(jsonObjects.getString("ShipingStatus"));
                            bill.setSalesOrderNumber(jsonObjects.getString("SalesOrderNumber"));
                            billingHistories.add(bill);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    ErrorController.showError(getActivity(),data,success);
                }


            }
        }).request(ServiceMod.ORDERS_BY_USER,params);
    }

    private void setupToolBar() {
        Activity activity = getActivity();
        Toolbar toolBar  =  (Toolbar) activity.findViewById(R.id.toolbar) ;
        toolBar.setVisibility(View.VISIBLE);
        ImageView ivToolBarNavigationIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolBar.findViewById(R.id.ivToolBarBack) ;
        ImageView ivToolBarEndIcn = (ImageView)toolBar.findViewById(R.id.ivToolBarEndIcn) ;
        title = (TextView) toolBar.findViewById(R.id.tvToolBarMiddleLabel);
        ivToolBarNavigationIcn.setVisibility(View.VISIBLE);
        ivToolBarBack.setVisibility(View.GONE);
        ivToolBarEndIcn.setVisibility(View.GONE);

        FrameLayout  bottomToolBar = (FrameLayout)activity.findViewById(R.id.bottomToolBar) ;
        bottomToolBar.setVisibility(View.GONE);

        ((MainPageActivity)getActivity()).leftCenterButton.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
       title.setText("Billing History");
    }
}
