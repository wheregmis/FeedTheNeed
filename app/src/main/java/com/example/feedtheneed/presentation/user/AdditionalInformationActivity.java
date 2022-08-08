package com.example.feedtheneed.presentation.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.feedtheneed.HomeActivity;
import com.example.feedtheneed.MapsActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.databinding.ActivityMapsBinding;
import com.example.feedtheneed.domain.model.User;
import com.example.feedtheneed.domain.usecase.user.UserUseCaseInterface;
import com.example.feedtheneed.domain.usecase.user.UserUserUseCase;
import com.example.feedtheneed.presentation.authentication.AuthActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public class AdditionalInformationActivity extends AppCompatActivity implements OnMapReadyCallback {
    Button btnNext;
    CheckBox isRestaurant;
    TextView txtDisplayName;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    boolean isRestaurantVariable;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_information);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnNext = (Button) findViewById(R.id.btnNext);
        txtDisplayName = findViewById(R.id.tvUserFullName);
        isRestaurant = findViewById(R.id.cbIsARestaurant);

        FirebaseApp.initializeApp(AdditionalInformationActivity.this);


        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();

        txtDisplayName.setText(firebaseUser.getDisplayName());
        // Check condition
//        if(firebaseUser==null)
//        {
//            // When user already sign in
//            // redirect to profile activity
//            startActivity(new Intent(AdditionalInformationActivity.this, ProfileActivity.class)
//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidated = true;

                if(txtDisplayName.getText().toString().length() == 0){
                    txtDisplayName.setError("Please input event participants limit !!");
                    txtDisplayName.requestFocus();
                    isValidated = false;
                }
                if (userLatLng == null ){
                    Snackbar snackbar = Snackbar
                            .make(view, "Please click on a map to select the location !!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    isValidated = false;
                }
                if (isValidated){
                    insertDataToFirebase().addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            // Redirect user to dashboard
                            startActivity(new Intent(AdditionalInformationActivity.this
                                    , HomeActivity.class)
                            );
                        }
                    });
                }



            }
        });
    }


    Task<DocumentReference> insertDataToFirebase(){
        if (isRestaurant.isChecked()){
            isRestaurantVariable = true;
        }else{
            isRestaurantVariable = false;
        }

        //userLatLng = new LatLng(43.6532, -79.38);

        User user = new User(
                firebaseUser.getUid(),
                txtDisplayName.getText().toString(),
                firebaseUser.getEmail(),
                isRestaurantVariable,
                String.valueOf(userLatLng.latitude),
                String.valueOf(userLatLng.longitude)

        );

        UserUseCaseInterface useCase = new UserUserUseCase();
        return useCase.addUserToFirebase(user);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermissionAndEnableLocation();

        // setting up click listener to place marker in mapview
        mMap.setOnMapClickListener(latLng -> {
            googleMap.clear();
            LatLng current1 = new LatLng(latLng.latitude, latLng.longitude);
            // TODO: 20/07/2022 This is where you handle a lat alng
            userLatLng = current1;
            googleMap.addMarker(new MarkerOptions()
                    .position(current1)
                    .title("Selected Location"));
        });
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkPermissionAndEnableLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        mMap.setMyLocationEnabled(true);
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissionAndEnableLocation();
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setInterval(20)
                .setFastestInterval(200);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        final LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

            }
        };
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (fusedLocationClient != null) {
                        if (location != null) {
                            mMap.setOnMapLoadedCallback(() -> {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));
                                fusedLocationClient.removeLocationUpdates(mLocationCallback);
                            });

                        }
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());

    }


}