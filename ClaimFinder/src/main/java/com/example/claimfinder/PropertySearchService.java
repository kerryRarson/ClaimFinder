package com.example.claimfinder;

import java.util.ArrayList;

/**
 * Created by klarsen on 9/28/13.
 */
public class PropertySearchService {
    static ArrayList<Address> Search(String lat, String lon, String radius){
        return fakeSearch(radius);
    }
    private static ArrayList<Address> fakeSearch(String miles){
        ArrayList<Address> results = new ArrayList<Address>();

        Address addr = new Address();
        addr.Miles = 5;
        addr.Address1 = "123 Some street";
        addr.Address2 = "";
        addr.City = "Whitefish";
        addr.State = "MT";
        addr.Zip = "59901";
        addr.LatLon = new LatLon();
        addr.LatLon.Latitude = "48.123";
        addr.LatLon.Longitude = "-114.5343";
        results.add(addr);

        addr = new Address();
        addr.Miles = 6;
        addr.Address1 = "99 Another street";
        addr.Address2 = "Apt. 4C";
        addr.City = "Whitefish";
        addr.State = "MT";
        addr.Zip = "59937";
        results.add(addr);

        addr = new Address();
        addr.Miles = 10;
        addr.Address1 = "267 Lion Trial";
        addr.Address2 = "";
        addr.City = "Whitefish";
        addr.State = "MT";
        addr.Zip = "59937";
        results.add(addr);

        addr = new Address();
        addr.Miles = 11;
        addr.Address1 = "267 Main Street";
        addr.Address2 = "";
        addr.City = "Whitefish";
        addr.State = "MT";
        addr.Zip = "59937";
        results.add(addr);

        return results;
    }
}
