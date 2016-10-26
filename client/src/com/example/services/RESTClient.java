/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.app.Application;
import com.example.dto.BaseRequest;
import com.example.dto.BaseResponse;
import com.example.dto.UpdateDeviceRequest;
import com.example.dto.UpdateDeviceResponse;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.io.Util;
import com.codename1.push.Push;
import com.codename1.ui.Display;
import com.codename1.util.Callback;
import com.example.dto.Mappers;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author shannah
 */
public class RESTClient implements WebServiceClient {
    
    
    
    /**
     * Sends a request to the server.
     * @param url The url for REST service.  This is not the full URL -- just the portion after the REST service address.
     * @param request The request to send.
     * @return The response from the server
     * @throws IOException If there was a problem connecting to the server.
     */
    public void sendRequest(String url, final BaseRequest request, Callback<BaseResponse> callback) {
        
        if (Push.getPushKey() != null) {
            request.setDeviceId(Push.getPushKey());
        }
        
        ConnectionRequest req = new ConnectionRequest() {

            @Override
            protected void buildRequestBody(OutputStream os) throws IOException {
                Map m = (Map)Mappers.getInstance().jsonify(request);
                DataOutputStream dos = new DataOutputStream(os);
                Util.writeObject(m, dos);
                dos.flush();
            }

            @Override
            protected void readResponse(InputStream input) throws IOException {
                DataInputStream in = new DataInputStream(input);
                Map m = (Map)Util.readObject(in);
                if (m == null) {
                    throw new IOException("Unable to deserialize response.");
                }
                System.out.println(m);
                BaseResponse br =  Mappers.getInstance().readMap(m, BaseResponse.class);
                switch (br.getCode()) {
                    case OK :
                        Display.getInstance().callSerially(()->{
                            callback.onSucess(br);
                        });
                        break;
                    default :
                        Display.getInstance().callSerially(()->{
                            callback.onError(RESTClient.this, new WebServiceError(br), br.getCode().getCode(), br.getMessage());
                        });
                }
            }

            @Override
            protected void handleErrorResponseCode(int code, String message) {
                Display.getInstance().callSerially(()->{
                    callback.onError(RESTClient.this, new WebServiceError(null), code, message);
                });
            }
            
            
            
        };
        
        String baseUrl = Application.BASE_URL() + url;
                
        
        req.setUrl(baseUrl);
        req.setPost(true);
        req.setHttpMethod("POST");
        req.setDuplicateSupported(true);
        
        // This mimetype is just arbitrarily made up.  Just needs to match the one
        // in MobileDeviceRESTServiceConfiguration in the server project
        req.setContentType("application/cn1");
        req.addRequestHeader("accept", "application/cn1");
        req.setFailSilently(true);
        
        NetworkManager.getInstance().addToQueue(req);
        //if (req.getResponseCode() != 200) {
        //    throw new IOException("Failed to connect to server.  Code: "+req.getResponseCode());
        //}
        //byte[] response = req.getResponseData();
        
        
    }

    @Override
    public void updateDeviceLocation(UpdateDeviceRequest request, Callback<UpdateDeviceResponse> callback) {
        sendRequest("updateDevice", request, (Callback)callback);
    }

    
    
    
    
}
