package com.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        String status,         // Status of the response: "success" or "error"
        String message,        // A message describing the response (or error)
        T data,                // The actual response data (can be any type)
        String errorDetails    // Optional: for error responses, you can include detailed error information
) {
    public ApiResponse(String status, String message, T data) {
        this(status, message, data, null);  // Default constructor without error details
    }
}
