package com.example.feedtheneed.domain.repository;

import android.content.Context;

import com.google.android.gms.tasks.Task;

public interface AuthRepository {

    void signInWithGoogle();

    Task<Void> signOut(Context context);
}
