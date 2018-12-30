package com.example.alan.organizze.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alan.organizze.R;
import com.example.alan.organizze.config.ConfigFirebase;
import com.example.alan.organizze.helper.Base64Util;
import com.example.alan.organizze.helper.DateUtil;
import com.example.alan.organizze.model.Movimentacao;
import com.example.alan.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoDescricao, campoCategoria, campoData;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference databaseReference = ConfigFirebase.getReferenciaDataBase();
    private FirebaseAuth firebaseAuth = ConfigFirebase.getAuth();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        selecionarCampos();
        recuperarReceitaTotal();
    }


    public void salvarReceita(View view) {
        if (validarReceita()) {
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());
            String data = campoData.getText().toString();
            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setData(data);
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);

            movimentacao.salvar(data);
            finish();

        }

    }

    public Boolean validarReceita() {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) {
            if (!textoData.isEmpty()) {
                if (!textoCategoria.isEmpty()) {
                    if (!textoDescricao.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitasActivity.this,
                                "Campo Descricao nao foi preenchido!!",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ReceitasActivity.this,
                            "Campo Categoria nao foi preenchido!!",
                            Toast.LENGTH_LONG).show();
                    return false;
                }

            } else {
                Toast.makeText(ReceitasActivity.this,
                        "Campo Data nao foi preenchido!!",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(ReceitasActivity.this,
                    "Campo valor nao foi preenchido!!",
                    Toast.LENGTH_LONG).show();
            return false;
        }

    }


    private void recuperarReceitaTotal() {
        DatabaseReference usuarioRef = recuperarIdUsuario();

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receitaAtualizada) {
        DatabaseReference usuarioRef = recuperarIdUsuario();
        usuarioRef.child("receitaTotal").setValue(receitaAtualizada);
    }

    private DatabaseReference recuperarIdUsuario() {
        String idEmail = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Util.codeficar(idEmail);
        return databaseReference.child("usuarios").child(idUsuario);
    }

    private void selecionarCampos() {
        campoValor = findViewById(R.id.receita_valor);
        campoCategoria = findViewById(R.id.receita_categoria);
        campoDescricao = findViewById(R.id.receita_descricao);
        campoData = findViewById(R.id.receita_data);
        campoData.setText(DateUtil.dataAtual());
    }
}
