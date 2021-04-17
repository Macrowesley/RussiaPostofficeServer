package cc.mrbird.febs.common.advice;

import cc.mrbird.febs.common.annotation.RsaSecurityParameter;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.RSAUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @desc 请求数据解密
 */
@ControllerAdvice(basePackages = "cc.mrbird.febs.test.controller")
public class RsaDecodeRequestBodyAdvice implements RequestBodyAdvice {

    private Logger logger = LoggerFactory.getLogger(RsaDecodeRequestBodyAdvice.class);

    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJaUAtrUp6bkHAQNMY9XtP76WnUiUkG5VCVzh8vOA5ek9Qx23Lqx9pHKZ0kDIvt7FFSjpUWRzk1Z0XD84+IvUYcNWEXY1cBms0AlQ33+2effStAL977FUwxjB6UxE8un4c/H/IjWDH1ZyPSIcxd+mSzrKbKdAvkZZz0DWMmAC9gnAgMBAAECgYBVm9jzqSuYCuCSvR2MLYVN4fBD4Dt7+m4IzOJL0NjiAnr/lyRvUHaq9LQ8InhlGdi5NkDkiL0N5R7aaiz1j4l9p1Aiyjoe4PMxvjC/JW9YQI4QfYNoYyKpeTFuZJmMdmGBch45VOJJggyvqNVHII2/6Fjxo9hi9/FH9/NdqrTVYQJBAO8pwbSkf4XKwQ6vX6dDaedVindHof0tm1yT1gItgv40MUaWvLQD7ad3JYEGxIb7kiv+Api/yvvLlwHrgjUKcpUCQQChLcJ7qcHAKYd1+x0sQhvpWVQfOmtIMarEXUbF+LejSenkh4y8oBx1u6HdXk7qK6+lhglbZUtMxC+TJNnQ1AzLAkEAmLvE/dTzr4C9a+BpqMrvjhHd3LD9bU874ZJz4G4qMqcoNNk854V+tyzq4Yxt43ozbi7If740JjttU4eQXskuRQJAGS8NNEVSALR8dfLGnCO4OL28ZyS5no4kIIpyhHxYNStXYF3Nn6tkxwCKSgxySCSLTVGkJKOvIE+HH3aikf6QAQJBALZoMLw4SHpBe5wKhm0QbPI71O1hN1QILX1UhMLQGXBenaPEp52id6Eg/ZkYxAnr21UIbNR+zu2N6qWBXW1RBQk=";

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {

            boolean encode = false;
            if (methodParameter.getMethod().isAnnotationPresent(RsaSecurityParameter.class)) {
                System.out.println("测试RSA beforeBodyRead");
                //获取注解配置的包含和去除字段
                RsaSecurityParameter serializedField = methodParameter.getMethodAnnotation(RsaSecurityParameter.class);
                //入参是否需要解密
                encode = serializedField.inDecode();

            }
            if (encode) {
                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
                return new MyHttpInputMessage(inputMessage);
            } else {
                return inputMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;
        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            String content = easpString(IOUtils.toString(inputMessage.getBody(), "utf-8"));
            this.body = IOUtils.toInputStream(RSAUtils.decryptDataOnJava(content, PRIVATE_KEY), "utf-8");
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        /**
         * @param requestData
         * @return
         */
        public String easpString(String requestData) {
            if (requestData != null && !requestData.equals("")) {
                String s = "{\"requestData\":";
                if (!requestData.startsWith(s)) {
                    throw new RuntimeException("参数【requestData】缺失异常！");
                } else {
                    int closeLen = requestData.length() - 1;
                    int openLen = "{\"requestData\":".length();
                    String substring = StringUtils.substring(requestData, openLen, closeLen);
                    return substring;
                }
            }
            return "";
        }
    }
}
