package com.lilemy.clientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import com.lilemy.clientsdk.model.OperationNumber;
import com.lilemy.clientsdk.model.enums.OperationEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.lilemy.clientsdk.utils.SignUtils.getSign;


/**
 * 调用第三方接口的客户端
 */
@Slf4j
public class LilemyApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8081";

    private final String accessKey;

    private final String secretKey;

    public LilemyApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getSum(OperationNumber number) {
        String result = getOperationNum(number, OperationEnum.SUM.getValue());
        log.info(result);
        return result;
    }

    public String getSubtract(OperationNumber number) {
        String result = getOperationNum(number, OperationEnum.SUBTRACT.getValue());
        log.info(result);
        return result;
    }

    public String getMultiply(OperationNumber number) {
        String result = getOperationNum(number, OperationEnum.MULTIPLY.getValue());
        log.info(result);
        return result;
    }

    public String getDivide(OperationNumber number) {
        String result = getOperationNum(number, OperationEnum.DIVIDE.getValue());
        log.info(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", getSign(body, secretKey));
        return hashMap;
    }

    /**
     * 根据运算符调用对应接口
     *
     * @param number    进行运算的数
     * @param operation 运算符
     * @return 请求体
     */
    private String getOperationNum(OperationNumber number, String operation) {
        String json = JSONUtil.toJsonStr(number);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/interface/operation/" + operation)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        log.info(String.valueOf(httpResponse.getStatus()));
        return httpResponse.body();

    }
}
