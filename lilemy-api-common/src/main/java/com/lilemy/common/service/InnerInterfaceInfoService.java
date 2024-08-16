package com.lilemy.common.service;

import com.lilemy.common.model.entity.InterfaceInfo;

/**
 * 内部接口信息服务
 */
public interface InnerInterfaceInfoService {

    /**
     * 获取接口信息
     *
     * @param path   接口路径
     * @param method 请求类型
     * @return {@link InterfaceInfo}
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
