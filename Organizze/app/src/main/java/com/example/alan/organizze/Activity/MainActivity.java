package com.example.alan.organizze.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.alan.organizze.Activity.CadastroActivity;
import com.example.alan.organizze.Activity.LoginActivity;
import com.example.alan.organizze.R;
import com.example.alan.organizze.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuariioLogado();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());


    }

    public void btEntrar(View view) {
        // Toast.makeText(this, "entrou para a globo", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginActivity.class));

    }

    public void btCadastrar(View view) {
        //Toast.makeText(this, "Cadastrou infeliz", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, CadastroActivity.class));

    }

    public void verificarUsuariioLogado() {
        auth = ConfigFirebase.getAuth();
        //auth.signOut();
        if (auth.getCurrentUser() != null) {
            abriTelaPrincipal();
        }
    }

    private void abriTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
     }

}
