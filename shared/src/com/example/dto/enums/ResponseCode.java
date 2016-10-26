/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dto.enums;

/**
 *
 * @author shannah
 */
public enum ResponseCode {
    OK(200),
    ERROR(500);
    private int code;
    
    ResponseCode(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
