package com.slama.foodfacts.restapi;


import com.slama.foodfacts.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiInterface {

    @GET("api/v0/product/{barcodeNumber}.json")
    Call<Product> getProductInfo(@Path("barcodeNumber") String barcodeNumber);
}
