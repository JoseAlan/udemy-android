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

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoDescricao, campoCategoria, campoData;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference dataref = ConfigFirebase.getReferenciaDataBase();
    private FirebaseAuth autentic = ConfigFirebase.getAuth();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.despesa_data);
        campoCategoria = findViewById(R.id.despesa_categoria);
        campoDescricao = findViewById(R.id.despesa_descricao);
        campoValor = findViewById(R.id.despesa_valor);


        campoData.setText(DateUtil.dataAtual());
        recuperarDespesaTotal();
    }


    public void salvarDespesa(View view) {

        if (validarCamposDespesa()) {
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao = new Movimentacao();
            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setTipo("d");
            movimentacao.setData(data);

            Double despesaAtualizada = despesaTotal + valorRecuperado;
            atualizarDespesa(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCamposDespesa() {

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
                        Toast.makeText(DespesasActivity.this,
                                "Campo Descricao nao foi preenchido!!",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else {
                    Toast.makeText(DespesasActivity.this,
                            "Campo Categoria nao foi preenchido!!",
                            Toast.LENGTH_LONG).show();
                    return false;
                }

            } else {
                Toast.makeText(DespesasActivity.this,
                        "Campo Data nao foi preenchido!!",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(DespesasActivity.this,
                    "Campo valor nao foi preenchido!!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void recuperarDespesaTotal() {
        String emailUsuario = autentic.getCurrentUser().getEmail();
        String idUsuario = Base64Util.codeficar(emailUsuario);
        DatabaseReference usuarioRef = dataref.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void atualizarDespesa(Double despesaAtualizada) {

        String emailUsuario = autentic.getCurrentUser().getEmail();
        String idUsuario = Base64Util.codeficar(emailUsuario);
        DatabaseReference usuarioRef = dataref.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesaAtualizada);
    }

}



