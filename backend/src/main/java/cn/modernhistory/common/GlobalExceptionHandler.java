package cn.modernhistory.common;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  ResponseEntity<?> business(BusinessException e) {
    return ResponseEntity.status(e.status).body(ApiResponse.failure(e.code, e.getMessage()));
  }

  @ExceptionHandler(AuthenticationException.class)
  ResponseEntity<?> auth(AuthenticationException e) {
    return ResponseEntity.status(401).body(ApiResponse.failure("UNAUTHORIZED", "邮箱或密码错误"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<?> validation(MethodArgumentNotValidException e) {
    return ResponseEntity.badRequest()
        .body(
            ApiResponse.failure(
                "VALIDATION_ERROR",
                e.getBindingResult().getFieldErrors().get(0).getField()
                    + ": "
                    + e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()));
  }

  @ExceptionHandler({
    IllegalArgumentException.class,
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class,
    org.springframework.web.bind.MissingServletRequestParameterException.class,
    org.springframework.web.multipart.support.MissingServletRequestPartException.class
  })
  ResponseEntity<?> bad(Exception e) {
    return ResponseEntity.badRequest()
        .body(ApiResponse.failure("BAD_REQUEST", "参数格式、必填字段或业务引用不合法"));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  ResponseEntity<?> size(Exception e) {
    return ResponseEntity.status(413).body(ApiResponse.failure("FILE_TOO_LARGE", "单文件上限20MB"));
  }

  @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
  ResponseEntity<?> missing(Exception e) {
    return ResponseEntity.status(404).body(ApiResponse.failure("NOT_FOUND", "接口不存在"));
  }

  @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
  ResponseEntity<?> method(Exception e) {
    return ResponseEntity.status(405).body(ApiResponse.failure("METHOD_NOT_ALLOWED", "请求方法不支持"));
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<?> unexpected(Exception e) {
    org.slf4j.LoggerFactory.getLogger(getClass()).error("Unhandled request", e);
    return ResponseEntity.internalServerError()
        .body(ApiResponse.failure("INTERNAL_ERROR", "服务暂时不可用"));
  }
}
