/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.app.Application;
import com.codename1.io.Preferences;
import com.codename1.location.Location;
import com.codename1.location.LocationListener;
import com.codename1.util.Callback;
import com.codename1.util.CallbackAdapter;

/**
 *
 * @author Chad
 */
public class BackgroundLocationListener implements LocationListener {

    @Override
    public void locationUpdated(Location location) {
        //String deviceId = Long.toString(Preferences.get("DeviceId__$", (long)-1));
        Application.getInstance().updateDeviceLocation(location, new CallbackAdapter());
    }

    @Override
    public void providerStateChanged(int newState) {
        
    }
    
}

