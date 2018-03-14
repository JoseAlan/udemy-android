package com.example.idadedecachorro.idadedecachorro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView resultIdade;
    private EditText caixaTexto;
    private Button botaoIdade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultIdade = findViewById(R.id.resultIdadeId);
        caixaTexto = findViewById(R.id.caixaTextoId);
        botaoIdade = findViewById(R.id.botaoIdadeId);

        botaoIdade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idadeDigitada = caixaTexto.getText().toString();

                if (idadeDigitada.isEmpty()) {
                    resultIdade.setText("nenhum numero digitado");

                } else {
                    int valorIdade = Integer.parseInt(idadeDigitada);
                    if (valorIdade>=15) {
                        resultIdade.setText("A idade do cachorro MUMIA em anos humanos e: " + valorIdade * 7 + " anos");
                    } else if (valorIdade == 0) {
                        resultIdade.setText("zero nao vale :(");
                    } else{
                        int resultado = valorIdade * 7;

                    resultIdade.setText("A idade do cachorro em anos humanos e: " + resultado + " anos");
                }
            }
        }
    });

}


}
