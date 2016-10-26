/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dto;

import com.example.dto.enums.ResponseCode;
import java.util.HashMap;

/**
 *
 * @author shannah
 */
public class BaseResponse {
    private ResponseCode code;
    private String message;
    private HashMap responseData;

    /**
     * @return the code
     */
    public ResponseCode getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(ResponseCode code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the responseData
     */
    public HashMap getResponseData() {
        return responseData;
    }

    /**
     * @param responseData the responseData to set
     */
    public void setResponseData(HashMap responseData) {
        this.responseData = responseData;
    }
    
}
