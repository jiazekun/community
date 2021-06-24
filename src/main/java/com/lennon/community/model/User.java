package com.lennon.community.model;

import lombok.Data;

/**
 * @author jzk
 * @date 2021/6/24-16:54
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
}
