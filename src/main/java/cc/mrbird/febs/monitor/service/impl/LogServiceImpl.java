package cc.mrbird.febs.monitor.service.impl;


import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.AddressUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.monitor.entity.SystemLog;
import cc.mrbird.febs.monitor.mapper.LogMapper;
import cc.mrbird.febs.monitor.service.ILogService;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;


@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, SystemLog> implements ILogService {

    private final ObjectMapper objectMapper;

    @Override
    public IPage<SystemLog> findLogs(SystemLog systemLog, QueryRequest request) {
        QueryWrapper<SystemLog> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(systemLog.getCreateTimeFrom()) &&
                StringUtils.equals(systemLog.getCreateTimeFrom(), systemLog.getCreateTimeTo())) {
            systemLog.setCreateTimeFrom(systemLog.getCreateTimeFrom() + " 00:00:00");
            systemLog.setCreateTimeTo(systemLog.getCreateTimeTo() + " 23:59:59");
        }
        if (StringUtils.isNotBlank(systemLog.getUsername())) {
            queryWrapper.lambda().eq(SystemLog::getUsername, systemLog.getUsername().toLowerCase());
        }
        if (StringUtils.isNotBlank(systemLog.getOperation())) {
            queryWrapper.lambda().like(SystemLog::getOperation, systemLog.getOperation());
        }
        if (StringUtils.isNotBlank(systemLog.getLocation())) {
            queryWrapper.lambda().like(SystemLog::getLocation, systemLog.getLocation());
        }
        if (StringUtils.isNotBlank(systemLog.getCreateTimeFrom()) && StringUtils.isNotBlank(systemLog.getCreateTimeTo())) {
            queryWrapper.lambda()
                    .ge(SystemLog::getCreateTime, systemLog.getCreateTimeFrom())
                    .le(SystemLog::getCreateTime, systemLog.getCreateTimeTo());
        }

        Page<SystemLog> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", FebsConstant.ORDER_DESC, true);

        return this.page(page, queryWrapper);
    }

    @Override
    public void deleteLogs(String[] logIds) {
        List<String> list = Arrays.asList(logIds);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public void saveLog(ProceedingJoinPoint point, User user, Method method, String ip, String operation, long start) {
        SystemLog systemLog = new SystemLog();
        // ?????? IP??????
        systemLog.setIp(ip);
        // ??????????????????
//        User user = FebsUtil.getCurrentUser();
        if (user != null) {
            systemLog.setUsername(user.getUsername());
        }
        // ????????????
        systemLog.setTime(System.currentTimeMillis() - start);
        // ??????????????????
        systemLog.setOperation(operation);
        // ???????????????
        String className = point.getTarget().getClass().getName();
        // ??????????????????
        String methodName = method.getName();
        systemLog.setMethod(className + "." + methodName + "()");
        // ????????????????????????
        Object[] args = point.getArgs();
        // ???????????????????????????
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            systemLog.setParams(params.toString());
        }
        systemLog.setCreateTime(new Date());
        systemLog.setLocation(AddressUtil.getCityInfo(ip));
        // ??????????????????
        save(systemLog);
    }

    @SuppressWarnings("all")
    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // ??????????????? NoSuchMethodException ??????????????? toString ?????? ???????????? writeValueAsString ????????? ??? Object??? toString??????
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("??????????????????");
        }
        return params;
    }
}
