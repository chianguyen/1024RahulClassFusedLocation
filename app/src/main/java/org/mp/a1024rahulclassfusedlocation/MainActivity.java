package org.mp.a1024rahulclassfusedlocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {      //for location updates

    private GoogleApiClient googleApiClient;
    private Location location;
    private LocationRequest locationRequest;
    private boolean isPermissionGranted;

/*    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();

        buildGoogleApi();
/*        checkPermission = checkPermission(permissions);

        //Check for version
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission(permissions).size()>0){

            }
        }*/

    }
/*
    //check permission
    private ArrayList<String> checkPermission(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    //boolean
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }*/

    //get permission
    private void getPermission() {

        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    //onConnected(null);
                    isPermissionGranted = true;
                }

                if (report.isAnyPermissionPermanentlyDenied()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle("Need Permission");
                    builder.setMessage("We need your permission to access your current location. Please grant the access.");
                    builder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 101);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }


            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).onSameThread().check();
    }

    private void buildGoogleApi(){

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    //Good practice
    @Override
    protected void onStart() {
        super.onStart();

        if(googleApiClient != null){
            googleApiClient.connect();
        }
    }

    private void getLocation(){

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);

        if(isPermissionGranted){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //if permission is granted

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            Toast.makeText(MainActivity.this, "Latitude: " + location.getLatitude() + "\n" + "Longitude: "+ location.getLongitude(), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
        }

        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location){
        if(location != null){
            Toast.makeText(MainActivity.this, "Change:\n"
                    + "Latitude: " + location.getLatitude() + "\n"
                    + "Longitude: "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

}
