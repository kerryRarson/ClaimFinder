package com.example.claimfinder;

/**
 * Created by klarsen on 9/22/13.
 */

import java.util.ArrayList;

import com.example.claimfinder.MainActivity.InnerLocationListener;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultsList extends ListActivity {
    private ProgressDialog progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_list);

        //Get the current location
        LocationManager locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new InnerLocationListener();

        locMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locListener);

        ArrayList<Address> results = (ArrayList<Address>) getIntent().getSerializableExtra("addresses");
        int miles = getIntent().getIntExtra("miles", 0);
        Spinner lstMiles = (Spinner)findViewById(R.id.spinner1);
        lstMiles.setSelection(miles);
        TextView latLon = (TextView)findViewById(R.id.lblCurrentLocation);
        latLon.setText(getIntent().getStringExtra("LatLon"));

        setListAdapter(new ArrayAdapter<Address>(this, android.R.layout.simple_list_item_1, results));
    }

    public void btnSearchClick(View view){
        Spinner miles = (Spinner)findViewById(R.id.spinner1);
        progressDlg = ProgressDialog.show(this, "Searching...", "Getting properties in a " + String.valueOf(miles.getSelectedItem()) + " mile radius...");
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
            TextView latLon = (TextView)findViewById(R.id.lblCurrentLocation);
            Thread.sleep(2000);

            ArrayList<Address> addressList =  new ArrayList<Address>();
            Intent i = new Intent(this, SearchResultsList.class);
            i.putExtra("addresses", addressList);
            i.putExtra("miles", miles.getSelectedItemPosition());
            i.putExtra("LatLon", latLon.getText() );
            startActivity(i);
            //Toast.makeText(getApplicationContext(), String.valueOf(addressList.size()) + " returned.", Toast.LENGTH_SHORT);

        }
        catch (Exception err){
            Toast.makeText(getApplicationContext(), err.toString(), Toast.LENGTH_LONG).show();
        }
        finally{
            progressDlg.dismiss();
        }

    }

    public void locClick(View view){
        TextView latLon = (TextView)findViewById(R.id.lblCurrentLocation);
        String mapUri = "geo:" + latLon.getText();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mapUri)));
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.search_results_list, menu);
    //    return true;
    //}

    //GPS listener
    public class InnerLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location loc)
        {
            Log.d("ClaimFinder", "onLocationChanged " + loc.toString());
            LatLon.Latitude =Double.toString(loc.getLatitude());
            LatLon.Longitude = Double.toString(loc.getLongitude());

            String locMessage = "Lat=" + loc.getLatitude() + " Lon=" + loc.getLongitude();
//Log.d("SearchResultsList", locMessage);

           // Toast.makeText(getApplicationContext(), locMessage, Toast.LENGTH_SHORT).show();
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
            Log.d("ClaimFinder", "onStatusChanged");
        }
    }/* End of Class MyLocationListener */

}
