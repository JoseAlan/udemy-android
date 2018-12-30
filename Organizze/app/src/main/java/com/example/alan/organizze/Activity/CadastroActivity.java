package com.example.alan.organizze.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alan.organizze.R;
import com.example.alan.organizze.config.ConfigFirebase;
import com.example.alan.organizze.helper.Base64Util;
import com.example.alan.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button btCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.cadastro_editText_name);
        campoSenha = findViewById(R.id.cadastro_editText_password);
        campoEmail = findViewById(R.id.cadastro_editText_email);
        btCadastrar = findViewById(R.id.button_cadastro);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNome.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                String textoEmail = campoEmail.getText().toString();

                if ((!textoNome.isEmpty()) || (!textoEmail.isEmpty()) || (!textoSenha.isEmpty())) {
                    usuario = new Usuario();
                    usuario.setEmail(textoEmail);
                    usuario.setNome(textoNome);
                    usuario.setSenha(textoSenha);
                    cadastartUsuario();

                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Preenche o(s) campo(s) em branco!!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    public void cadastartUsuario() {

        autenticacao = ConfigFirebase.getAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String idUsuario = Base64Util.codeficar(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    finish();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email valido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Usuario ja cadastrado";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuario" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
