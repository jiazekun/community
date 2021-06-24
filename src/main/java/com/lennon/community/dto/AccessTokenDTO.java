package com.lennon.community.dto;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author jzk
 * @date 2021/6/23-16:09
 */
@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class AccessTokenDTO {
    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUrl;
    private String state;
}
