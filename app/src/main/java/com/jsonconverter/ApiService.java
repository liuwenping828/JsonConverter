package com.jsonconverter;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/9/13.
 */
public interface ApiService {

//    http://gank.io/api/data/福利/1/1

    @GET("data/福利/{number}/{page}")
    Call<JSONObject> getBeauties(@Path("number") int number, @Path("page") int page);

//    // 获取下一个播放内容
//    @GET("getNextConnect")
//    Call<JSONObject> getNextConnect(@Query("deviceCode") String deviceCode, @Query("token") String token);


    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);







}
