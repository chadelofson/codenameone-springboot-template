/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config;

/**
 *
 * @author Chad
 */
import ca.weblite.codename1.mapper.DataMapper;
import com.example.dto.Mappers;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

/**
 *
 * @author shannah
 */
public class CN1DataMapperMessageConverter extends AbstractHttpMessageConverter {

    public CN1DataMapperMessageConverter() {
        super(new MediaType("application", "cn1"));
        for (DataMapper m : Mappers.getInstance().getContext().values()) {
            m.setOutputDatesAsLongs(true);
        }
    }

    @Override
    protected boolean supports(Class type) {
        return true;
    }

    @Override
    protected Object readInternal(Class type, HttpInputMessage him) throws IOException, HttpMessageNotReadableException {
       System.out.println("In readInternal");
       DataInputStream in = new DataInputStream(him.getBody());
       Map m = (Map)com.codename1.io.Util.readObject(in);
       System.out.println("Read object "+m);
       return Mappers.getInstance().readMap(m, type);
    }

    @Override
    protected void writeInternal(Object t, HttpOutputMessage hom) throws IOException, HttpMessageNotWritableException {
        DataOutputStream out = new DataOutputStream(hom.getBody());
        System.out.println("Writing object of type "+t.getClass());
        
        Map m = (Map)Mappers.getInstance().jsonify(t);
        System.out.println(m);
        com.codename1.io.Util.writeObject(m, out);
    }

}
