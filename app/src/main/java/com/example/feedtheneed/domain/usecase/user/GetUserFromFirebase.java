package com.example.feedtheneed.domain.usecase.user;

import com.example.feedtheneed.data.repository.UserRepositoryImplementation;
import com.example.feedtheneed.domain.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class GetUserFromFirebase {
    UserRepository userRepository;
    String userEmail;
    GetUserFromFirebase(String userEmail){
        userRepository = new UserRepositoryImplementation();
        this.userEmail = userEmail;
    }

    Task<QuerySnapshot> getUserFromFirebaseImplementation(){
        return userRepository.getUser(userEmail);
    }
}
