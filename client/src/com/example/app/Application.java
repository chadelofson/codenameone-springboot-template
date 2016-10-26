/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.app;

import com.example.dto.UpdateDeviceRequest;
import com.example.dto.UpdateDeviceResponse;
import com.example.views.MainForm;
import com.codename1.io.Log;
import com.codename1.io.Properties;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.push.PushCallback;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Toolbar;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.util.CallbackAdapter;
import com.codename1.util.SuccessCallback;
import com.example.services.WebServiceClient;
import java.io.IOException;
import java.io.InputStream;
//import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/** implements PushCallback, LocationListener
 *
 * @author shannah
 */
public class Application implements PushCallback  {
    
    WebServiceClient ws;
    private static String BASE_URL = null; 
    private Resources theme, css;
    private static Application instance;
    private Form current;

    private static String GOOGLE_PROJECT_ID;
    //private static String PUSH_TOKEN;  // Don't need this right now because we're not pushing from device

    private MainForm mainForm;
    
    public static Application getInstance() {
        return instance;
    }
    
    public static Resources getTheme() {
        return instance.theme;
    }
   
    
    public static String BASE_URL() {
        return BASE_URL;
    }
    
    private void loadRuntimeSettings() {
        Properties p = new Properties();
        Map props = new HashMap();
        try {
            InputStream in = Display.getInstance().getResourceAsStream(null, "/runtime.properties");
            if (in != null) {
                try {
                    p.load(in);
                    props.putAll(p);
                } finally {
                    if (in != null) {
                        try {in.close();} catch (Throwable t){}
                    }
                }
            }
            in = Display.getInstance().getResourceAsStream(null, "/runtime.override.properties");
            if (in != null) {
                try {
                    p.load(in);
                    props.putAll(p);
                } finally {
                    if (in != null) {
                        try {in.close();} catch (Throwable t){}
                    }
                }
            }
        } catch (IOException ex) {
            Log.e(ex);
        }
        
        if (p.containsKey("BASE_URL")) {
            BASE_URL = (String)p.get("BASE_URL");
        }
        
        //if (p.containsKey("PUSH_TOKEN")) {
        //    PUSH_TOKEN = (String)p.get("PUSH_TOKEN");
        //}
        
        if (p.containsKey("GOOGLE_PROJECT")) {
            GOOGLE_PROJECT_ID = (String)p.get("GOOGLE_PROJECT");
        }
    }
    
    public void init(Object context) {
        System.out.println("In init");
        theme = UIManager.initFirstTheme("/theme");
        try {
            css = Resources.openLayered("/theme.css");
            UIManager.getInstance().addThemeProps(css.getTheme(css.getThemeResourceNames()[0]));
        } catch (IOException ex) {
            Log.e(ex);
        }
        
        instance = this;

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature, uncomment if you have a pro subscription
        Log.bindCrashProtection(true);
        loadRuntimeSettings();
    }
    
    public void start() {

        if(current != null){
            current.show();
            return;
        }
        startForegroundLocationListener();
        
        mainForm = new MainForm();
        
        if (GOOGLE_PROJECT_ID != null) {
            Display.getInstance().callSerially(() -> {
                System.out.println("Registering device for push notification with SenderID: " + GOOGLE_PROJECT_ID);
                Hashtable metaData = new Hashtable();
                metaData.put(com.codename1.push.Push.GOOGLE_PUSH_KEY, GOOGLE_PROJECT_ID);
                Display.getInstance().registerPush(metaData, true);
            });
        }
        
        
        System.out.println("Showing main form");
        mainForm.show();
    }
    
    public void stop() {
        current = Display.getInstance().getCurrent();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = Display.getInstance().getCurrent();
        }
        stopForegroundLocationListener();
    }
    
    public void destroy() {
        
    }
    
    @Override
    public void push(String value) {

        
    }

    @Override
    public void registeredForPush(String deviceId) {

    }

    @Override
    public void pushRegistrationError(String error, int errorCode) {
//        Dialog.show("Registration Error", "Error " + errorCode + "\n" +
//                error , "OK", null);
        System.out.println("Registration Error: " + errorCode + "\n" + error);
    }

    
    
    private void startForegroundLocationListener() {
        
    }
    
    private void stopForegroundLocationListener() {
        
    }
    
    public Image getImage(String name) {
        Image im = theme.getImage(name);
        if (im == null) {
            im = css.getImage(name);
        }
        return im;

    }

    
    public void updateDeviceLocation(Location loc, final SuccessCallback callback) {
        UpdateDeviceRequest req = new UpdateDeviceRequest();
        req.setLatitude(loc.getLatitude());
        req.setLongitude(loc.getLongitude());
        
        ws.updateDeviceLocation(req, new CallbackAdapter() {

            @Override
            public void onSucess(Object value) {
                callback.onSucess(value);
            }
            
        });
    }
  
}
