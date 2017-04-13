package com.netreadystaging.godine.activities.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;
import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.onboard.LoginActivity;
import com.netreadystaging.godine.activities.onboard.Splash2;
import com.netreadystaging.godine.adapters.NavListViewAdapter;
import com.netreadystaging.godine.callbacks.DrawerLocker;
import com.netreadystaging.godine.fragments.Howitworks;
import com.netreadystaging.godine.fragments.ReferFriendsandFamily;
import com.netreadystaging.godine.utils.AppGlobal;
import com.netreadystaging.godine.utils.DrawerConstant;
import com.netreadystaging.godine.fragments.BillingPageFragment;
import com.netreadystaging.godine.fragments.ContactPageFragment;
import com.netreadystaging.godine.fragments.ExploreRestrauntsPageFragment;
import com.netreadystaging.godine.fragments.FavoritePageFragment;
import com.netreadystaging.godine.fragments.FeedBackPageFragment;
import com.netreadystaging.godine.fragments.OffersPageFragment;
import com.netreadystaging.godine.fragments.ProfilePageFragment;
import com.netreadystaging.godine.fragments.ProfileWelcomeFragment;
import com.netreadystaging.godine.fragments.ReviewRatingPageFragment;
import com.netreadystaging.godine.fragments.VerificationPageFragment;
import com.netreadystaging.godine.views.CFABSubIconView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

/**
 * Created by sony on 19-07-2016.
 */
public class MainPageActivity extends AppBaseActivity implements DrawerLocker {


