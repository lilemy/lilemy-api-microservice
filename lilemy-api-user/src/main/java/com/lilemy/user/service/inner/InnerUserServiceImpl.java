package com.lilemy.user.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.service.InnerUserService;
import com.lilemy.common.model.entity.User;
import com.lilemy.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 内部用户服务实现类
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("access_key", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
