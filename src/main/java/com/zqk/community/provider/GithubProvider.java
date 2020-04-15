package com.zqk.community.provider;

import com.alibaba.fastjson.JSON;
import com.zqk.community.dto.AccessTokenDTO;
import com.zqk.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //媒体类型以json格式发送
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        //将dto对象转化为json字符串格式，然后再转化为json格式

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //将Requestbody类对象body发送至https://github.com/login/oauth/access_token

        try (Response response = client.newCall(request).execute()) {
            //收到回复response
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        //同样将发送request get请求发送至github
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //获取到github发回来的response用户信息
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
