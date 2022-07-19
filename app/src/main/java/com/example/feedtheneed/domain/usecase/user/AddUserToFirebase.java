package com.example.feedtheneed.domain.usecase.user;

import com.example.feedtheneed.data.repository.UserRepositoryImplementation;
import com.example.feedtheneed.domain.model.User;
import com.example.feedtheneed.domain.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class AddUserToFirebase {
    UserRepository userRepository;
    User user;
    AddUserToFirebase(User user){
        userRepository = new UserRepositoryImplementation();
        this.user = user;
    }

    Task<DocumentReference> addUserToFirebaseImplementation(){
        return userRepository.insertUser(user);
    }
}
