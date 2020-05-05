package com.firebase.loginfirebasev2.RecursosJava;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseApp extends  android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
       //utilidades.mAuth = FirebaseAuth.getInstance();
    }
}
