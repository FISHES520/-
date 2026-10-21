package cn.modernhistory.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> validation(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().stream().findFirst().map(e -> e.getField() + "：" + e.getDefaultMessage()).orElse("请求参数无效");
        return ResponseEntity.badRequest().body(ApiResponse.failure("VALIDATION_ERROR", message));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiResponse<Void>> badRequest(IllegalArgumentException ex) { return ResponseEntity.badRequest().body(ApiResponse.failure("BAD_REQUEST", ex.getMessage())); }
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiResponse<Void>> constraint(ConstraintViolationException ex) { return ResponseEntity.badRequest().body(ApiResponse.failure("VALIDATION_ERROR", ex.getMessage())); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> unexpected(Exception ex) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure("INTERNAL_ERROR", "服务暂时不可用")); }
}
