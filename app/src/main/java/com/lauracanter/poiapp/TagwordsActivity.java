package com.lauracanter.poiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Laura on 08/03/2018.
 */

public class TagwordsActivity extends AppCompatActivity {

    private NonSwipeableViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private Button mNextButton;

    private SliderAdapter mSliderAdapter;
    private ArrayList<String> mFoodAndDrinkGoogleKeywords = new ArrayList<>();
    private ArrayList<String> mShoppingGoogleKeywords = new ArrayList<>();
    private ArrayList<String> mEntertainmentAndSitesGoogleKeywords = new ArrayList<>();
    private ArrayList<String> mNonGoogleKeywords = new ArrayList<>();

    private TextView[] mDots;
    private int mCurrentPage;

    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userPreferencesDatabaseInstance;

    private String currentUserId, currentUserName;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagwords);

        getSupportActionBar().hide();

        userPreferencesDatabaseInstance = FirebaseDatabase.getInstance().getReference().child("User Preferences");

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);
        mNextButton = findViewById(R.id.next_btn);

        mSliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(mSliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("POIApp", "mCurrentPage number is "+mCurrentPage);

                if (mCurrentPage == 3)
                {
                    storeKeywordsInFirebase();
                    Intent intent = new Intent(TagwordsActivity.this, HomePageActivity.class);
                    finish();
                    startActivity(intent);
                }

                else
                {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                    storeKeywordsInArrays();
                }
            }
        });
    }



    public void addDotsIndicator(int position) {

        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for(int i=0;i<mDots.length;i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentWhite));

            mDotLayout.addView(mDots[i]);

        }

        if(mDots.length>0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            Log.d("POIApp", "onPageSelected is "+i);
            addDotsIndicator(i);

            mCurrentPage=i;

            if(i==mDots.length-1)
            {
                mNextButton.setEnabled(true);
                mNextButton.setText("Finish");
            }
            else
            {
                mNextButton.setEnabled(true);
                mNextButton.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    public void storeKeywordsInArrays()
    {
        ToggleButton toggleButton1 = findViewById(R.id.toggleButton1);
        ToggleButton toggleButton2 = findViewById(R.id.toggleButton2);
        ToggleButton toggleButton3 = findViewById(R.id.toggleButton3);
        ToggleButton toggleButton4 = findViewById(R.id.toggleButton4);
        ToggleButton toggleButton5 = findViewById(R.id.toggleButton5);
        ToggleButton toggleButton6 = findViewById(R.id.toggleButton6);
        ToggleButton toggleButton7 = findViewById(R.id.toggleButton7);
        ToggleButton toggleButton8 = findViewById(R.id.toggleButton8);

        if (toggleButton1.isChecked() == true) {
            if (toggleButton1.getText().toString() == "Food Court/Market") {
                Log.d("POIApp", "togglebutton 1 is Food Court/Market");
                mNonGoogleKeywords.add("food court");
                mNonGoogleKeywords.add("food market");
            } else if (toggleButton1.getText().toString() == "Books") {
                Log.d("POIApp", "togglebutton 1 is Books");
                mShoppingGoogleKeywords.add("book_store");
            } else if (toggleButton1.getText().toString() == "Bowling") {
                Log.d("POIApp", "togglebutton 1 is Bowling");
                mEntertainmentAndSitesGoogleKeywords.add("bowling_alley");
            } else if (toggleButton1.getText().toString() == "Library") {
                Log.d("POIApp", "togglebutton 1 is Library");
                mEntertainmentAndSitesGoogleKeywords.add("library");
            }
        }

        if (toggleButton2.isChecked() == true) {
            if (toggleButton2.getText().toString() == "Restaurant") {
                Log.d("POIApp", "toggleButton 2 is Restaurant");
                mFoodAndDrinkGoogleKeywords.add("restaurant");
            } else if (toggleButton2.getText().toString() == "Jewellery") {
                Log.d("POIApp", "toggleButton 2 is Jewellery");
                mShoppingGoogleKeywords.add("jewelry_store");
            } else if (toggleButton2.getText().toString() == "Amusement Park") {
                Log.d("POIApp", "toggleButton 2 is Amusement Park");
                mEntertainmentAndSitesGoogleKeywords.add("amusement_park");
            } else if (toggleButton2.getText().toString() == "Nature Attraction") {
                Log.d("POIApp", "toggleButton 2 is Nature Attraction");
                mNonGoogleKeywords.add("nature attraction");
                mNonGoogleKeywords.add("beach");
                mNonGoogleKeywords.add("mountain");
                mNonGoogleKeywords.add("hiking trail");
            }

        }

        if (toggleButton3.isChecked() == true) {
            if (toggleButton3.getText().toString() == "Cafe") {
                Log.d("POIApp", "toggleButton is Cafe");
                mFoodAndDrinkGoogleKeywords.add("cafe");
            } else if (toggleButton3.getText().toString() == "Gifts") {
                Log.d("POIApp", "toggleButton is Gifts");
                mNonGoogleKeywords.add("gift shop");
            } else if (toggleButton3.getText().toString() == "Aquarium") {
                Log.d("POIApp", "toggleButton is Aquarium");
                mEntertainmentAndSitesGoogleKeywords.add("aquarium");
            } else if (toggleButton3.getText().toString() == "Park") {
                Log.d("POIApp", "toggleButton is Park");
                mEntertainmentAndSitesGoogleKeywords.add("park");
            }
        }

        if (toggleButton4.isChecked() == true) {
            if (toggleButton4.getText().toString() == "Fine Dining") {
                Log.d("POIApp", "toggleButton is a Fine Dining");
                mNonGoogleKeywords.add("fine dining");
            } else if (toggleButton4.getText().toString() == "Clothes/Shoes") {
                Log.d("POIApp", "toggleButton is Clothes and Shoes ");
                mShoppingGoogleKeywords.add("clothes_store");
                mShoppingGoogleKeywords.add("shoe_store");
            } else if (toggleButton4.getText().toString() == "Art Gallery") {
                Log.d("POIApp", "toggleButton is Art Gallery ");
                mEntertainmentAndSitesGoogleKeywords.add("art_gallery");
            } else if (toggleButton4.getText().toString() == "Stadium") {
                Log.d("POIApp", "toggleButton is Stadium");
                mEntertainmentAndSitesGoogleKeywords.add("stadium");
            }
        }

        if (toggleButton5.isChecked() == true) {
            if (toggleButton5.getText().toString() == "Bar") {
                Log.d("POIApp", "toggleButton is Bar");
                mFoodAndDrinkGoogleKeywords.add("bar");
            } else if (toggleButton5.getText().toString() == "Department Store") {
                Log.d("POIApp", "toggleButton is Department Store");
                mShoppingGoogleKeywords.add("department_store");
                mShoppingGoogleKeywords.add("shopping_mall");
            } else if (toggleButton5.getText().toString() == "Cinema") {
                Log.d("POIApp", "toggleButton is Cinema");
                mEntertainmentAndSitesGoogleKeywords.add("movie_theatre");
            } else if (toggleButton5.getText().toString() == "Mosque") {
                Log.d("POIApp", "toggleButton is Mosque");
                mEntertainmentAndSitesGoogleKeywords.add("mosque");
            }
        }

        if (toggleButton6.isChecked() == true) {
            if (toggleButton6.getText().toString() == "Nightclub") {
                Log.d("POIApp", "toggleButton is Nightclub");
                mFoodAndDrinkGoogleKeywords.add("nightclub");
            } else if (toggleButton6.getText().toString() == "Market") {
                Log.d("POIApp", "toggleButton is a Market");
                mNonGoogleKeywords.add("market");
            } else if (toggleButton6.getText().toString() == "Museum") {
                Log.d("POIApp", "toggleButton is Museum");
                mEntertainmentAndSitesGoogleKeywords.add("museum");
            } else if (toggleButton6.getText().toString() == "Church") {
                Log.d("POIApp", "toggleButton is Church");
                mEntertainmentAndSitesGoogleKeywords.add("church");
            }
        }

        if (toggleButton7.isChecked() == true) {
            if (toggleButton7.getText().toString() == "Casino") {
                Log.d("POIApp", "toggleButton is Casino");
                mFoodAndDrinkGoogleKeywords.add("casino");
            } else if (toggleButton7.getText().toString() == "Vintage/Antique") {
                Log.d("POIApp", "toggleButton is Vintage/Antique");
                mNonGoogleKeywords.add("vintage");
                mNonGoogleKeywords.add("antique");
            } else if (toggleButton7.getText().toString() == "Zoo") {
                Log.d("POIApp", "toggleButton is Zoo");
                mEntertainmentAndSitesGoogleKeywords.add("zoo");
            } else if (toggleButton7.getText().toString() == "Synagogue") {
                Log.d("POIApp", "toggleButton is Synagogue");
                mEntertainmentAndSitesGoogleKeywords.add("synagogue");
            }
        }

        if (toggleButton8.isChecked() == true) {
            if (toggleButton8.getText().toString() == "Ice Cream") {
                Log.d("POIApp", "toggleButton is Ice Cream");
                mNonGoogleKeywords.add("ice cream");
                mNonGoogleKeywords.add("gelato");
            } else if (toggleButton8.getText().toString() == "Food Store") {
                Log.d("POIApp", "toggleButton is Food Store");
                mNonGoogleKeywords.add("food store");
                mNonGoogleKeywords.add("supermarket");
            } else if (toggleButton8.getText().toString() == "Park") {
                Log.d("POIApp", "toggleButton is Park");
                mEntertainmentAndSitesGoogleKeywords.add("park");
            } else if (toggleButton8.getText().toString() == "Hindu Temple") {
                Log.d("POIApp", "toggleButton is Hindu Temple");
                mEntertainmentAndSitesGoogleKeywords.add("hindu_temple");
            }
        }

    }


    public void storeKeywordsInFirebase()
    {
        currentUserId = currentUser.getUid();
        UserPreferences userPreferencesInfo = new UserPreferences(mFoodAndDrinkGoogleKeywords, mShoppingGoogleKeywords, mEntertainmentAndSitesGoogleKeywords, mNonGoogleKeywords);

        //userPreferencesDatabaseInstance.child("user_preferences").setValue(userPreferencesInfo);
        userPreferencesDatabaseInstance.child(currentUserId).setValue(userPreferencesInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(TagwordsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(TagwordsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(this, "User Information Saved", Toast.LENGTH_SHORT);

    }


}
