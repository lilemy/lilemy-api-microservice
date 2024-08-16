package com.lilemy.userinterface.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.model.entity.UserInterfaceInfo;
import com.lilemy.common.service.InnerUserInterfaceInfoService;
import com.lilemy.userinterface.service.UserInterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 内部用户接口信息服务实现类
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public Integer invokeLeftCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("interface_info_id", interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        return userInterfaceInfo.getLeftNum();
    }

    @Override
    public Boolean invokeInterface(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
