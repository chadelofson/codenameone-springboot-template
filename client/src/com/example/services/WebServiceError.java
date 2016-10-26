/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.dto.BaseResponse;

/**
 *
 * @author shannah
 */
public class WebServiceError extends Exception {
    private BaseResponse response;
    
    public WebServiceError(BaseResponse response) {
        super(response != null ? response.getMessage() : "Null response");
        this.response = response;
    }

    /**
     * @return the response
     */
    public BaseResponse getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(BaseResponse response) {
        this.response = response;
    }
    
}