    AppGlobal appGlobal = AppGlobal.getInatance();
    String title ;
    Toolbar toolbar ;
    FrameLayout   bottomToolBar ;
    DrawerLayout mDrawer ;
    ListView nvDrawer ;
    private ArrayList<String> listNavLabels ;
    ImageView ivToolBarNavigationIcn ,ivToolBarEndIcn ,ivToolBarBack;
    TextView tvToolBarMiddleLabel ;
    int nvDrawables[] ;
    public FloatingActionButton leftCenterButton;
    public FloatingActionMenu leftCenterMenu;
    private FrameLayout overLayBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);
        setupTopBar();
        setupBottomBar();
        setupDrawer();
    }

    private void setupBottomBar() {
        bottomToolBar = (FrameLayout)findViewById(R.id.bottomToolBar) ;
        setupOverLay();
        setupCFABMenu();
    }

    private void setupOverLay() {

        overLayBg =  new FrameLayout(this);

        overLayBg.setBackgroundColor(Color.parseColor("#80000000"));
        overLayBg.setVisibility(View.GONE);

        this.addContentView(overLayBg,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setupCFABMenu()
    {

        int redActionButtonSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_size);
        int redActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.action_button_margin);
        int redActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_size);
        int redActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.red_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        final ImageView fabIconStar = new ImageView(this);
        fabIconStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new));

        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(redActionButtonSize, redActionButtonSize);
        starParams.setMargins(redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin,
                redActionButtonMargin);
        fabIconStar.setLayoutParams(starParams);

        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(redActionButtonContentSize, redActionButtonContentSize);
        fabIconStarParams.setMargins(redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin,
                redActionButtonContentMargin);

        leftCenterButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconStar, fabIconStarParams)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .setPosition(FloatingActionButton.POSITION_BOTTOM_CENTER)
                .setLayoutParams(starParams)
                .build();

        // Set up customized SubActionButtons for the right center menu
        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
        lCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);
        lCSubBuilder.setLayoutParams(blueContentParams);
        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        lCSubBuilder.setLayoutParams(blueParams);

        CFABSubIconView layout1 =  new CFABSubIconView(this);
        layout1.setView(R.drawable.search,"Restaurants");
        CFABSubIconView layout2 =  new CFABSubIconView(this);
        layout2.setView(R.drawable.user,"Profile");
        CFABSubIconView layout3 =  new CFABSubIconView(this);
        layout3.setView(R.drawable.star,"Verification");
        CFABSubIconView layout4 =  new CFABSubIconView(this);
        layout4.setView(R.drawable.favourite,"Favorite");
        CFABSubIconView layout5 =  new CFABSubIconView(this);
        layout5.setView(R.drawable.users,"Refer Friends");

        // Sub Action Buttons Ids
        SubActionButton actionButton1 = lCSubBuilder.setContentView(layout1, blueContentParams).build() ;
        SubActionButton actionButton2 =lCSubBuilder.setContentView(layout2, blueContentParams).build();
        SubActionButton actionButton3 =lCSubBuilder.setContentView(layout3, blueContentParams).build();
        SubActionButton actionButton4 =lCSubBuilder.setContentView(layout4, blueContentParams).build();
        SubActionButton actionButton5 =lCSubBuilder.setContentView(layout5, blueContentParams).build();

        // Set Click Listener
        actionButton1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDrawerItem(DrawerConstant.EXPLORE_RESTAURANT);
            }
        });
        actionButton2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // leftCenterMenu.close(true);
                selectDrawerItem(DrawerConstant.PROFILE);
            }
        });
        actionButton3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   leftCenterMenu.close(true);
                selectDrawerItem(DrawerConstant.VERIFICATION);
            }
        });
        actionButton4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // leftCenterMenu.close(true);
                selectDrawerItem(DrawerConstant.FAVORITE);
            }
        });
        actionButton5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // leftCenterMenu.close(true);
                if(leftCenterMenu.isOpen()) {
                    ReferFriendsandFamily frag = new ReferFriendsandFamily();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent, frag).addToBackStack(null).commit();
                }
                closeCFABMenu();
            }
        });

        // Build another menu with custom options
        leftCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(actionButton1,this)
                .addSubActionView(actionButton2,this)
                .addSubActionView(actionButton3,this)
                .addSubActionView(actionButton4,this)
                .addSubActionView(actionButton5,this)
                .setRadius(redActionMenuRadius)
                .setStartAngle(190)
                .setEndAngle(350)
                .attachTo(leftCenterButton)
                .build();


        leftCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                leftCenterButton.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 135);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(leftCenterButton, pvhR);
                animation.start();

                overLayBg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                leftCenterButton.setRotation(135);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(leftCenterButton, pvhR);
                animation.start();

                overLayBg.setVisibility(View.GONE);
            }
        });


    }

   /* private View.OnClickListener menuButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            leftCenterMenu.close(true);
        }
    };*/


    private void setupDrawer() {

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer = (ListView) findViewById(R.id.nvView);
        nvDrawables = new int[] {R.drawable.nav_explore,R.drawable.nav_offer,R.drawable.nav_favourite,R.drawable.nav_rating,
                R.drawable.nav_profile,R.drawable.nav_contact,R.drawable.nav_billing,R.drawable.nav_verification,
                R.drawable.nav_feedback,R.drawable.nav_how_it_works,R.drawable.nav_got_question,R.drawable.nav_logout};

        listNavLabels  =  new ArrayList<>();

        listNavLabels.add("Explore Restaurants");
        listNavLabels.add("Offers");
        listNavLabels.add("Favorite");
        listNavLabels.add("My Reviews");
        listNavLabels.add("Profile");
        listNavLabels.add("Contact GoDine");
        listNavLabels.add("Billing History");
        listNavLabels.add("Verification");
        listNavLabels.add("FeedBack");
        listNavLabels.add("How it Works");
        listNavLabels.add("Got Questions?");
        listNavLabels.add("LogOut");

        NavListViewAdapter adapter = new NavListViewAdapter(getApplicationContext(),listNavLabels,nvDrawables);
        nvDrawer.setAdapter(adapter);

        setupDrawerContent(nvDrawer);

    }

    int currentPageIndex = -1 ;
    private void setupTopBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivToolBarNavigationIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ivToolBarBack = (ImageView)toolbar.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().getBackStackEntryCount();
                getSupportFragmentManager().popBackStack();
            }
        });
        ivToolBarNavigationIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        tvToolBarMiddleLabel= (TextView)toolbar.findViewById(R.id.tvToolBarMiddleLabel) ;

        ivToolBarEndIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarEndIcn) ;
        setSupportActionBar(toolbar);
    }

    private void setupDrawerContent(ListView navigationView) {

        setDefaultDrawerContent();

        navigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if(currentPageIndex != i)
//                {
                if(i== DrawerConstant.GOT_QUESTIONS)
                {
                    mDrawer.closeDrawers();
                    ApiConfig.Builder configBuilder = new ApiConfig.Builder();
                    configBuilder.setRequireEmail(true);
                    Support.showFAQs(MainPageActivity.this
                            , configBuilder.build());

                }
                else {
                    selectDrawerItem(i);
                }
//                    currentPageIndex =  i ;
//                }
            }
        });
    }

    private void setDefaultDrawerContent() {
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = ProfileWelcomeFragment.class ;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    public void selectDrawerItem(int index) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null ;
        title =  listNavLabels.get(index);

        switch(index)  {
            case DrawerConstant.EXPLORE_RESTAURANT  :
                fragmentClass = ExploreRestrauntsPageFragment.class ; break ;
            case DrawerConstant.OFFERS : fragmentClass = OffersPageFragment.class ;break ;
            case DrawerConstant.FAVORITE : fragmentClass = FavoritePageFragment.class ;break ;
            case DrawerConstant.REVIEW_RATING : fragmentClass = ReviewRatingPageFragment.class ; break ;
            case DrawerConstant.PROFILE :fragmentClass = ProfilePageFragment.class ;break ;
            case DrawerConstant.CONTACT_GODINE : fragmentClass = ContactPageFragment.class ; break ;
            case DrawerConstant.BILLING :fragmentClass = BillingPageFragment.class ; break ;
            case DrawerConstant.VERIFICATION :fragmentClass = VerificationPageFragment.class ; break;
            case DrawerConstant.FEEDBACK :fragmentClass = FeedBackPageFragment.class ; break ;
            case DrawerConstant.HOW_IT_WORKS :fragmentClass =Howitworks.class ; break;
            case DrawerConstant.GOT_QUESTIONS :
            {

            }
            break ;

            default:logoutSession();return ;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(title).commit();

        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
        // setTitle(label);
        /************************
         *  settingTopBar();
         *  *********************/
        tvToolBarMiddleLabel.setText(title);
        // settingTopBar(index);

        // Close the navigation drawer
        mDrawer.closeDrawers();

        closeCFABMenu();

    }

    private void closeCFABMenu() {
        if(leftCenterMenu.isOpen()) leftCenterMenu.close(true);

    }

    private void logoutSession() {
        Intent intent = new Intent(getApplicationContext(),Splash2.class) ;
        startActivity(intent);
        finish();
        appGlobal.resetAppGlobalParams();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() >1)
            super.onBackPressed();
        else
        {
            final AlertDialog.Builder build=new AlertDialog.Builder(MainPageActivity.this);
            LayoutInflater inflater=getLayoutInflater();
            View view1=inflater.inflate(R.layout.dialogbox_activity,null);
            build.setView(view1);
            TextView Top=(TextView) view1.findViewById(R.id.pasword);
            Top.setText("Exit");
            TextView message= (TextView) view1.findViewById(R.id.message);
            message.setText("Do you want to exit from GoDine™?");
            final EditText currentpassword= (EditText) view1.findViewById(R.id.entername);
            currentpassword.setVisibility(View.GONE);
            final EditText newpassword= (EditText) view1.findViewById(R.id.enternumber);
            newpassword.setVisibility(View.GONE);
            build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            build.create();
            build.show();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCFABMenu();
    }

    @Override
    public void setDrawerLocked(boolean enabled) {
        if(enabled)
        {
            mDrawer.setDrawerLockMode(mDrawer.LOCK_MODE_LOCKED_CLOSED);
        }
        else
        {
            mDrawer.setDrawerLockMode(mDrawer.LOCK_MODE_UNLOCKED);
        }
    }
}
