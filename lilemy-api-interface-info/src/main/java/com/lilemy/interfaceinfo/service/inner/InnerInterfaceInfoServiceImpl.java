package com.lilemy.interfaceinfo.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.model.entity.InterfaceInfo;
import com.lilemy.common.service.InnerInterfaceInfoService;
import com.lilemy.interfaceinfo.service.InterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 内部接口信息实现类
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (StringUtils.isAnyBlank(path, method)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("path", path);
        queryWrapper.eq("method", method);
        return interfaceInfoService.getOne(queryWrapper);
    }
}
