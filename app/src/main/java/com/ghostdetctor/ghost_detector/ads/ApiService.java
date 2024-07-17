//package com.ghostdetctor.ghost_detector.ads;
//
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.GET;
//
//public interface ApiService {
//    Gson gson = new GsonBuilder()
//            .setDateFormat("yyyy-MM-dd HH:mm:ss")
//            .create();
//
//    ApiService apiService = new Retrofit.Builder()
//            .baseUrl("http://id.haiyanstore.online/api/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//            .create(ApiService.class);
//
//    @GET("getid/ca-app-pub-3940256099942544~3347511713")
//    Call<List<AdsModel>> callAdsSplash();
//}
