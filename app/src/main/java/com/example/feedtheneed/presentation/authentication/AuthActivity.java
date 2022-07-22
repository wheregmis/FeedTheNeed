package com.example.feedtheneed.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedtheneed.HomeActivity;
import com.example.feedtheneed.MapsActivity;
import com.example.feedtheneed.R;
import com.example.feedtheneed.data.repository.UserRepositoryImplementation;
import com.example.feedtheneed.domain.model.User;
import com.example.feedtheneed.domain.repository.UserRepository;
import com.example.feedtheneed.domain.usecase.user.UserUseCaseInterface;
import com.example.feedtheneed.domain.usecase.user.UserUserUseCase;
import com.example.feedtheneed.presentation.user.AdditionalInformationActivity;
import com.example.feedtheneed.presentation.user.ProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthActivity extends AppCompatActivity {

    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Assign variable
        btSignIn=findViewById(R.id.bt_sign_in);

        // Initialize sign in options
        // the client-id is copied form
        // google-services.json file
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("1031451218357-8r4b7a78ike60i726hhkhs5ah9suevkg.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(AuthActivity.this
                ,googleSignInOptions);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize sign in intent
                Intent intent=googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
            }
        });

        FirebaseApp.initializeApp(AuthActivity.this);

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        // Check condition
//        if(firebaseUser!=null)
        {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(AuthActivity.this, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            // When request code is equal to 100
            // Initialize task
            Log.d("AuthPopup", "If it responded from the auth pop up");
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            // check condition
            if(signInAccountTask.isSuccessful())
            {
                // When google sign in successful
                // Initialize string
                String s="Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    // Check condition
                    if(googleSignInAccount!=null)
                    {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if(task.isSuccessful())
                                        {
                                            // When task is successful
//                                            // Redirect to profile activity

                                            // Initialize firebase auth
                                            firebaseAuth=FirebaseAuth.getInstance();

                                            // Initialize firebase user
                                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                                            UserRepository userRepository = new UserRepositoryImplementation();

                                            UserUseCaseInterface useCaseToGetUser = new UserUserUseCase();

                                            useCaseToGetUser.getUserFromFirebase(firebaseUser.getEmail()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.getResult().size() == 0){
                                                            User user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());

                                                            UserUseCaseInterface useCase = new UserUserUseCase();
                                                            useCase.addUserToFirebase(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    // TODO: 19/07/2022 Handle On Success
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // TODO: 19/07/2022 Handle failure
                                                                }
                                                            });

                                                            Intent intent = new Intent(AuthActivity.this, AdditionalInformationActivity.class);
                                                            startActivity(intent);
                                                        }else{
                                                            startActivity(new Intent(AuthActivity.this
                                                                    , MapsActivity.class)
                                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        }
                                                    } else {
                                                        Log.w("Retrieval", "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            // When task is unsuccessful
                                            // Display Toast
                                            displayToast("Authentication Failed :"+task.getException()
                                                    .getMessage());
                                        }
                                    }
                                });

                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
