package com.aliberkaygediko.anbean.Fragments;

import com.aliberkaygediko.anbean.Notification.MyResponse;
import com.aliberkaygediko.anbean.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkmWw6xg:APA91bGuC_sWdoPaifB5CehageTN9A26FHhEpKNBCTNbJb4UxGR_KthnyHKWp84fvNQeHebQ2B8484-mQQlSP4to-y0XZiwXFLrzyXlHQf_b3dOZYr_EWXUCLmJwcTpCTNl1CCKWDzUX"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
