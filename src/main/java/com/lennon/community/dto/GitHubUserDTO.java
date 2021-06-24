package com.lennon.community.dto;

/**
 * @author jzk
 * @date 2021/6/23-21:31
 */
import lombok.Data;


@Data
public class GitHubUserDTO {
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
}
