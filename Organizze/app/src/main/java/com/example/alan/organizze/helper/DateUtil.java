package com.example.alan.organizze.helper;

import java.text.SimpleDateFormat;

public class DateUtil {
    public static String dataAtual (){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String mesAnoEscolhido(String data){
        String dataRetorno [] = data.split("/");
        String dia = dataRetorno[0];
        String mes = dataRetorno[1];
        String ano = dataRetorno[2];

        String mesAno = mes + ano;
        return mesAno;
    }
}
