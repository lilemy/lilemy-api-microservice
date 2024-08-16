package com.lilemy.interfaceinfo.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lilemy.clientsdk.client.LilemyApiClient;
import com.lilemy.clientsdk.model.OperationNumber;
import com.lilemy.common.common.BaseResponse;
import com.lilemy.common.common.DeleteRequest;
import com.lilemy.common.common.ResultCode;
import com.lilemy.common.common.ResultUtils;
import com.lilemy.common.constant.UserConstant;
import com.lilemy.common.exception.BusinessException;
import com.lilemy.common.exception.ThrowUtils;
import com.lilemy.interfaceinfo.service.InterfaceInfoService;
import com.lilemy.common.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.lilemy.common.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.lilemy.common.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.lilemy.common.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.lilemy.common.model.entity.InterfaceInfo;
import com.lilemy.common.model.entity.User;
import com.lilemy.common.model.enums.InterfaceInfoStatusEnum;
import com.lilemy.common.model.vo.interfaceinfo.InterfaceInfoVO;
import com.lilemy.service.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 接口信息接口
 */
@RestController
@RequestMapping("/")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    // region 增删改查

    @Operation(summary = "创建")
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        Object object = StpUtil.getTokenSession().get(UserConstant.USER_LOGIN_STATE);
        User user = (User) object;
        interfaceInfo.setUserId(user.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User user = UserUtils.getLoginUser();
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !UserUtils.isAdmin()) {
            throw new BusinessException(ResultCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    @Operation(summary = "更新（仅管理员）")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据 id 获取")
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo));
    }

    @Operation(summary = "分页获取列表（仅管理员）")
    @PostMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        ThrowUtils.throwIf(!UserUtils.isAdmin(), ResultCode.NO_AUTH_ERROR);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    @Operation(summary = "分页获取列表（封装类）")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                         HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ResultCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    @Operation(summary = "分页获取当前用户创建的资源列表")
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                                           HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        User loginUser = UserUtils.getLoginUser();
        interfaceInfoQueryRequest.setUserId(loginUser.getId());
        return listInterfaceInfoVOByPage(interfaceInfoQueryRequest, request);
    }

    // endregion

    @Operation(summary = "测试在线调用")
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        // 1. 校验接口是否存在
        Long id = interfaceInfoInvokeRequest.getId();
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        JSONObject jsonObject = JSONUtil.parseObj(userRequestParams);
        log.info("params: {}", jsonObject);
        if (userRequestParams == null) {// 在线调用获取名称接口时参数为空（有些接口不需要填写参数）
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ResultCode.NOT_FOUND_ERROR);
        // 2. 只有已经上线的接口才能下线
        if (oldInterfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "该接口已关闭");
        }
        // 3. 调用接口
        User loginUser = UserUtils.getLoginUser();
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        LilemyApiClient apiClient = new LilemyApiClient(accessKey, secretKey);
        // 调用求和接口
        OperationNumber operationNumber = new OperationNumber();
        operationNumber.setFirstNumber(jsonObject.getDouble("firstNumber"));
        operationNumber.setSecondNumber(jsonObject.getDouble("secondNumber"));
        log.info("operationNumber: {}", operationNumber);
        // todo 远程调用接口
        String sum = apiClient.getSum(operationNumber);
        return ResultUtils.success(sum);
    }
}
