package com.example.feedtheneed.presentation.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.feedtheneed.HomeActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.data.repository.AuthRepoImplementation;
import com.example.feedtheneed.domain.repository.AuthRepository;
import com.example.feedtheneed.domain.usecase.user.UserUseCaseInterface;
import com.example.feedtheneed.domain.usecase.user.UserUserUseCase;
import com.example.feedtheneed.presentation.authentication.AuthActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileActivity extends AppCompatActivity {

    // Initialize variable
    CircularImageView ivImage;
    TextView tvName,mail,user_name, isRestaurant;
    ImageView img;
    Button btLogout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Assign variable
        ivImage=findViewById(R.id.iv_image);
        tvName=findViewById(R.id.tv_name);
        user_name=findViewById(R.id.user_name);
        img=findViewById(R.id.img);
        mail=findViewById(R.id.mail);
        btLogout=findViewById(R.id.bt_logout);
        isRestaurant=findViewById(R.id.user_type);


        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        // Check condition
        if(firebaseUser!=null)
        {
            // When firebase user is not equal to null
            // Set image on image view
            Glide.with(ProfileActivity.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(ivImage);
            // set name on text view
            if(firebaseUser.getPhoneNumber().equals(""))
            {
                tvName.setText(firebaseUser.getDisplayName());
                img.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else {tvName.setText(firebaseUser.getPhoneNumber()); img.setImageResource(R.drawable.ic_baseline_phone_24);}
//            user_name.setText(firebaseUser.getDisplayName());
            mail.setText(firebaseUser.getEmail());

            Log.d("PhotoURL", "PhotoUrl: "+firebaseUser.getPhotoUrl());
        }

        UserUseCaseInterface useCase = new UserUserUseCase();
        useCase.getUserFromFirebase(firebaseUser.getEmail()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                DocumentSnapshot userInformation = task.getResult().getDocuments().get(0);
                // TODO: 05/08/2022 Here you have details of the user please show these in profile activity
                user_name.setText(userInformation.get("userFullName").toString());
                if (userInformation.get("restaurant").toString().equalsIgnoreCase("true")){
                    isRestaurant.setText("Restaurant Representative");
                }else{
                    isRestaurant.setText("Normal User");
                }
            }
        });


        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthRepository authRepository = new AuthRepoImplementation();
                authRepository.signOut(ProfileActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
            }
        });
    }
}
