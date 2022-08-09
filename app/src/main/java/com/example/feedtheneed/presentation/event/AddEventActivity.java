package com.example.feedtheneed.presentation.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.example.feedtheneed.HomeActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;
import com.example.feedtheneed.domain.usecase.event.EventUseCase;
import com.example.feedtheneed.domain.usecase.event.EventUseCaseInterface;
import com.example.feedtheneed.domain.usecase.user.UserUseCaseInterface;
import com.example.feedtheneed.domain.usecase.user.UserUserUseCase;
import com.example.feedtheneed.presentation.authentication.AuthActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.firestore.FirebaseFirestore;


import android.os.Bundle;
import android.provider.MediaStore;
import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Calendar calendar;
    private TextView dateview;
    private TextView timeview;
    private TextView eventName;
    private TextView eventHost;
    private TextView eventFoodType;
    private TextView eventTotalParticipants;
    StorageReference childRef, imagesRef,storageRef;
    Uri imageUri;
    private TextView eventDescription;
    private int year, month, day,hour,minute;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    GoogleMap nMap;
    LatLng eventLocation;
    ArrayList<Uri> imagesEncodedList = new ArrayList<Uri>();
    ArrayList<String> imagesUploaded = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
//        FirebaseStorage storage = FirebaseStorage.getInstance();
        dateview = (TextView) findViewById(R.id.dateview);
        eventName = (TextView) findViewById(R.id.eventName);
        eventHost = (TextView) findViewById(R.id.eventHost);
        eventDescription = (TextView) findViewById(R.id.eventDescription);
        timeview = (TextView) findViewById(R.id.timeview);
        eventFoodType = (TextView) findViewById(R.id.food_type);
        eventTotalParticipants = (TextView) findViewById(R.id.participants_number);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        // Create a child reference
// imagesRef now points to "images"
        imagesRef = storageRef.child("gs://feedthe-243fc.appspot.com");

// Child references can also take paths
// spaceRef now points to "images/space.jpg
// imagesRef still points to "images"
       childRef = storageRef.child("images/space.jpg");

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
      minute = calendar.get(Calendar.MINUTE);
        showDate(year, month+1, day);
        showTime(hour, minute);

        setUpMap();

        FirebaseApp.initializeApp(AddEventActivity.this);

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        firebaseUser=firebaseAuth.getCurrentUser();


    }

    // method to set up map fragment
    void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                        Toast.LENGTH_SHORT)
                .show();
    }
    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca",
                        Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 998) {
            return new TimePickerDialog(this,
                    myTimeListener, hour, minute, DateFormat.is24HourFormat(this));

        }
        return null;
    }
    public void addImages(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,3);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        Log.d("image","selected");
        if(resultCode == RESULT_OK && data!=null){
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            String imageEncoded;
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for(int i = 0; i < count; i++) {
                   imageUri = data.getClipData().getItemAt(i).getUri();

                imagesEncodedList.add(imageUri);
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        } else if(data.getData() != null) {
            String imagePath = data.getData().getPath();
            //do something with the image (save it to some directory or whatever you need to do with it here)
        }
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private TimePickerDialog.OnTimeSetListener myTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    showTime(i,i1);
                }
            };
    private void showDate(int year, int month, int day) {
        dateview.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime(int hours, int minutes) {
        timeview.setText(new StringBuilder().append(hours).append(":")
                .append(minutes));
    }
    public void createEvent(View view){

        boolean isValidated = true;
        // TODO: 27/07/2022 Uncomment Below line to add image upload feature

        Log.d("Add Event Validation", "createEvent: "+eventName.getText().toString().length());
        if ((int)eventName.getText().toString().length() == 0){
            eventName.setError("Please input event name !!");
            eventName.requestFocus();
            isValidated = false;
        }
        if ((int)eventDescription.getText().toString().length() == 0){
            eventDescription.setError("Please input event description !!");
            eventDescription.requestFocus();
            isValidated = false;
        }
        if ((int)eventFoodType.getText().toString().length() == 0){
            eventFoodType.setError("Please input event food type !!");
            eventFoodType.requestFocus();
            isValidated = false;
        }
        if ((int)eventTotalParticipants.getText().toString().length() == 0){
            eventTotalParticipants.setError("Please input event participants limit !!");
            eventTotalParticipants.requestFocus();
            isValidated = false;
        }
        if (imagesEncodedList.size() < 1){
            Snackbar snackbar = Snackbar
                    .make(view, "Please select some images for the event !!", Snackbar.LENGTH_LONG);
            snackbar.show();
            isValidated = false;
        }
        if(isValidated){
//        StorageReference riversRef = storageRef.child("images/"+imageUri.getLastPathSegment());
//        UploadTask uploadTask = riversRef.putFile(imageUri);

            for(int i=0;i<imagesEncodedList.size();i++) {
                StorageReference riversRef = storageRef.child("images/" + imagesEncodedList.get(i).getLastPathSegment());
//            imagesUploaded.add(riversRef.getRoot().toString() + riversRef.getPath());
//            Log.d("image List", imagesUploaded.toString());
                UploadTask uploadTask = riversRef.putFile(imageUri);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.d("failure to upload", "failed " + exception.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        Log.d("success to upload", "success");
                        // ...
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                imagesUploaded.add(task.getResult().toString());

                                // TODO: 04/08/2022 Please update the image urls below

                                // TODO: 04/08/2022 Append value inside
                                Event event =
                                        new Event(UUID.randomUUID().toString(),eventName.getText().toString(),eventHost.getText().toString(), eventDescription.getText().toString(),
                                                dateview.getText().toString(),timeview.getText().toString(), String.valueOf(eventLocation.latitude), String.valueOf(eventLocation.longitude), null, eventFoodType.getText().toString(), eventTotalParticipants.getText().toString(), imagesUploaded, null);

                                EventUseCaseInterface eventUseCase = new EventUseCase();
                                eventUseCase.addEventToFirebase(event);

                                //ViewEventActivity -> to check view event
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }
                        });

                    }
                });

            }
        } else {
//            Snackbar snackbar = Snackbar
//                    .make(view, "Cannot process your event at the moment please try later !!", Snackbar.LENGTH_LONG);
//            snackbar.show();
        }


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        nMap = googleMap;
        nMap.clear();

        UserUseCaseInterface userUseCase = new UserUserUseCase();
        userUseCase.getUserFromFirebase(firebaseUser.getEmail()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                eventHost.setText(task.getResult().getDocuments().get(0).get("userFullName").toString());
                eventHost.setEnabled(false);
                eventLocation = new LatLng(Double.valueOf(task.getResult().getDocuments().get(0).get("userLat").toString()), Double.valueOf(task.getResult().getDocuments().get(0).get("userLong").toString()));

                nMap.addMarker(new MarkerOptions()
                        .position(eventLocation)
                        .title("Event Location"));

                nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 11));


            }
        });

    }
}