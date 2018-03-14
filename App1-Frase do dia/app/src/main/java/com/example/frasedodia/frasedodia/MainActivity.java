package com.example.frasedodia.frasedodia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textViewNovaFrase;
    private Button buttonNovaFrase;
    private int numAnterior = 0;
    private int num = 0;
    private Random random = new Random();
    private String[] frases = {"A vida é melhor para aqueles que fazem o possível para ter o melhor – John Wooden",
            "Se você não está disposto a arriscar, esteja disposto a uma vida comumm – Jim Rohn",
            "Ter sucesso é falhar repetidamente, mas sem perder o entusiasmo – Winston Churchill"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewNovaFrase = (TextView) findViewById(R.id.textoNovaFrase);
        buttonNovaFrase = (Button) findViewById(R.id.botaoNovaFrase);
        buttonNovaFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geraRandomR(random);
                numAnterior = num;
                textViewNovaFrase.setText(frases[num]);
                /*geraRandom(random);
                while (num == numAnterior) {
                    geraRandom(random);
                }
                textViewNovaFrase.setText(frases[num]);
                numAnterior = num;
                */

            }
        });

    }

    /*private void geraRandom(int random) {
        num = random.nextInt(frases.length);
    }
    */
    private void geraRandomR(Random random) {

        if (num == numAnterior) {
            num = random.nextInt(frases.length);
            geraRandomR(random);
        }

    }
}
