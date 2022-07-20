package com.example.feedtheneed.domain.usecase.user;

import com.example.feedtheneed.domain.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public class UserUserUseCase implements UserUseCaseInterface {


    @Override
    public Task<DocumentReference> addUserToFirebase(User user) {
        return new AddUserToFirebase(user).addUserToFirebaseImplementation();
    }

    @Override
    public Task<QuerySnapshot> getUserFromFirebase(String userEmail) {
        return new GetUserFromFirebase(userEmail).getUserFromFirebaseImplementation();
    }
}
