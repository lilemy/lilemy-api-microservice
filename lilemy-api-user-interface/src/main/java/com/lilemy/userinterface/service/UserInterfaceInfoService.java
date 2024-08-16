package com.lilemy.userinterface.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lilemy.common.model.entity.UserInterfaceInfo;

/**
 * 用户调用接口关系服务
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 接口调用次数
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return 用户是否还有调用次数
     */
    Boolean invokeCount(long interfaceInfoId, long userId);
}
