package com.example.auctionapp.global.retrofit;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class RetrofitConstants {

    @Getter
    @NoArgsConstructor
    public enum ERetrofitCallback{
        eUnauthorized("Unauthorized"),
        eForbidden("Forbidden"),
        eNotFound("Not Found"),
        eNoContent("No Content"),

        eSuccessResponse("login retrofit success, idToken: "),
        eFailResponse("onFailResponse"),
        eConnectionFail("연결실패");



        private String text;
        private ERetrofitCallback(String text){
            this.text=text;
        }
        public String getText(){
            return this.text;
        }
    }
}
