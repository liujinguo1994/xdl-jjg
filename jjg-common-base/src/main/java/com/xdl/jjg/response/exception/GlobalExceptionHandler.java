package com.xdl.jjg.response.exception;

import com.xdl.jjg.response.web.ApiResponse;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 全局异常拦截处理器
 */
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常拦截
     * @param e
     * @return
     */
    @ExceptionHandler(UnCheckedWebException.class)
    public ApiResponse unCheckedWebException(Exception e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
    }

    /**
     * 验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse unauthorizedException(Exception e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"无权限，请检查是否正确");
    }

    /**
     * 验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public ApiResponse authorizationException(Exception e){
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"无权限，请检查是否正确");
    }


    /**
     * 自定义参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(ArgumentException.class)
    public ApiResponse argumentException(Exception e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
    }

    /**
     * spring handler 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse handlerNoFoundException(Exception e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_NOT_FOUND,"路径不存在，请检查路径是否正确");
    }

    /**
     * 重复key异常
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResponse handleDuplicateKeyException(DuplicateKeyException e){
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"数据库中已存在该记录");
    }

    /**
     * 父类异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e){
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"内部错误");
    }

    /**
     * 运行异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ApiResponse handleGlobalException(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"运行异常");
    }

    /**
     * 空指针异常
     * @param e
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse handlerNullPointerException(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"空指针异常");
    }

    /**
     * 类型转换异常
     * @param e
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public ApiResponse handlerClassCastException(ClassCastException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"类型转换异常");
    }

    /**
     * IO异常
     * @param e
     * @return
     */
    @ExceptionHandler(IOException.class)
    public ApiResponse handlerIOException(IOException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"IO异常");
    }

    /**
     * 未知方法异常
     * @param e
     * @return
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public ApiResponse handlerNoSuchMethodException(NoSuchMethodException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"未知方法异常");
    }

    /**
     * 数组越界异常
     * @param e
     * @return
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ApiResponse handlerIndexOutOfBoundsException(IndexOutOfBoundsException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_BAD_REQUEST,"数组越界异常");
    }

    /**
     * 400错误
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse handlerRequestNotReadable(HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_BAD_REQUEST,"http消息不可读异常");
    }

    /**
     * 类型匹配异常
     * @param e
     * @return
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ApiResponse handlerRequestTypeMismatch(TypeMismatchException e) {
        logger.error(e.getMessage(), e);
        String message = String.format("参数类型不匹配，类型应该为: %s,参数值为:%s,属性名称为:%s", e.getRequiredType(),e.getValue(),e.getPropertyName());
        ServletRequestAttributes SERVLET_REQUEST_ATTRIBUTES  = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(SERVLET_REQUEST_ATTRIBUTES !=null){
            HttpServletRequest request = SERVLET_REQUEST_ATTRIBUTES.getRequest();
            logger.error("异常接口{}  异常信息{},",request.getRequestURI(), message);
        }
        return ApiResponse.fail(HttpStatus.SC_BAD_REQUEST,"类型匹配异常");
    }

    /**
     * 错误服务请求参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse handlerRequestMissingServletRequest(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_BAD_REQUEST,"错误服务请求参数异常");
    }

    /**
     *不支持的request请求异常
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse handlerRequest405(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_METHOD_NOT_ALLOWED,"不支持的request方法请求异常");
    }

    /**
     * 不可接受的HTTP协议异常
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ApiResponse handlerRequest406(HttpMediaTypeNotAcceptableException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_NOT_ACCEPTABLE,"不支持的request方法请求异常");
    }

    /**
     * 转换不支持异常
     * @param e
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public ApiResponse handlerServer500(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"转换不支持异常");
    }

    /**
     * 栈溢出异常
     * @param e
     * @return
     */
    @ExceptionHandler(StackOverflowError.class)
    public ApiResponse handlerRequestStackOverflow(StackOverflowError e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"栈溢出异常");
    }

    /**
     * hibernate validator异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        List<FieldError> bindingResult = e.getBindingResult().getFieldErrors();
        logger.debug(String.format("Start : %s : handleDataexception()", this
                .getClass().getSimpleName()));
        StringBuffer errorMesssage = new StringBuffer();
        for(FieldError fieldError: bindingResult){
            errorMesssage.append(fieldError.getDefaultMessage() + "\n");
            logger.debug("Invalid {} value submitted for {}",
                    fieldError.getRejectedValue(), fieldError.getField());
        }
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,errorMesssage.toString());
    }

    /**
     * 数据绑定异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse bindExceptionHandler(BindException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"数据绑定异常");
    }

    /**
     * 超过最大上传数量异常
     * @param e
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResponse maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        logger.error(e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.SC_INTERNAL_SERVER_ERROR,"超过最大上传数量异常");
    }

}
