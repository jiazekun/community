package com.lennon.community.provider;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.kyss.community.dto.GitHubUser;
import com.lennon.community.dto.AccessTokenDTO;
import com.lennon.community.dto.GitHubUser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
public class GitHubProvider {

    public static final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//    public static final String logUrl = "https://github.com/login/oauth/access_token";//这是github的
//    public static final String userUrl = "https://api.github.com/user";//这是github的

    //https://gitee.com/oauth/token?
    // grant_type=authorization_code
    // &code={code}&client_id={client_id}
    // &redirect_uri={redirect_uri}
    // &client_secret={client_secret}

//    public static final String logUrl = "https://gitee.com/oauth/token?";//这是github的
    public static final String userUrl = "https://gitee.com/api/v5/user";//这是github的


    OkHttpClient client = new OkHttpClient();

    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws IOException {
        String logUrl = "https://gitee.com/oauth/token?&grant_type=authorization_code&code="+accessTokenDTO.getCode()+"&client_id="+accessTokenDTO.getClientId()+"&redirect_uri="+accessTokenDTO.getRedirectUrl()+"&client_secret="+accessTokenDTO.getClientSecret()+"&state="+accessTokenDTO.getState();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .url(logUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String token = response.body().string();
            System.out.println(this.getClass()+"的"+token);
            return token;
        }
    }

    public GitHubUser getUserInfo(String token) throws IOException {
        JSONObject jsonObject = JSON.parseObject(token);
        String stringToken = (String) jsonObject.get("access_token");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", "token " + stringToken)
                .url(userUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String userInfo = response.body().string();
            System.out.println(userInfo);
            JSONObject userInfoObj = JSON.parseObject(userInfo);
            GitHubUser GitHubUser = new GitHubUser();
            GitHubUser.setId(((Number) userInfoObj.get("id")).longValue());
            GitHubUser.setName((String) userInfoObj.get("name"));
            GitHubUser.setBio((String) userInfoObj.get("bio"));
            GitHubUser.setAvatarUrl((String) userInfoObj.get("avatar_url"));
            return GitHubUser;
        }
    }
}
