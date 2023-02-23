package com.test.pmu.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PmuException extends Exception {
    private String message;
}
