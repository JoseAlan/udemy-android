package com.example.alan.organizze.helper;

import android.util.Base64;

import java.util.UUID;

public class Base64Util {

    public static String codeficar(String texto){

        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodifiar(String textoodificado){
        return new String (Base64.decode(textoodificado, Base64.DEFAULT));

    }

}
