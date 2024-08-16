package com.lilemy.common.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更新个人信息请求
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 143732764048089209L;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

}