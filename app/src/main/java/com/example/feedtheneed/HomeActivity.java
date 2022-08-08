package com.example.feedtheneed;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.presentation.event.AddEventActivity;
import com.example.feedtheneed.presentation.event.ViewEventActivity;
import com.example.feedtheneed.presentation.user.ProfileActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedtheneed.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CircularImageView imageView;
    Toolbar toolbar;
    SearchView mSearchView;
    AutoCompleteTextView autoCompleteTextView;
    MultiAutoCompleteTextView multiAutoCompleteTextView;
    ArrayList<Event> eventList = new ArrayList<>();

    private static final ArrayList<String> EVENTS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarHome.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menubar);
        toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Add Activity");
        setSupportActionBar(toolbar);

//        imageView.findViewById(R.id.imageView);
        firebaseAuth= FirebaseAuth.getInstance();

        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();

//        // Check condition
//        if(firebaseUser!=null)
//        {
//            // When firebase user is not equal to null
//            // Set image on image view
//            Glide.with(HomeActivity.this)
//                    .load(firebaseUser.getPhotoUrl())
//                    .into(imageView);
//        }
        // todo have to check default action bar meny icon and setting icon
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.menubar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        imageView = findViewById(R.id.imageView);
        Glide.with(HomeActivity.this)
                .load(firebaseUser.getPhotoUrl())
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to test -> LeaderboardActivity
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, EVENTS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.search);
        textView.setAdapter(adapter);
        updateEventList();

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String eventName = EVENTS.get(i);
                Event selectedEvent = null;
                for (Event event: eventList) {
                    if (event.getEventName().equals(eventName)) {
                        selectedEvent = event;
                        break;
                    }
                }

                if (selectedEvent != null) {
                    Intent intent = new Intent(HomeActivity.this, ViewEventActivity.class);

                    //Create the bundle
                    Bundle bundle = new Bundle();

                    //Add your data to bundle
                    bundle.putString("eventId", selectedEvent.getEventId());

                    //Add the bundle to the intent
                    intent.putExtras(bundle);

                    //Fire that second activity
                    startActivity(intent);
                }
            }
        });
    }

    private void updateEventList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("event").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> listEvents = value.getDocuments();

//                // iterating through the array
                for (DocumentSnapshot documentSnapshot: listEvents) {
                    LatLng eventLocation = new LatLng(Double.valueOf(documentSnapshot.get("eventLat").toString()), Double.valueOf(documentSnapshot.get("eventLong").toString()));

                    eventList.add(documentSnapshot.toObject(Event.class));
                }

                for (Event event: eventList) {
                    EVENTS.add(event.getEventName());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}