/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config;


import com.example.dto.UpdateDeviceRequest;
import com.example.dto.UpdateDeviceResponse;
import com.example.dto.enums.ResponseCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shannah
 */
@RestController
public class RESTService {

    private class Location {
        double latitude;
        double longitude;
        
        Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
    
    Map<String,Location> deviceLocations = new HashMap<String,Location>();
    
    private boolean sendPush(String[] deviceIds, String message, String type) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String did : deviceIds) {
            sb.append("&device=").append(did);
        }
        String device = sb.toString();
        HttpURLConnection connection = (HttpURLConnection) new URL("https://push.codenameone.com/push/push").openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        
        //String device = "&device=6430232162074624&device=6095332624039936";
        
        String query = "token=" + RESTServiceConfiguration.getPushToken() + device +
                "&type=" + type +
                "&auth=" + RESTServiceConfiguration.getGcmApiKey() +
                "&certPassword=" + RESTServiceConfiguration.getIOSPushCertPassword() +
                "&cert=" + RESTServiceConfiguration.getIOSPushCertURL() +
                "&body=" + URLEncoder.encode(message, "UTF-8") +
                "&production=false";
                
        System.out.println("Query is "+query);
        try (OutputStream output = connection.getOutputStream()) {
            output.write(query.getBytes("UTF-8"));
        }
//        catch (IOException ex) {
//            Logger.getLogger(RESTService.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        int responseCode = connection.getResponseCode();
        System.out.println("Push response code "+responseCode);
        switch (responseCode) {
            case 200:
            case 201: {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                System.out.println(sb.toString());
                break;
            }
            default: {
                
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                System.out.println(sb.toString());
                
                return false;
            }
                
        }
        
        return true;
    }
    
    
    
    
    @RequestMapping(value = "/updateDevice", method = RequestMethod.POST)
    public UpdateDeviceResponse updateDevice(@RequestBody UpdateDeviceRequest request) {
        deviceLocations.put(request.getDeviceId(), new Location(request.getLatitude(), request.getLongitude()));
        UpdateDeviceResponse response = new UpdateDeviceResponse();

        response.setCode(ResponseCode.OK);
        response.setMessage("Device location updated");

        return response;
    }
    
    
}