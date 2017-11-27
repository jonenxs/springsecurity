package com.nxs.web.controller;

import com.nxs.Exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> hanlerUserException(UserNotExistException exception) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", exception.getId());
        result.put("message", exception.getMessage());
        return result;
    }
}
