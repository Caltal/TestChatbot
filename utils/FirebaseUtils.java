package com.example.asg02.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {
    private static DatabaseReference databaseReference;
    private static FirebaseAuth auth;

    public static DatabaseReference getDatabaseReference(String path) {
        if (databaseReference != null) {
            return databaseReference;
        }
        return FirebaseDatabase.getInstance().getReference(path);
    }

    public static void setDatabaseReference(DatabaseReference reference) {
        databaseReference = reference;
    }

    public static FirebaseAuth getAuth() {
        if (auth != null) {
            return auth;
        }
        return FirebaseAuth.getInstance();
    }

    public static void setAuth(FirebaseAuth firebaseAuth) {
        auth = firebaseAuth;
    }

    public static void setMockInstance(FirebaseUtils mockInstance) {
        databaseReference = mockInstance.databaseReference;
        auth = mockInstance.auth;
    }

    // other methods...
}
