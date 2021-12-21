package cc.mrbird.febs.common.handler;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.exception.FileDownloadException;
import cc.mrbird.febs.common.exception.LimitAccessException;
import cc.mrbird.febs.common.exception.LimitAccessViewException;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.rcs.common.exception.RcsApiException;
import cc.mrbird.febs.rcs.common.exception.RcsManagerBalanceException;
import cc.mrbird.febs.rcs.dto.manager.ApiRcsResponse;
import cc.mrbird.febs.rcs.dto.manager.OperationError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /*@ExceptionHandler(value = Exception.class)
    public FebsResponse handleException(Exception e) {
        e.printStackTrace();
        log.error("系统内部异常，异常信息 {}", e.getMessage());
        return new FebsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message(MessageUtils.getMessage("globalHandler.system.error"));
    }
    */

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e, HttpServletResponse response) {
        response.setStatus(400);
        e.printStackTrace();
        log.error("系统内部异常，异常信息 {}", e.getMessage());
//        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR"));
//        return new ApiRcsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR");
        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        msg.put("message", "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = FebsException.class)
    public FebsResponse handleFebsException(FebsException e) {
        log.error("Febs系统错误 {}", e.getMessage());
        return new FebsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage());
    }



    @ExceptionHandler(value = RcsApiException.class)
    public ResponseEntity handleRcsApiException(RcsApiException e) {
        log.error("RCS api 错误 {}", e.getMessage());
        HashMap msg = new HashMap<>(2);
        msg.put("code", e.getCode());
        msg.put("message", e.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        log.error("访问方式不对 {}", e.getMessage());
//        return new ApiResponse(HttpStatus.BAD_REQUEST.value(),new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
//        return new ApiRcsResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.BAD_REQUEST.value());
        msg.put("message", e.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = LimitAccessException.class)
    public FebsResponse handleLimitAccessApiException(LimitAccessException e) {
        log.error("LimitAccessException, {}", e.getMessage());
        return new FebsResponse().code(HttpStatus.BAD_REQUEST).message(e.getMessage());
    }

    @ExceptionHandler(value = LimitAccessViewException.class)
    public Object handleLimitAccessViewException(LimitAccessViewException e) {
        log.error("666 handleLimitAccessViewException, {}", e.getMessage());

        ModelAndView mav = new ModelAndView();
        mav.setViewName(FebsUtil.view("error/429"));
//        mav.setViewName("error/429");
        return mav;
    }


    /**
     * 处理我们调用俄罗斯接口时 关于金额的异常
     * @param e
     * @return
     */
    /*@ExceptionHandler(value = FmException.class)
    public ApiResponse handleFmException(FmException e) {
        log.error("RCS 金额异常 {} ", e.getMessage());
        return new ApiResponse(HttpStatus.BAD_REQUEST.value(),
                new OperationError(HttpStatus.BAD_REQUEST.value(),e.getMessage(),e.getManagerBalanceDTO()));
    }*/

    /**
     * 处理我们调用俄罗斯接口时 关于金额的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RcsManagerBalanceException.class)
    public ResponseEntity handleRcsManagerBalanceException(RcsManagerBalanceException e) {
        log.error("RCS 金额异常 {} , balance = {}", e.getMessage(), e.getManagerBalanceDTO().toString());
       /* return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new OperationError(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),e.getManagerBalanceDTO()));*/
//        return new OperationError(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),e.getManagerBalanceDTO());
        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        msg.put("message", e.getManagerBalanceDTO().toString());
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletResponse response) {
        log.error("RCS handleHttpMessageNotReadableException 错误 {}", e.getMessage());
//        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()));
//        return new ApiRcsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        msg.put("message", e.getMessage());
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 统一处理请求参数校验(实体对象传参-form)
     *
     * @param e BindException
     * @return FebsResponse
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity validExceptionHandler(BindException e) {
        log.error("请求参数校验(实体对象传参-form) 错误 {}", e.getMessage());
        e.printStackTrace();
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
//        return new FebsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
//        return new ApiRcsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),message.toString());
        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.BAD_REQUEST.value());
        msg.put("message", message.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    /**
     * 统一处理请求参数校验(json)
     *
     * @param e ConstraintViolationException
     * @return FebsResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("请求参数校验(json) 错误 {}", e.getMessage());
//        e.printStackTrace();
        StringBuilder message = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error(message.toString());
//        return new FebsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
//        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),message.toString()));
//        return new ApiRcsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),message.toString());
        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.BAD_REQUEST.value());
        msg.put("message", message.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }



    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return FebsResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(ConstraintViolationException e) {
        log.error("统一处理请求参数校验(普通传参) 错误 {}", e.getMessage());
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
//        return new FebsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
//        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),message.toString()));
//        return new ApiRcsResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),message.toString());
        HashMap msg = new HashMap<>(2);
        msg.put("code", HttpStatus.BAD_REQUEST.value());
        msg.put("message", message.toString());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(value = UnauthorizedException.class)
    public FebsResponse handleUnauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException, {}", e.getMessage());
        return new FebsResponse().code(HttpStatus.FORBIDDEN).message(e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public FebsResponse handleAuthenticationException(AuthenticationException e) {
        log.error("AuthenticationException, {}", e.getMessage());
        return new FebsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public FebsResponse handleAuthorizationException(AuthorizationException e){
        log.error("AuthorizationException, {}", e.getMessage());
        return new FebsResponse().code(HttpStatus.UNAUTHORIZED).message(e.getMessage());
    }


    @ExceptionHandler(value = ExpiredSessionException.class)
    public FebsResponse handleExpiredSessionException(ExpiredSessionException e) {
        log.error("ExpiredSessionException", e);
        return new FebsResponse().code(HttpStatus.UNAUTHORIZED).message(e.getMessage());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleFileDownloadException(FileDownloadException e) {
        log.error("FileDownloadException", e);
    }
}
