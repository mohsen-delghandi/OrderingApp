package com.example.mohsen.orderingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Mohsen on 2017-06-16.
 */

public class CallWebService {

    public String SOAP_ACTION = "http://tempuri.org/";
    public String OPERATION_NAME;
    public String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public String SOAP_ADDRESS;
    public String SOAP_PROPERTY_NAME;

    public CallWebService(Context context, String methodName, String propertyName) {
        SQLiteDatabase db = new MyDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(MyDatabase.FOOD_TABLE,new String[]{MyDatabase.NAME},null,null,null,null,null);
        cursor.moveToFirst();
        SOAP_ADDRESS = "http://" + cursor.getString(0) + "/CRMWebService.asmx";
//        SOAP_ADDRESS = "http://192.168.1.34/CRMWebService.asmx";
        SOAP_ACTION += methodName;
        OPERATION_NAME = methodName;
        SOAP_PROPERTY_NAME = propertyName;
        cursor.close();
        db.close();
    }

    public String Call(String s){

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi=new PropertyInfo();
        pi.setName(SOAP_PROPERTY_NAME);
        pi.setValue(s);
        pi.setType(String.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS,2000);

        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }
}