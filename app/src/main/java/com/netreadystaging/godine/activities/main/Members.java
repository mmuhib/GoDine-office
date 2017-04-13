package com.netreadystaging.godine.activities.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.netreadystaging.godine.R;
import com.netreadystaging.godine.adapters.MemberAdapter;
import com.netreadystaging.godine.controllers.ErrorController;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.models.MemberList;
import com.netreadystaging.godine.models.Restaurant;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import in.technobuff.helper.http.HttpResponseCallback;

public class Members extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    EditText et_memberNumber,et_memberCell;
    Button bt_searchmember;
    TextView mTitle;
    ListView mlist;
    ArrayList<MemberList> memberLists;
    MemberAdapter memberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        setupToolBar();
        Locale locale=getResources().getConfiguration().locale;
        mlist= (ListView) findViewById(R.id.memberlist);
        memberLists=new ArrayList<>();

        memberAdapter=new MemberAdapter(getApplicationContext(),R.layout.member_custom_row,memberLists);
        et_memberCell= (EditText)findViewById(R.id.et_memberCell);
        et_memberCell.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mlist.setAdapter(memberAdapter);
        mlist.setOnItemClickListener(this);
        bt_searchmember= (Button) findViewById(R.id.bt_searchmember);
        bt_searchmember.setOnClickListener(this);
    }
    Toolbar toolbar_gd_member_search ;
    private void setupToolBar()
    {
        toolbar_gd_member_search = (Toolbar) findViewById(R.id.toolbar_member_search);
        mTitle = (TextView) toolbar_gd_member_search.findViewById(R.id.tvToolBarMiddleLabel) ;
        mTitle.setText(getResources().getText(R.string.topmember));
        ImageView ivToolBarNavigationIcn = (ImageView)toolbar_gd_member_search.findViewById(R.id.ivToolBarNavigationIcn) ;
        ImageView ivToolBarBack = (ImageView)toolbar_gd_member_search.findViewById(R.id.ivToolBarBack) ;
        ImageView Set= (ImageView) toolbar_gd_member_search.findViewById(R.id.ivToolBarEndIcn);
        Set.setVisibility(View.GONE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        ivToolBarBack.setVisibility(View.VISIBLE);
         }

    @Override
    public void onClick(View view) {//9495544526

        if(memberLists!=null)
        {
            memberLists.clear();
        }
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(bt_searchmember.getWindowToken(),0);
        et_memberNumber= (EditText) findViewById(R.id.et_memberNumber);
       // et_memberCell.setText("(509) 590-6309");
        String memberNumber=et_memberNumber.getText().toString().trim();
        String memberCell=et_memberCell.getText().toString();

        if(!memberNumber.isEmpty() && !memberCell.isEmpty())
        {

           final AlertDialog.Builder builder=new AlertDialog.Builder(Members.this);
            builder.setTitle("Info");
            builder.setMessage("Please fill either Member Number or \n Member Phone");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
        else {
            Utility.showLoadingPopup(Members.this);
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId", memberNumber);
            params.put("CellNo", memberCell);

            new ServiceController(getApplicationContext(), new HttpResponseCallback() {
                @Override
                public void response(boolean success, boolean fail, String data) {
                    Utility.hideLoadingPopup();
                    if (success) {
                        Log.d("Muhib",data);
                        JSONArray jsonArray = null;
                        try {

                            jsonArray = new JSONArray(data);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                   JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                    MemberList memberList = new MemberList();
                                    memberList.setEmail(jsonObjects.getString("Email"));
                                    memberList.setFirstName(jsonObjects.getString("FirstName"));
                                    memberList.setLastName(jsonObjects.getString("LastName"));
                                    memberList.setCell(jsonObjects.getString("Cell"));
                                    memberList.setUserId(jsonObjects.getString("UserId"));
                                    memberList.setTelephone(jsonObjects.getString("Telephone"));
                                    memberLists.add(memberList);
                                    memberAdapter.notifyDataSetChanged();

                                }
                            } else {
                               // Utility.Alertbox(getApplicationContext(),"Info","Please Fill Value","OK");
                                AlertDialog.Builder builder = new AlertDialog.Builder(Members.this);
                                builder.setTitle("Info");
                                builder.setMessage("Please fill correct Member number or Cell number");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                            Utility.hideLoadingPopup();
                            memberAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ErrorController.showError(getApplicationContext(), data, success);
                    }
                }
            }).request(ServiceMod.MEMBER_SEARCH, params);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int i, long l)
    {
        String name=((TextView)v.findViewById(R.id.memberlistname)).getText().toString();
        String cell=((TextView)v.findViewById(R.id.memberlistcall)).getText().toString();
        String userid=((TextView)v.findViewById(R.id.memberid)).getText().toString();
        Intent intent=new Intent();
        intent.putExtra("Name",name);
        intent.putExtra("Cell",cell);
        intent.putExtra("UserId",userid);
        setResult(2,intent);
        finish();
    }
}
