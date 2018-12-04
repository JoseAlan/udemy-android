package com.example.alan.organizze.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alan.organizze.R;
import com.example.alan.organizze.config.ConfigFirebase;
import com.example.alan.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button btLogin;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoSenha = findViewById(R.id.login_editText_password);
        campoEmail = findViewById(R.id.login_editText_email);
        btLogin = findViewById(R.id.activity_login_button);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoSenha = campoSenha.getText().toString();
                String textoEmail = campoEmail.getText().toString();

                if ((!textoEmail.isEmpty()) || (!textoSenha.isEmpty())) {
                    usuario = new Usuario();
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    loginUsuario();

                } else {

                    Toast.makeText(LoginActivity.this,
                            "Preenche o(s) campo(s) em branco!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginUsuario() {
        autenticacao = ConfigFirebase.getAuth();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    abriTelaPrincipal();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Email nao cadastrado ou desabilitado!!!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email e senha nao correspondem!!!";
                    } catch (Exception e) {
                        excecao = "Erro ao Logar usuario " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abriTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}
