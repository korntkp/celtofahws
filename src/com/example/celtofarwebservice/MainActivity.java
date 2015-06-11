package com.example.celtofarwebservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private final String NAMESPACE = "http://www.w3schools.com/webservices/";
	private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
	private final String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
	private final String METHOD_NAME = "CelsiusToFahrenheit";
	private String TAG = "PGGURU";
	private static String celcius;
	private static String fahren;
	Button b;
	TextView tv;
	EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Celcius Edit Control
	    et = (EditText) findViewById(R.id.editText1);
	    //Fahrenheit Text control
	    tv = (TextView) findViewById(R.id.tv_result);
	    //Button to trigger web service invocation
	    b = (Button) findViewById(R.id.button1);
	    //Button Click Listener
	    b.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            //Check if Celcius text control is not empty
	            if (et.getText().length() != 0 && et.getText().toString() != "") {
	                //Get the text control value
	                celcius = et.getText().toString();
	                //Create instance for AsyncCallWS
	                AsyncCallWS task = new AsyncCallWS();
	                //Call execute 
	                task.execute();
	            //If text control is empty
	            } else {
	                tv.setText("Please enter Celcius");
	            }
	        }
	    });
	}
	
	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getFahrenheit(celcius);
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //if (fahren == "null"){
/*
            if(checkConn){
            	tv.setText("Please connect the internet.");
            }else{
            	tv.setText(fahren + "° F");
            }
            */
            tv.setText(fahren + "° F");
            
        }
 
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            tv.setText("Calculating...");
        }
 
        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
 
    }
	
	public void getFahrenheit(String celsius) {
	    //Create request
	    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	    //Property which holds input parameters
	    PropertyInfo celsiusPI = new PropertyInfo();
	    //Set Name
	    celsiusPI.setName("Celsius");
	    //Set Value
	    celsiusPI.setValue(celsius);
	    //Set dataType
	    celsiusPI.setType(double.class);
	    //Add the property to request object
	    request.addProperty(celsiusPI);
	    //Create envelope
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	            SoapEnvelope.VER11);
	    envelope.dotNet = true;
	    //Set output SOAP object
	    envelope.setOutputSoapObject(request);
	    //Create HTTP call object
	    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	 
	    try {
	        //Invole web service
	        androidHttpTransport.call(SOAP_ACTION, envelope);
	        //Get the response
	        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	        //Assign it to fahren static variable
	        fahren = response.toString();
	 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static boolean checkConn(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx
	            .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo i = conMgr.getActiveNetworkInfo();
	    if (i == null)
	    	return false;
	    if (!i.isConnected())
	    	return false;
	    if (!i.isAvailable())
	    	return false;
	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
