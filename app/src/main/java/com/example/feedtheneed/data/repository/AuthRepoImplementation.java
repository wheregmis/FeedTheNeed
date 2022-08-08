package com.example.feedtheneed.data.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.feedtheneed.domain.repository.AuthRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepoImplementation implements AuthRepository {

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;


    @Override
    public void signInWithGoogle() {

    }

    @Override
    public Task<Void> signOut(Context context) {

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(context
                , GoogleSignInOptions.DEFAULT_SIGN_IN);

        return googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Check condition
                if(task.isSuccessful())
                {
                    // When task is successful
                    // Sign out from firebase
                    firebaseAuth.signOut();

                    // Display Toast
                    Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show();

//                    // Finish activity
                }
            }
        });

    }
}
