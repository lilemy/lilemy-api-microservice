package com.lilemy.common.service;

import com.lilemy.common.model.entity.User;

/**
 * 内部用户服务
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     *
     * @param accessKey 用户秘钥
     * @return 用户信息
     */
    User getInvokeUser(String accessKey);
}
