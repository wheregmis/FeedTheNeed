package com.example.feedtheneed.domain.repository;

import com.example.feedtheneed.domain.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public interface UserRepository {

    Task<DocumentReference> insertUser(User user);
    Task<QuerySnapshot> getUser(String userEmail);
    void getUsers();

}
