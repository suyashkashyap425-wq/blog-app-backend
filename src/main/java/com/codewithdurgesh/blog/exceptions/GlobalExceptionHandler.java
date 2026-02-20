package com.codewithdurgesh.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.codewithdurgesh.blog.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========= RESOURCE NOT FOUND =========
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse(ex.getMessage(), false),
                HttpStatus.NOT_FOUND
        );
    }

    // ========= VALIDATION ERROR =========
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError)err).getField();
            String msg = err.getDefaultMessage();
            errors.put(field,msg);
        });

        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    // ========= CUSTOM API ERROR =========
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex){
        return new ResponseEntity<>(
                new ApiResponse(ex.getMessage(), false),
                HttpStatus.BAD_REQUEST
        );
    }

    // ========= ACCESS DENIED =========
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex){
        return new ResponseEntity<>(
                new ApiResponse("You are not authorized to access this resource", false),
                HttpStatus.FORBIDDEN
        );
    }

    // ========= IMAGE UPLOAD ERROR =========
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipart(MultipartException ex){
        return new ResponseEntity<>(
                new ApiResponse("Upload image using form-data key = image", false),
                HttpStatus.BAD_REQUEST
        );
    }

    // ========= MEDIA TYPE =========
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMedia(HttpMediaTypeNotSupportedException ex){
        return new ResponseEntity<>(
                new ApiResponse("Unsupported Content-Type", false),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    // ========= FALLBACK =========
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneric(Exception ex){
        return new ResponseEntity<>(
                new ApiResponse("Internal Server Error", false),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
