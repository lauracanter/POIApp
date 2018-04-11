package com.lauracanter.poiapp;

import com.lauracanter.poiapp.Remote.IGoogleAPIService;
import com.lauracanter.poiapp.Remote.RetrofitClient;

public class Common {

    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
