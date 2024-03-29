package com.example.feedtheneed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.feedtheneed.domain.usecase.event.EventUseCase;
import com.example.feedtheneed.domain.usecase.event.EventUseCaseInterface;
import com.example.feedtheneed.presentation.event.AddEventActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.feedtheneed.databinding.ActivityMapsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // function to sort hashmap
    public LinkedHashMap<Double, String> sortHashMapByValues(
            HashMap<Double, String> passedMap) {
        List<Double> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Double, String> sortedMap =
                new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next();
            Iterator<Double> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Double key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMarkers();
        checkPermissionAndEnableLocation();
    }

    // function to set markers for events
    public void setMarkers(){
        EventUseCaseInterface eventUseCase = new EventUseCase();

        eventUseCase.getAllEventsFromFirebase().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> listEvents = value.getDocuments();

//                // iterating through the array
                for (DocumentSnapshot documentSnapshot: listEvents) {
                    Log.d("Getting Events", "Events"+documentSnapshot.toString());

                    LatLng eventLocation = new LatLng(Double.valueOf(documentSnapshot.get("eventLat").toString()), Double.valueOf(documentSnapshot.get("eventLong").toString()));
//
//                    mMap.addMarker(new MarkerOptions()
//                            .position(eventLocation)
//                            .title(documentSnapshot.get("eventName").toString()));

                    setMarkerInCoordinate(eventLocation, documentSnapshot.get("eventName").toString());
                }
            }
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
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
                .setInterval(2000)
                .setFastestInterval(2000);
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
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11));
                                fusedLocationClient.removeLocationUpdates(mLocationCallback);


                                HashMap<String, String> nearbyEventHashMap = new HashMap<String, String>();

                                // Getting nearby Events
                                EventUseCaseInterface eventUseCase = new EventUseCase();
                                eventUseCase.getAllNearByEvents(new LatLng(latitude, longitude)).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Log.d("Nearby Event", "Nearby Event"+task.getResult().getDocuments().toString());

                                        List<DocumentSnapshot> listEvents = task.getResult().getDocuments();
                                        float[] distanceBetweenUserAndEvent = new float[1];

                                        for (DocumentSnapshot documentSnapshot: listEvents) {
                                            LatLng eventLocation = new LatLng(Double.valueOf(documentSnapshot.get("eventLat").toString()), Double.valueOf(documentSnapshot.get("eventLong").toString()));

                                            Location.distanceBetween(latitude, longitude,
                                                    eventLocation.latitude, eventLocation.longitude,
                                                    distanceBetweenUserAndEvent);

                                            // Putting value in nearby event Hashmap
                                            nearbyEventHashMap.put(documentSnapshot.getString("eventId"), String.valueOf(distanceBetweenUserAndEvent[0]));

                                            // Creating new hashmap with distance (double) key so it will be easy to sort
                                            HashMap<Double,String> newMap = new HashMap<>();
                                            // filling new hashmap
                                            for(Map.Entry<String,String> entry : nearbyEventHashMap.entrySet())
                                                newMap.put(Double.valueOf(entry.getValue()), entry.getKey());

                                            // Printing nearby events in debug console
                                            Log.d("Nearby Events", "Nearby Events "+sortHashMapByValues(newMap).toString());

                                        }
                                    }
                                });
                            });

                        }
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());

    }

    // function to set marker in a co cordinate
    private void setMarkerInCoordinate(LatLng latLng, String text){
        Log.i("MapsActivity", "setMarkerInCoordinate: "+latLng);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), firebaseUser.getPhotoUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }


        MarkerOptions options = new MarkerOptions().position(latLng)
                .title(text)
//                .icon(addStampToImage(bitmap)
                .icon(createPureTextIcon(text)
                );

        mMap.addMarker(options);


    }


    // creating a BitMapDescriptor from a text
    public BitmapDescriptor createPureTextIcon(String text) {

        Paint textPaint = new Paint(); // Adapt to your needs
        textPaint.setTextSize(50);
        float textWidth = textPaint.measureText(text);
        float textHeight = textPaint.getTextSize();
        int width = (int) (textWidth);
        int height = (int) (textHeight);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.translate(0, height);

        // For development only:
        // Set a background in order to see the
        // full size and positioning of the bitmap.
        // Remove that for a fully transparent icon.
        //canvas.drawColor(Color.LTGRAY);

        canvas.drawText(text, 0, 0, textPaint);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);
        return icon;
    }

    // function to add text below
    private BitmapDescriptor addStampToImage(Bitmap originalBitmap) {

        int extraHeight = (int) (originalBitmap.getHeight() * 2.15);

        Bitmap newBitmap = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight() + extraHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.BLUE);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Resources resources = getResources();
        float scale = resources.getDisplayMetrics().density;

        String text = "Maulik";
        Paint pText = new Paint();
        pText.setColor(Color.WHITE);

        setTextSizeForWidth(pText,(int) (originalBitmap.getHeight() * 0.10),text);

        Rect bounds = new Rect();
        pText.getTextBounds(text, 0, text.length(), bounds);

        Rect textHeightWidth = new Rect();
        pText.getTextBounds(text, 0, text.length(), textHeightWidth);

        canvas.drawText(text, (canvas.getWidth() / 2) - (textHeightWidth.width() / 2),
                originalBitmap.getHeight() + (extraHeight / 2) + (textHeightWidth.height() / 2),
                pText);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(newBitmap);
        return icon;
    }

    // function to set Text size for width
    private void setTextSizeForWidth(Paint paint, float desiredHeight,
                                     String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredHeight / bounds.height();

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }
}