package com.lilemy.interfaceinfo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.constant.CommonConstant;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.exception.ThrowUtils;
import com.lilemy.common.utils.SqlUtils;
import com.lilemy.interfaceinfo.mapper.InterfaceInfoMapper;
import com.lilemy.interfaceinfo.service.InterfaceInfoService;
import com.lilemy.common.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.lilemy.common.model.entity.InterfaceInfo;
import com.lilemy.common.model.vo.interfaceinfo.InterfaceInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息服务实现
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestParams = interfaceInfo.getRequestParams();
        String method = interfaceInfo.getMethod();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description, url, requestParams, method), ResultCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "接口描述过长");
        }
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "接口名字过长");
        }
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = interfaceInfoQueryRequest.getSortField();
        // 将驼峰字段转化为数据库的下划线字段
        String underlineSortField = StrUtil.toUnderlineCase(sortField);
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestParams = interfaceInfoQueryRequest.getRequestParams();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.like(StringUtils.isNotBlank(requestParams), "request_params", requestParams);
        queryWrapper.like(StringUtils.isNotBlank(requestHeader), "request_header", requestHeader);
        queryWrapper.like(StringUtils.isNotBlank(responseHeader), "response_header", responseHeader);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(method), "method", method);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                underlineSortField);
        return queryWrapper;
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        return interfaceInfoVO;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOListPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOListPage;
        }
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(this::getInterfaceInfoVO).collect(Collectors.toList());
        interfaceInfoVOListPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOListPage;
    }

}




