package com.example.feedtheneed.domain.usecase.user;

import com.example.feedtheneed.domain.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public interface UserUseCaseInterface {

    Task<DocumentReference> addUserToFirebase(User user);

    Task<QuerySnapshot> getUserFromFirebase(String userEmail);



}
