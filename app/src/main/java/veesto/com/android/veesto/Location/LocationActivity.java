package veesto.com.android.veesto.Location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import veesto.com.android.veesto.R;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int PERMISSIONS_REQUEST_WIFI_ID = 3;

    private ListView listOfNetworks;
    private WifiManager wifi;
    private List<ScanResult> results;
    private BroadcastReceiver receiver;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        requestPermissions();

        listOfNetworks = (ListView) findViewById(R.id.list_of_networks);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled())
        {
            wifi.setWifiEnabled(true);
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                results = wifi.getScanResults();
                onWifiListReady(results);
            }
        };
        wifi.startScan();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_WIFI_ID);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showNoGPSMessage();
        }
        updateCurrentLocation(locationManager);

    }

    private void updateCurrentLocation(LocationManager locationManager) {
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        Location currentLocation = locationManager.getLastKnownLocation(provider);
        // try another way to get location
        if(currentLocation == null)
        {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        // if the current location available
        if(currentLocation != null)
        {
            // move camera to current location
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WIFI_ID: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    // if the request refused, finish
                    finish();
                }
            }
        }
    }

    private void onWifiListReady(final List<ScanResult> networks)
    {
        // get all available networks
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> networksName = new ArrayList<>();
                for(int i = 0 ; i < networks.size(); i++)
                {
                    networksName.add(networks.get(i).SSID);
                }
                updateList(networksName);
            }
        });
        thread.start();



    }

    private void updateList(final ArrayList<String> networksName)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listOfNetworks.setAdapter(new WifiListAdapter(LocationActivity.this, R.layout.netword_card, networksName));
            }
        });
    }

    public void showNoGPSMessage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
