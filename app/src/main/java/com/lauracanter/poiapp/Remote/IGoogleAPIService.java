package com.lauracanter.poiapp.Remote;

import com.lauracanter.poiapp.Model.MyPlaces;

import java.net.URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPIService {

    @GET
    Call<MyPlaces> getNearbyPlaces(@Url String url);
}
