package com.example.alan.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference referencia;

    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static DatabaseReference getReferenciaDataBase(){
        if(referencia == null){
            referencia = FirebaseDatabase.getInstance().getReference();
        }
        return referencia;
    }

   /* public static DatabaseReference getUserRef(){
        if(referencia == null && auth ==null){
            auth = FirebaseAuth.getInstance();
            referencia = FirebaseDatabase.getInstance().getReference();
        }
    }
    */
}
