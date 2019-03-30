package com.example.secureyourchildchild;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChildMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleMap mMap;
    public static final int MY_PERMISSTION_REQUESR_CODE=1996;
    public static final int PLAY_SERVICE_RESOLUTION_REQUEST=890;
    private GoogleApiClient mgoogleApiClient;
    private Location lastlocation;
    private LocationRequest mlocationRequest;
    private LocationCallback locationCallback;
    private int UPDATE_INTERVAL=5000;
    private int FASTEST_INTERVAL=3000;
    private int Displacement=10;
    Marker currentmarker;
    DatabaseReference ref;
    GeoFire geoFire;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String child_location_refrence="All_Child_Location_GeoFire";
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    SharedPreferences.Editor sharedEditor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference(child_location_refrence);
        geoFire=new GeoFire(ref);
        setUpLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSTION_REQUESR_CODE:
                if (grantResults.length<0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if (checkPlayservice()){
                        builtGoogleApiCliect();
                        createLocatinoRequest();
                        DisplayLocation();
                    }
                }
                break;
        }
    }
    private void createLocatinoRequest() {
        mlocationRequest=new LocationRequest();
        mlocationRequest.setInterval(UPDATE_INTERVAL);
        mlocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setSmallestDisplacement(Displacement);
    }

    private void builtGoogleApiCliect() {
        mgoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mgoogleApiClient.connect();
    }
    private boolean checkPlayservice() {
        int resultcode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode!= ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode)){
                GooglePlayServicesUtil.getErrorDialog(resultcode,this,PLAY_SERVICE_RESOLUTION_REQUEST).show();
            }
            else {
                Toast.makeText(this, "This Device Is Not Supported 😭", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //Request runtime permission
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSTION_REQUESR_CODE);
        }
        else{
            if (checkPlayservice()){
                builtGoogleApiCliect();
                createLocatinoRequest();
                DisplayLocation();
            }
        }
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
//        locationCallback=new LocationCallback();
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleApiClient,mlocationRequest,this);
//        fusedLocationProviderClient.requestLocationUpdates(mlocationRequest,locationCallback,null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(23.7545861, 90.3752816);
        currentmarker=mMap.addMarker(new MarkerOptions().position(sydney).title("Daffodil International University"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.7545861, 90.3752816), 12.0f));
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        currentmarker=mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.setTrafficEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.child_map_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.map_parent_add_bychild:
                //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                //Toast.makeText(this, "Parent added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChildMapActivity.this,AddPatentActivityFromChild.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        DisplayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mgoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation=location;
        DisplayLocation();
    }
    private void DisplayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        lastlocation= LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
        if (lastlocation!=null){
            final double latitude=lastlocation.getLatitude();
            final double longitude=lastlocation.getLongitude();
            geoFire.setLocation(firebaseUser.getUid(), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (currentmarker!=null){
                        currentmarker.remove();
                        currentmarker=mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("Your Location"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),12.0f));
                        mMap.setTrafficEnabled(true);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                    }
                    else {
                        currentmarker=mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title("you"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),12.0f));

                    }
                    Log.d("EDMTDEV",String.format("Your location was changed : %f / %f ",latitude,longitude));
                }
            });

        }
        else {
            Log.d("EDMTDEV","Can not get Location");
        }

    }

}
