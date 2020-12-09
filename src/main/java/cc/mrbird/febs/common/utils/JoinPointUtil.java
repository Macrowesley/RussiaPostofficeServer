package cc.mrbird.febs.common.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class JoinPointUtil {
    public JoinPointUtil() {
    }

    /**
     *
     * @param request
     * @return
     */
    public static synchronized LinkedHashMap<String, String> getHeadersInfo(HttpServletRequest request) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String> ();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取参数信息，只能是get，并且是?name=value&name2=value2的形式才能从中获取参数
     * @param request
     * @return
     */
    public static synchronized LinkedHashMap<String, String> getURLParameterMap(HttpServletRequest request) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * post得到的object,得到对应的key value
     * @param jsonStr
     * @return
     */
    public static synchronized LinkedHashMap<String, String> getJsonStrMap(String jsonStr){
        LinkedHashMap map = JSON.parseObject(jsonStr, LinkedHashMap.class);
        return map;
    }

    /**
     * 功能描述 获取API接口签名实现类
     * @author Hades
     * @date 2020/6/2
     * @param  token token
     * @param  timestamp 当前时间戳
     * @param  nonce 随机字符串
     * @return com.wise.medical.common.utils.Certificate
     */
    /*private Certificate getCertificate(String token , String timestamp , String nonce) {
        Certificate.CertificateBuilder certificateBuilder = new Certificate.CertificateBuilder ();
        certificateBuilder.setToken (token);
        certificateBuilder.setRequestNonce (nonce);
        certificateBuilder.setRequestTimestamp (timestamp);
        certificateBuilder.setSecretKey (SystemConstant.SECRET_KEY);
        return new Certificate (certificateBuilder);
    }*/

}
