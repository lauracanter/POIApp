package com.lauracanter.poiapp;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by Laura on 08/03/2018.
 */

public class UserPreferences {
    private String mCurrentUser;
    private ArrayList<String> mFoodAndDrinkGoogleKeywords;
    private ArrayList<String> mShoppingGoogleKeywords;
    private ArrayList<String> mEntertainmentAndSitesGoogleKeywords;
    private ArrayList<String> mNonGoogleKeywords;

    public UserPreferences() {
    }

    public UserPreferences(ArrayList<String> foodAndDrinkGoogleKeywords, ArrayList<String> shoppingGoogleKeywords, ArrayList<String> entertainmentAndSitesGoogleKeywords, ArrayList<String> nonGoogleKeywords) {
        mFoodAndDrinkGoogleKeywords = foodAndDrinkGoogleKeywords;
        mShoppingGoogleKeywords = shoppingGoogleKeywords;
        mEntertainmentAndSitesGoogleKeywords = entertainmentAndSitesGoogleKeywords;
        mNonGoogleKeywords = nonGoogleKeywords;
    }

    public String getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(String currentUser) {
        mCurrentUser = currentUser;
    }

    public ArrayList<String> getFoodAndDrinkGoogleKeywords() {
        return mFoodAndDrinkGoogleKeywords;
    }

    public void setFoodAndDrinkGoogleKeywords(ArrayList<String> foodAndDrinkGoogleKeywords) {
        mFoodAndDrinkGoogleKeywords = foodAndDrinkGoogleKeywords;
    }

    public ArrayList<String> getShoppingGoogleKeywords() {
        return mShoppingGoogleKeywords;
    }

    public void setShoppingGoogleKeywords(ArrayList<String> shoppingGoogleKeywords) {
        mShoppingGoogleKeywords = shoppingGoogleKeywords;
    }

    public ArrayList<String> getEntertainmentAndSitesGoogleKeywords() {
        return mEntertainmentAndSitesGoogleKeywords;
    }

    public void setEntertainmentAndSitesGoogleKeywords(ArrayList<String> entertainmentAndSitesGoogleKeywords) {
        mEntertainmentAndSitesGoogleKeywords = entertainmentAndSitesGoogleKeywords;
    }

    public ArrayList<String> getNonGoogleKeywords() {
        return mNonGoogleKeywords;
    }

    public void setNonGoogleKeywords(ArrayList<String> nonGoogleKeywords) {
        mNonGoogleKeywords = nonGoogleKeywords;
    }

}
