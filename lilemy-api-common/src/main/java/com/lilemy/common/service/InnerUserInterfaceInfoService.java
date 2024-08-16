package com.lilemy.common.service;

/**
 * 内部用户接口服务
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 接口调用次数
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return 用户是否还有调用次数
     */
    Integer invokeLeftCount(long interfaceInfoId, long userId);

    /**
     * 调用接口，扣减用户接口调用次数
     *
     * @param interfaceInfoId 接口 id
     * @param userId          用户 id
     * @return {@link Boolean}
     */
    Boolean invokeInterface(long interfaceInfoId, long userId);
}
