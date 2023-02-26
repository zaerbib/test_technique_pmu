package com.test.pmu.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PmuException extends Exception {
    private String message;
    private String httpMessage;
}
