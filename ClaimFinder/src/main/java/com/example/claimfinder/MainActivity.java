package com.example.claimfinder;

/**
 * Created by klarsen on 9/22/13.
 * 
 * PLEASE REFACTOR THIS TO extend ActivityList
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import java.util.Map;

import com.example.claimfinder.LatLon;
import com.example.claimfinder.Address;
//import job.engine.homescreen.MyLocationListener;
import android.app.ListActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Thread searchThread;
    private ProgressDialog progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the current location
        LocationManager locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new InnerLocationListener();

        locMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void locClick(View view){
        TextView latLon = (TextView)findViewById(R.id.lblCurrentLocation);
        String mapUri = "geo:" + latLon.getText();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mapUri)));
    }
    public void btnSearchClick(View view){
        Spinner miles = (Spinner)findViewById(R.id.spinner1);
        progressDlg = ProgressDialog.show(this, "Searching...", "Getting properties in a " + String.valueOf(miles.getSelectedItem()) + " mile radius, Please Wait...");
        //Toast.makeText(getApplicationContext(), String.valueOf(miles.getSelectedItem()) + " - was selected.", Toast.LENGTH_SHORT).show();
        this.StartSearch();
    }
    private void StartSearch(){
        new Thread( new Runnable(){
            public void run(){
                searchForProperties();
            }
        }).start();
    }
    protected void searchForProperties(){
        try{

            Spinner miles = (Spinner)findViewById(R.id.spinner1);
            TextView latLon = (TextView)findViewById(R.id.currentLon);
            Thread.sleep(5000);

            String lon = latLon.getText().toString();
            latLon = (TextView)findViewById(R.id.currentLat);
            String lat = latLon.getText().toString();

            ArrayList<Address> addressList = PropertySearchService.Search( lat, lon, miles.toString());

            Intent i = new Intent(this, SearchResultsList.class);
            i.putExtra("addresses", addressList);
            i.putExtra("miles", miles.getSelectedItemPosition());
            i.putExtra("LatLon", lat + ", " + lon );
            Log.d("searchForProperties", String.valueOf(lat + ", " + lon));
            startActivity(i);
            //Toast.makeText(getApplicationContext(), String.valueOf(addressList.size()) + " returned.", Toast.LENGTH_SHORT);
            //setListAdapter(new ArrayAdapter<Address>(this, android.R.layout.simple_list_item_1, addressList));
        }
        catch (Exception err){
            Log.e("searchForProperties Error", err.toString());
            Toast.makeText(getApplicationContext(), err.toString(), Toast.LENGTH_LONG).show();
        }
        finally{
            progressDlg.dismiss();
        }

    }


    //GPS listener
    public class InnerLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc)
        {
            LatLon.Latitude =Double.toString(loc.getLatitude());
            LatLon.Longitude = Double.toString(loc.getLongitude());

            String locMessage = "Lat=" + loc.getLatitude() + " Lon=" + loc.getLongitude();

            TextView txtLat = (TextView)findViewById(R.id.currentLat);
            Double lat = loc.getLatitude();
            txtLat.setText(lat.toString());

            TextView txtLon = (TextView)findViewById(R.id.currentLon);
            Double lon = loc.getLongitude();
            txtLon.setText(lon.toString());

            //Toast.makeText(getApplicationContext(), locMessage, Toast.LENGTH_SHORT).show();
            TextView lblLoc = (TextView)findViewById(R.id.lblCurrentLocation);
            lblLoc.setText(locMessage);

            Button btnSearch = (Button)findViewById(R.id.btnSubmit);
            btnSearch.setEnabled(true);

            lblLoc.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locClick(view);
                }
            });
        }
        @Override
        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT ).show();
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }/* End of Class MyLocationListener */

}
