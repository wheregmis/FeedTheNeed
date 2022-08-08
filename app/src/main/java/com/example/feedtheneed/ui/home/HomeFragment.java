package com.example.feedtheneed.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.feedtheneed.domain.usecase.user.UserUseCaseInterface;
import com.example.feedtheneed.domain.usecase.user.UserUserUseCase;
import com.example.feedtheneed.presentation.chat.ChatActivity;
import com.example.feedtheneed.CustomViewPagerAdapter;
import com.example.feedtheneed.R;
import com.example.feedtheneed.data.repository.LeaderboardRepoImplementation;
import com.example.feedtheneed.databinding.FragmentHomeBinding;
import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.usecase.event.EventUseCase;
import com.example.feedtheneed.domain.usecase.event.EventUseCaseInterface;
import com.example.feedtheneed.presentation.chat.ChatListActivity;
import com.example.feedtheneed.presentation.event.AddEventActivity;
import com.example.feedtheneed.presentation.event.ViewEventActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private ImageView ivBottomSheet;
    private BottomSheetDialog dialog;
    private ViewPager eventsViewPager;
    private ArrayAdapter mArrayAdapter;

    private GoogleMap mMap;
    private MapView mMapView;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private ArrayList<Event> nearbyEvents;
    private ArrayList<Event> involvedEvents;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    HashMap<Integer, String> tabs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = inflater.inflate(R.layout.fragment_home, null, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mMapView = (MapView) root.findViewById(R.id.mapView);
        nearbyEvents = new ArrayList<>();
        involvedEvents = new ArrayList<>();

//        searchView.setActivated(true);
//        searchView.onActionViewExpanded();
//        searchView.setIconified(false);
//        searchView.clearFocus();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                Toast.makeText(getActivity(), newText, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        bottom_sheet = root.findViewById(R.id.lvBottomSheetBehaviour);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        ivBottomSheet = root.findViewById(R.id.mazimizeBottomSheet);

        binding.floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddEventActivity.class));
            }
        });

        binding.fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatListActivity.class));
            }
        });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btn_bottom_sheet.setText("Expand Sheet");
                        binding.floatAdd.setVisibility(View.VISIBLE);
                        binding.fabList.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                binding.floatAdd.setVisibility(View.INVISIBLE);
                binding.fabList.setVisibility(View.INVISIBLE);
            }
        });

//        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();

        UserUseCaseInterface userUseCase = new UserUserUseCase();
        userUseCase.getUserFromFirebase(firebaseUser.getEmail()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().getDocuments().get(0).get("restaurant").toString().equals("true")){
                    binding.floatAdd.setVisibility(View.VISIBLE);
                }
            }
        });

        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(eventsViewPager);
        tabs = MyTabbedView.getInstance().getTabs();
        eventsViewPager = (ViewPager) root.findViewById(R.id.eventsViewPager);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        setMarkers();
        checkPermissionAndEnableLocation();
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

                    setMarkerInCoordinate(eventLocation, documentSnapshot.get("eventName").toString(), documentSnapshot.get("eventId").toString());
                }
            }
        });

        // TODO: 28/07/2022 Getting Involved Projects
        HashMap<String, String> involvedEventHashMap = new HashMap<String, String>();

        eventUseCase.getInvolvedEvents("get2sabin@gmail.com").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> listEvents = task.getResult().getDocuments();
                float[] distanceBetweenUserAndEvent = new float[1];

                for (DocumentSnapshot documentSnapshot: listEvents) {
                    LatLng eventLocation = new LatLng(Double.valueOf(documentSnapshot.get("eventLat").toString()), Double.valueOf(documentSnapshot.get("eventLong").toString()));

                    involvedEvents.add(documentSnapshot.toObject(Event.class));
                    Location.distanceBetween(latitude, longitude,
                            eventLocation.latitude, eventLocation.longitude,
                            distanceBetweenUserAndEvent);

                    // Putting value in nearby event Hashmap
                    involvedEventHashMap.put(documentSnapshot.getString("eventId"), String.valueOf(distanceBetweenUserAndEvent[0]));

                    // Creating new hashmap with distance (double) key so it will be easy to sort
                    HashMap<Double, String> newMap = new HashMap<>();
                    // filling new hashmap
                    for (Map.Entry<String, String> entry : involvedEventHashMap.entrySet())
                        newMap.put(Double.valueOf(entry.getValue()), entry.getKey());

                    newMap = sortHashMapByValues(newMap);
                }

                updateInvolvedEvents();
            }
        });

        // TODO: 29/07/2022 Leaderboard Testing
        // TODO: 29/07/2022 please handle the return type of those function as per need

        LeaderboardRepoImplementation implementation = new LeaderboardRepoImplementation();
        implementation.getTopRestaurantsBasedOnEventHosted();

        // TODO: 29/07/2022 Leaderboard Testing
        // TODO: 29/07/2022 Please handle the return types as per the need
        implementation.getTopUserBasedOnVolunteerEvent();

    }

    private void updateInvolvedEvents() {
        eventsViewPager.setAdapter(new CustomViewPagerAdapter(getActivity(), tabs, nearbyEvents, involvedEvents));
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkPermissionAndEnableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);
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
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        final LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

            }
        };
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
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

                                            nearbyEvents.add(documentSnapshot.toObject(Event.class));
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

                                            newMap = sortHashMapByValues(newMap);

                                            // Printing nearby events in debug console
                                            Log.d("Nearby Events", "Nearby Events "+newMap.toString());
                                        }

//                                        updateNearbyEvents();
                                    }
                                });
                            });

                        }
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());

    }

    private void updateNearbyEvents() {
        eventsViewPager.setAdapter(new CustomViewPagerAdapter(getActivity(), tabs, nearbyEvents, involvedEvents));
    }

    // function to set marker in a co cordinate
    private void setMarkerInCoordinate(LatLng latLng, String text, String eventId){
        Log.i("MapsActivity", "setMarkerInCoordinate: "+latLng);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), firebaseUser.getPhotoUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }


        MarkerOptions options = new MarkerOptions().position(latLng)
                .title(text)
                .snippet(eventId)
//                .icon(addStampToImage(bitmap)
                .icon(createPureTextIcon(text)
                );

        mMap.addMarker(options);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Intent i = new Intent(getActivity(), ViewEventActivity.class);

                //Create the bundle
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("eventId", marker.getSnippet());

                //Add the bundle to the intent
                i.putExtras(bundle);

                //Fire that second activity
                startActivity(i);
                return true;
            }
        });


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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public static class MyTabbedView {
        private static HashMap<Integer, String> tabs;
        static MyTabbedView instance;

        public MyTabbedView () {
            tabs = new HashMap<>();
        }

        public static HashMap<Integer, String> getTabs () {
            tabs.put(0, "Nearby Events");
            tabs.put(1, "Involved Events");

            return tabs;
        }

        public static MyTabbedView getInstance () {
            if (tabs == null) {
                instance = new MyTabbedView();
            }

            return instance;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView;
        SearchManager searchManager = (SearchManager)getActivity().
                getSystemService(Context.SEARCH_SERVICE);
        inflater.inflate(R.menu.event_search, menu);
        MenuItem mSearch = menu.findItem(R.id.app_bar_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search here");
        mSearchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                Toast.makeText(getActivity(), newText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}