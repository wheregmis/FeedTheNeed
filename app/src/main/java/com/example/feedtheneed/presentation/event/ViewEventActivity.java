package com.example.feedtheneed.presentation.event;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.usecase.event.EventUseCase;
import com.example.feedtheneed.domain.usecase.event.EventUseCaseInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewEventActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private TextView eventTitle;
    private TextView eventType;
    private TextView eventDateTime;
    private TextView eventDescription;

    private String eventIdGlobal;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ArrayList<String> imageUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewevent);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();

        // Initializing the components
        eventTitle = (TextView) findViewById(R.id.event_name);
        eventType = (TextView) findViewById(R.id.event_type);
        eventDescription = (TextView) findViewById(R.id.description);
        eventDateTime = (TextView) findViewById(R.id.date_time);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        eventIdGlobal = bundle.getString("eventId");

        Log.d("EventIdInViewPage", "EventId"+eventIdGlobal);

        EventUseCaseInterface eventUseCase = new EventUseCase();
        eventUseCase.getEventDetails(eventIdGlobal).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                DocumentSnapshot event = task.getResult().getDocuments().get(0);
                eventTitle.setText(event.get("eventName").toString());
                eventType.setText(event.get("eventFoodType").toString());
                eventDescription.setText(event.get("eventDescription").toString());
                String eventDateTimeString = event.get("eventDate").toString() +" "+ event.get("eventTime").toString();

                // TODO: 04/08/2022 Handle image array for sliders @Namrata Miss
                imageUrlList = (ArrayList<String>) event.get("eventImageUrls");
                Log.d("ViewEventActivity", "ImageUrls: "+imageUrlList);
                eventDateTime.setText(eventDateTimeString);
            }
        });

        findViewById(R.id.event_participant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //         TODO: 25/07/2022  Just for testing user participants in event

                eventUseCase.participateInEvent(firebaseUser.getEmail(), eventIdGlobal).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Snackbar snackbar = Snackbar
                                .make(view, "Successfully Added You as a Participants", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });

        findViewById(R.id.bevolunteer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventUseCase.addUserToVolunteerEvent(firebaseUser.getEmail(), eventIdGlobal, view).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("Document Ref", ""+task.getResult().size());
                    }
                });
            }
        });

    }

}