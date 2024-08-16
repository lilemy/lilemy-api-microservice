package com.lilemy.userinterface.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.model.entity.UserInterfaceInfo;
import com.lilemy.userinterface.mapper.UserInterfaceInfoMapper;
import com.lilemy.userinterface.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
 * 用户调用接口关系服务实现类
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Override
    public Boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interface_info_id", interfaceInfoId);
        updateWrapper.eq("user_id", userId);
        updateWrapper.setSql("left_num = left_num - 1, total_num = total_num + 1");
        return this.update(updateWrapper);
    }
}




