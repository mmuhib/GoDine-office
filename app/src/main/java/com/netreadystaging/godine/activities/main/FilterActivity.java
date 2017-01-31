package com.netreadystaging.godine.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netreadystaging.godine.R;
import com.netreadystaging.godine.activities.AppBaseActivity;
import com.netreadystaging.godine.activities.filters.SelectFilterActivity;
import com.netreadystaging.godine.models.FilterResponse;


public class FilterActivity extends AppBaseActivity implements View.OnClickListener{
    public static int FILTER_RESPONSE_CODE = 220;
    public static String FILTER_RESPONSE = "filter_response";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        setSelectFilters();

        setSelectFiltersDoneButton();

        setupTopBar();
    }

    private void setSelectFiltersDoneButton() {
        final TextView tvDoneRestFilter = (TextView) findViewById(R.id.tvDoneRestFilter);
        tvDoneRestFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterResponse filterResponse = new FilterResponse();
                Intent filterResIntent =  new Intent();
                filterResIntent.putExtra(FILTER_RESPONSE,filterResponse);
                setResult(FILTER_RESPONSE_CODE,filterResIntent);
                finish();
                overridePendingTransition(R.anim.nothing,R.anim.slide_out_bottom);
            }
        });
    }

    private void setSelectFilters() {
        final TextView tvSelectRestCuisines = (TextView) findViewById(R.id.tvSelectRestCuisines);
        tvSelectRestCuisines.setOnClickListener(this);
        final TextView tvSelectRestFeatures = (TextView) findViewById(R.id.tvSelectRestFeatures);
        tvSelectRestFeatures.setOnClickListener(this);
        final TextView tvSelectRestPrice = (TextView) findViewById(R.id.tvSelectRestPrice);
        tvSelectRestPrice.setOnClickListener(this);
        final TextView tvSelectRestRating = (TextView) findViewById(R.id.tvSelectRestRating);
        tvSelectRestRating.setOnClickListener(this);
    }

    private void setupTopBar()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView ivToolBarNavigationIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarNavigationIcn) ;
        ivToolBarNavigationIcn.setVisibility(View.GONE);
        final ImageView ivToolBarBack = (ImageView)toolbar.findViewById(R.id.ivToolBarBack) ;
        ivToolBarBack.setVisibility(View.VISIBLE);
        ivToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final TextView title= (TextView)toolbar.findViewById(R.id.tvToolBarMiddleLabel) ;
        title.setVisibility(View.VISIBLE);
        title.setText("Filter");
        final ImageView ivToolBarEndIcn = (ImageView)toolbar.findViewById(R.id.ivToolBarEndIcn) ;
        ivToolBarEndIcn.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(FilterActivity.this, SelectFilterActivity.class);
        switch(v.getId())
        {
            case R.id.tvSelectRestCuisines :
                intent.putExtra("isMultiple",true);
                intent.putExtra("filter","Cuisines");
                startActivityForResult(intent,CUISINE_FILTER_REQUEST_CODE);
                break ;

            case R.id.tvSelectRestFeatures :
                intent.putExtra("isMultiple",true);
                intent.putExtra("filter","Features");
                startActivityForResult(intent,FEATURE_FILTER_REQUEST_CODE);
                break ;

            case R.id.tvSelectRestPrice :
                intent.putExtra("isMultiple",false);
                intent.putExtra("filter","Price");
                startActivityForResult(intent,PRICE_FILTER_REQUEST_CODE);
                break ;

            case R.id.tvSelectRestRating :
                intent.putExtra("isMultiple",false);
                intent.putExtra("filter","Rating");
                startActivityForResult(intent,RATING_FILTER_REQUEST_CODE);
                break ;

        }

        overridePendingTransition(R.anim.slide_in_bottom,R.anim.nothing);

    }


    private static final int CUISINE_FILTER_REQUEST_CODE = 200 ;
    private static final int FEATURE_FILTER_REQUEST_CODE = 201 ;
    private static final int PRICE_FILTER_REQUEST_CODE = 202 ;
    private static final int RATING_FILTER_REQUEST_CODE = 203 ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== com.itsolutionts.filterhelper.activity.FilterActivity.FILTER_RESPONSE_CODE)
        {


            switch (requestCode) {
                case CUISINE_FILTER_REQUEST_CODE:
                    final TextView tvSelectRestCuisines = (TextView) findViewById(R.id.tvSelectRestCuisines);
                    if(data!=null) {
                        String response = data.getStringExtra("response");
                        if(response!=null && !response.isEmpty())
                            tvSelectRestCuisines.setText(response);
                    }
                    break;

                case FEATURE_FILTER_REQUEST_CODE:
                    final TextView tvSelectRestFeatures = (TextView) findViewById(R.id.tvSelectRestFeatures);
                    if(data!=null) {
                        String response = data.getStringExtra("response");
                        if(response!=null && !response.isEmpty())
                            tvSelectRestFeatures.setText(response);
                    }
                    break;

                case PRICE_FILTER_REQUEST_CODE:
                    final TextView tvSelectRestPrice = (TextView) findViewById(R.id.tvSelectRestPrice);
                    if(data!=null) {
                        String response = data.getStringExtra("response");
                        if(response!=null && !response.isEmpty())
                            tvSelectRestPrice.setText(response);
                    }
                    break;

                case RATING_FILTER_REQUEST_CODE:
                    final TextView tvSelectRestRating = (TextView) findViewById(R.id.tvSelectRestRating);
                    if(data!=null) {
                        String response = data.getStringExtra("response");
                        if(response!=null && !response.isEmpty())
                            tvSelectRestRating.setText(response);
                    }
                    break;

            }
        }

    }
}
