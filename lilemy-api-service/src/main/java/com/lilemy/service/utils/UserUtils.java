package com.lilemy.service.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.constant.UserConstant;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.model.entity.User;
import com.lilemy.common.model.enums.UserRoleEnum;

/**
 * 用户信息
 */
public class UserUtils {

    /**
     * 获取当前登录用户
     *
     * @return 登录用户信息
     */
    public static User getLoginUser() {
        // 先判断是否已登录
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ResultCode.NOT_LOGIN_ERROR);
        }
        Object userObj = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        return (User) userObj;
    }

    /**
     * 判断用户是否为管理员
     *
     * @param user 用户信息
     * @return true -> 管理员
     */
    public static Boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 判断用户是否为管理员
     *
     * @return true -> 管理员
     */
    public static Boolean isAdmin() {
        return isAdmin(getLoginUser());
    }
}
