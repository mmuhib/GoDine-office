package com.netreadystaging.godine.activities.onboard;

import android.content.Intent;
import android.support.design.widget.Snackbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.main.GoDineRestaurantSearchActivity;
import com.netreadystaging.godine.activities.main.Join_GoDine;
import com.netreadystaging.godine.activities.main.MainPageActivity;
import com.netreadystaging.godine.activities.main.WhyJoinActivity;
import com.netreadystaging.godine.controllers.ServiceController;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.ServiceMod;
import com.netreadystaging.godine.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technobuff.helper.http.HttpResponseCallback;

public class LoginActivity extends AppBaseActivity {

    AppGlobal appGlobal = AppGlobal.getInatance() ;
    CheckBox rememberpass;
    EditText etUsername;
    EditText etPassword;
    String user;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appGlobal.context =  getApplicationContext() ;
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        final CheckBox cbxRememberMe= (CheckBox) findViewById(R.id.cbxRememberMe);
        if(appGlobal.isRemember())
        {
            cbxRememberMe.setChecked(true);
        }
        etUsername.setText(appGlobal.u_remember);
        etPassword.setText(appGlobal.p_remember);
        Button btnLoginToGoDine = (Button) findViewById(R.id.btnLoginToGoDine);
        btnLoginToGoDine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                user = etUsername.getText().toString().trim();
                pass = etPassword.getText().toString().trim();
                if (!user.isEmpty() && !pass.isEmpty()) {
                    if (Utility.checkValidEmail(user)) {

                        if(!Utility.isNetworkConnected(getApplicationContext()))
                        {
                            Toast.makeText(LoginActivity.this, "No Network Connection!", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        Utility.showLoadingPopup(LoginActivity.this);
                        HashMap<String,String> params =  new  HashMap<>();
                        params.put("username",user);
                        params.put("password",pass);
                        params.put("DeviceId","fa9sf8ansdf932492314234kdfabhshf_kajfdfj93343");
                        params.put("DeviceType","ANDROID");

                        new ServiceController(getApplicationContext(), new HttpResponseCallback() {
                            @Override
                            public void response(boolean success, boolean fail, String data) {
                                Utility.hideLoadingPopup();
                                if(success) {
                                    JSONArray jsonArray = null;
                                    try {
                                        jsonArray = new JSONArray(data);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                            int userId = jsonObjects.getInt("UserId");
                                            String username = jsonObjects.getString("Username");
                                            String firstName = jsonObjects.getString("FirstName");
                                            String lastName = jsonObjects.getString("LastName");
                                            String mobile = jsonObjects.getString("Mobile");
                                            String telephone = jsonObjects.getString("Telephone");
                                            String emailId = jsonObjects.getString("Email");
                                            String address = jsonObjects.getString("Address");
                                            String city = jsonObjects.getString("City");
                                            String state = jsonObjects.getString("State");
                                            String zipcode = jsonObjects.getString("ZipCode");
                                            String mobileNotification = jsonObjects.getString("MobileNotification");
                                            String miles = jsonObjects.getString("Miles");
                                            int membershipId = jsonObjects.getInt("MembershipId");
                                            String memberType = jsonObjects.getString("MembershipType");
                                            String memberExpiryDate = jsonObjects.getString("MemberExpiryDate");
                                            String memberSince = jsonObjects.getString("MemberSince");
                                            String parentRestaurantIdOrUserId = jsonObjects.getString("ParentRestaurantIdOrUserId");
                                            String isVerificationImageUploaded = jsonObjects.getString("IsVerificationImageUploaded");

                                            /***********************************
                                             * INITIALISE APP GLOBAL PARAM
                                             * **********************************/
                                            appGlobal.setUserId(userId) ;
                                            appGlobal.setUsername(username) ;
                                            appGlobal.setPassword(pass) ;
                                            appGlobal.setFirstName(firstName) ;
                                            appGlobal.setLastName(lastName);
                                            appGlobal.setMobile(mobile );
                                            appGlobal.setTelephone(telephone);
                                            appGlobal.setEmailId(emailId);
                                            appGlobal.setAddress(address);
                                            appGlobal.setCity(city) ;
                                            appGlobal.setState(state);
                                            appGlobal.setZipcode(zipcode);
                                            appGlobal.setMobileNotification(mobileNotification);
                                            appGlobal.setMiles(miles);
                                            appGlobal.setMembershipId(membershipId);
                                            appGlobal.setMemberType(memberType);
                                            appGlobal.setMemberExpiryDate(memberExpiryDate); ;
                                            appGlobal.setMemberSince(memberSince); ;
                                            appGlobal.setParentRestaurantIdOrUserId(parentRestaurantIdOrUserId);
                                            appGlobal.setIsVerificationImageUploaded(isVerificationImageUploaded);
                                            appGlobal.welcomeText = "Welcome" ;

                                            final CheckBox cbxRememberMe= (CheckBox) findViewById(R.id.cbxRememberMe);
                                            appGlobal.setRemember(cbxRememberMe.isChecked());

                                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (JSONException e) {

                                        Toast.makeText(LoginActivity.this, "Error Getting Data!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Error Getting Data!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).request(ServiceMod.LOGIN,params);
                    } else {
                        Toast.makeText(LoginActivity.this, "Please Enter valid Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (user.isEmpty() && pass.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please Enter username and Password.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (user.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                        }
                        if (pass.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }



    public void goToForgotPswrd(View view) {
//        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mainActivity);
//        linearLayout.setBackgroundResource(R.drawable.login_bg_blur);

        FrameLayout forgotFormLayout = (FrameLayout) findViewById(R.id.forgotPasswordFormId);
        LinearLayout loginFormLayout = (LinearLayout) findViewById(R.id.loginFormId);


        forgotFormLayout.setVisibility(View.VISIBLE);
        loginFormLayout.setVisibility(View.INVISIBLE);

        final EditText etFrgtEmailId = (EditText) forgotFormLayout.findViewById(R.id.etFrgtEmailId) ;

        Button btnFrgtPassword  = (Button) forgotFormLayout.findViewById(R.id.btnFrgtPassword) ;
        btnFrgtPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String emailId =   etFrgtEmailId.getText().toString().trim() ;
                if(!emailId.isEmpty())
                {
                    if(Utility.checkValidEmail(emailId))
                    {
                        if(!Utility.isNetworkConnected(getApplicationContext()))
                        {
                            // Toast.makeText(LoginActivity.this, "No Network Connection!", Toast.LENGTH_SHORT).show();
                            Snackbar.make(view,"No Network Connection!",Snackbar.LENGTH_LONG).show();
                            return ;
                        }

                        Utility.showLoadingPopup(LoginActivity.this);
                        HashMap<String,String> params =  new  HashMap<>();
                        params.put("Email",emailId);
                        new ServiceController(getApplicationContext(), new HttpResponseCallback() {
                            @Override
                            public void response(boolean success, boolean fail, String data) {
                                Utility.hideLoadingPopup();
                                if(success)
                                {
                                    try {
                                        JSONArray jsonArray =  new JSONArray(data);

                                        for (int indexJObj=0 ;indexJObj<jsonArray.length();indexJObj++)
                                        {
                                            JSONObject jObj  =  jsonArray.getJSONObject(indexJObj) ;
                                            if(jObj!=null)
                                            {
                                                Snackbar.make(view,jObj.getString("Message"),Snackbar.LENGTH_LONG).show();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //Toast.makeText(LoginActivity.this, "Password recovered SuccessFully!", Toast.LENGTH_SHORT).show();

                                    closeForgotForm(view);

                                }
                            }
                        }).request(ServiceMod.FORGOT_PASSWORD,params);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Enter Valid E-mail id!",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter E-mail id!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void closeForgotForm(View view) {
//        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mainActivity);
//        linearLayout.setBackgroundResource(R.drawable.login_bg);

        FrameLayout forgotFormLayout = (FrameLayout) findViewById(R.id.forgotPasswordFormId);
        LinearLayout loginFormLayout = (LinearLayout) findViewById(R.id.loginFormId);

        forgotFormLayout.setVisibility(View.INVISIBLE);
        loginFormLayout.setVisibility(View.VISIBLE);

    }

    public void goToWhyJoin(View view) {
        try {
            Intent intent = new Intent(LoginActivity.this, WhyJoinActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in,R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public void goToRestaurantSearch(View view) {
        try {
            Intent intent = new Intent(LoginActivity.this, GoDineRestaurantSearchActivity.class);
            intent.putExtra("From","Login");
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
           }
    public void  signupGoDine(View view)
    {
        try {
            Intent intent = new Intent(LoginActivity.this, Join_GoDine.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
