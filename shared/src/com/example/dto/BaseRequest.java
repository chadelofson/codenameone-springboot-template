/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dto;

import java.util.HashMap;

/**
 *
 * @author shannah
 */
public class BaseRequest {
    private int id;
    private String deviceId;
    private HashMap requestData;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    } 

    /**
     * @return the requestData
     */
    public HashMap getRequestData() {
        return requestData;
    }

    /**
     * @param requestData the requestData to set
     */
    public void setRequestData(HashMap requestData) {
        this.requestData = requestData;
    }
    
}
