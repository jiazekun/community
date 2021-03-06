package com.lennon.community.controller;

/**
 * @author jzk
 * @date 2021/6/23-21:34
 */

import com.lennon.community.dto.AccessTokenDTO;
import com.lennon.community.dto.GitHubUser;
//import com.lennon.community.generator.dao.UserMapper;
//import com.lennon.community.generator.model.User;
import com.lennon.community.mapper.UserMapper;
import com.lennon.community.model.User;
import com.lennon.community.provider.GitHubProvider;
//import com.lennon.community.service.IOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName OAuthController
 * @Description TODO
 * @Author davidt
 * @Date 6/4/2020 11:01 AM
 * @Version 1.0
 **/

@Controller
public class OAuthController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private IOAuthService IOAuthService;

//    @Value("${github.client.id}")//这是github的clientId
    @Value("${gitte.oauth.clientId}")//这是gitte的clientId
    private String clientId;

//    @Value("${github.client.secret}")//这是github的clientSecret
    @Value("${gitte.oauth.clientSecret}")//这是gitte的clientSecret
    private String clientSecret;

//    @Value("${github.redirect.uri}")//这是github的uri
    @Value("${gitte.oauth.callback}")//这是gitte的callback
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state",required = true) String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClientId(clientId);
        accessTokenDTO.setClientSecret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirectUrl(redirectUri);
        accessTokenDTO.setState(state);
        try {
            String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
            GitHubUser gitHubUser = gitHubProvider.getUserInfo(accessToken);
//            System.out.println(user.getName());
//            System.out.println(accessToken);
//            System.out.println(user);
            if (gitHubUser != null) {
                User user = new User();
                user.setName(gitHubUser.getName());
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setAccountId(String.valueOf(gitHubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insert(user);
//                user.setAvatarUrl(GitHubUser.getAvatarUrl());
//                // save to h2 database
//                IOAuthService.insertOrUpdate(user);

                // save to session
                request.getSession().setAttribute("user", gitHubUser);
                // 添加 cookie
                response.addCookie(new Cookie("token", token));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
