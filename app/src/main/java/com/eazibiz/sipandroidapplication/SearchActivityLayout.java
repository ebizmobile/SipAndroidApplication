package com.eazibiz.sipandroidapplication;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by KARTHI on 01-12-2016.
 */

public class SearchActivityLayout extends ActionBarActivity {

    private ImageView iv;
    private TextView text;
    private AnimatedVectorDrawable searchToBar;
    private AnimatedVectorDrawable barToSearch;
    private float offset;
    private Interpolator interp;
    private int duration;
    private boolean expanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.search_anim_layout);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
       mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        final View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        //TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        //mTitleTextView.setText("My Own Title");


        iv = (ImageView)mCustomView. findViewById(R.id.enq_list_search);
        text = (TextView)mCustomView. findViewById(R.id.text);
        searchToBar = (AnimatedVectorDrawable)mCustomView.getResources().getDrawable(R.drawable.anim_search_to_bar);
        barToSearch = (AnimatedVectorDrawable)mCustomView. getResources().getDrawable(R.drawable.anim_bar_to_search);
        interp = AnimationUtils.loadInterpolator(mCustomView.getContext(), android.R.interpolator.linear_out_slow_in);
        duration = getResources().getInteger(R.integer.duration_bar);
        offset = -71f * (int) getResources().getDisplayMetrics().scaledDensity;
        iv.setTranslationX(offset);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (!expanded) {
                    Toast.makeText(SearchActivityLayout.this, "value111", Toast.LENGTH_SHORT).show();
                    iv.setImageDrawable(searchToBar);
                    text.setVisibility(View.VISIBLE);
                    searchToBar.start();
                    iv.animate().translationX(0f).setDuration(duration).setInterpolator(interp);
                    text.animate().alpha(1f).setStartDelay(duration - 100).setDuration(100).setInterpolator(interp);
                } else {

                    Toast.makeText(SearchActivityLayout.this, "value222", Toast.LENGTH_SHORT).show();
                    iv.setImageDrawable(barToSearch);
                    text.setVisibility(View.GONE);
                    barToSearch.start();
                    iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
                    text.setAlpha(0f);
                }
                expanded = !expanded;


                // iv is sized to hold the search+bar so when only showing the search icon, translate the
                // whole view left by half the difference to keep it centered





            }
        });

       /* ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });*/

        mActionBar.setCustomView(mCustomView);
       mActionBar.setDisplayShowCustomEnabled(true);




    }

    public void animate(View view) {

        if (!expanded) {
            iv.setImageDrawable(searchToBar);
            searchToBar.start();
            Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                    Toast.LENGTH_LONG).show();
            iv.animate().translationX(0f).setDuration(duration).setInterpolator(interp);
            text.animate().alpha(1f).setStartDelay(duration - 100).setDuration(100).setInterpolator(interp);
        } else {
            iv.setImageDrawable(barToSearch);
            barToSearch.start();
            Toast.makeText(getApplicationContext(), "Refresh Clicked2!",
                    Toast.LENGTH_LONG).show();
            iv.animate().translationX(offset).setDuration(duration).setInterpolator(interp);
            text.setAlpha(0f);
        }
        expanded = !expanded;
    }
}
