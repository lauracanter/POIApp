package com.lauracanter.poiapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Laura on 17/03/2018.
 */

public class SliderAdapter extends PagerAdapter{

    Context context;
    LayoutInflater mLayoutInflater;

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    public int[] slide_image ={
        R.drawable.logo_world,
        R.drawable.logo_world,
        R.drawable.logo_world,
            R.drawable.logo_world
    };

    public String[] keyword_titles = {
            "Choose Food/Drink Places:",
            "Choose Shopping Places:",
            "Choose Entertainment/Sightseeing Places",
            "Choose Entertainment/Sightseeing Places"
    };

    public String[] slide_toggle_button_1 = {
            "Food Court/Market",
            "Books",
            "Bowling",
            "Library"
    };

    public String[] slide_toggle_button_2 = {
            "Restaurant",
            "Jewellery",
            "Amusement Park",
            "Nature Attraction"
    };

    public String[] slide_toggle_button_3 = {
            "Cafe",
            "Gifts",
            "Aquarium",
            "Park"
    };

    public String[] slide_toggle_button_4 = {
            "Fine Dining",
            "Clothes/Shoes",
            "Art Gallery",
            "Stadium"
    };

    public String[] slide_toggle_button_5 = {
            "Bar",
            "Department Store",
            "Cinema",
            "Mosque"
    };

    public String[] slide_toggle_button_6 = {
            "Nightclub",
            "Market",
            "Museum",
            "Church"
    };

    public String[] slide_toggle_button_7 = {
            "Casino",
            "Vintage/Antique",
            "Zoo",
            "Synagogue"
    };

    public String[] slide_toggle_button_8 = {
            "Ice Cream",
            "Food Store",
            "Park",
            "Hindu Temple"
    };


    @Override
    public int getCount() {
        return slide_toggle_button_1.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.activity_slide_layout, container, false);

       // ImageView slideImageView = view.findViewById(R.id.icon_imageview);
        TextView title = view.findViewById(R.id.text_keyword_title);
        ToggleButton toggleButton1 = view.findViewById(R.id.toggleButton1);
        ToggleButton toggleButton2 = view.findViewById(R.id.toggleButton2);
        ToggleButton toggleButton3 = view.findViewById(R.id.toggleButton3);
        ToggleButton toggleButton4 = view.findViewById(R.id.toggleButton4);
        ToggleButton toggleButton5 = view.findViewById(R.id.toggleButton5);
        ToggleButton toggleButton6 = view.findViewById(R.id.toggleButton6);
        ToggleButton toggleButton7 = view.findViewById(R.id.toggleButton7);
        ToggleButton toggleButton8 = view.findViewById(R.id.toggleButton8);


        //slideImageView.setImageResource(slide_image[position]);
        title.setText(keyword_titles[position]);
        toggleButton1.setText(slide_toggle_button_1[position]);
        toggleButton1.setTextOn(slide_toggle_button_1[position]);
        toggleButton1.setTextOff(slide_toggle_button_1[position]);

        toggleButton2.setText(slide_toggle_button_2[position]);
        toggleButton2.setTextOn(slide_toggle_button_2[position]);
        toggleButton2.setTextOff(slide_toggle_button_2[position]);

        toggleButton3.setText(slide_toggle_button_3[position]);
        toggleButton3.setTextOn(slide_toggle_button_3[position]);
        toggleButton3.setTextOff(slide_toggle_button_3[position]);

        toggleButton4.setText(slide_toggle_button_4[position]);
        toggleButton4.setTextOn(slide_toggle_button_4[position]);
        toggleButton4.setTextOff(slide_toggle_button_4[position]);

        toggleButton5.setText(slide_toggle_button_5[position]);
        toggleButton5.setTextOn(slide_toggle_button_5[position]);
        toggleButton5.setTextOff(slide_toggle_button_5[position]);

        toggleButton6.setText(slide_toggle_button_6[position]);
        toggleButton6.setTextOn(slide_toggle_button_6[position]);
        toggleButton6.setTextOff(slide_toggle_button_6[position]);

        toggleButton7.setText(slide_toggle_button_7[position]);
        toggleButton7.setTextOn(slide_toggle_button_7[position]);
        toggleButton7.setTextOff(slide_toggle_button_7[position]);

        if(slide_toggle_button_8[position].equals(""))
        {
            toggleButton8.setVisibility(View.INVISIBLE);
        }
        else
        {
            toggleButton8.setText(slide_toggle_button_8[position]);
            toggleButton8.setTextOn(slide_toggle_button_8[position]);
            toggleButton8.setTextOff(slide_toggle_button_8[position]);
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((RelativeLayout)object);
    }

}
