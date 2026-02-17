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

    // ================= RESOURCE NOT FOUND =================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        return new ResponseEntity<>(
                new ApiResponse(ex.getMessage(), false),
                HttpStatus.NOT_FOUND
        );
    }

    // ================= VALIDATION ERRORS =================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgsNotValidException(
            MethodArgumentNotValidException ex) {

        Map<String, String> resp = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });

        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    // ================= BAD CREDENTIALS / API ERROR =================
    // 🔥 Durgesh-style: success = true
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {

        return new ResponseEntity<>(
                new ApiResponse(ex.getMessage(), true), // ✅ CHANGED HERE
                HttpStatus.BAD_REQUEST
        );
    }

    // ================= ACCESS DENIED (ROLE ISSUE → 403) =================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(
            AccessDeniedException ex) {

        return new ResponseEntity<>(
                new ApiResponse(
                        "Access Denied: You are not authorized to perform this action",
                        false
                ),
                HttpStatus.FORBIDDEN
        );
    }

    // ================= MULTIPART ERROR =================
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipartException(
            MultipartException ex) {

        return new ResponseEntity<>(
                new ApiResponse(
                        "Please upload image using form-data with key 'image'",
                        false
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // ================= UNSUPPORTED MEDIA TYPE =================
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex) {

        return new ResponseEntity<>(
                new ApiResponse(
                        "Content-Type not supported",
                        false
                ),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    // ================= FALLBACK =================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {

        return new ResponseEntity<>(
                new ApiResponse(
                        "Internal Server Error: " + ex.getMessage(),
                        false
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
