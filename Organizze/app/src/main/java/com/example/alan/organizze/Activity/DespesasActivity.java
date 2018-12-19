package com.example.alan.organizze.Activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.alan.organizze.R;
import com.example.alan.organizze.helper.DateUtil;
import com.example.alan.organizze.model.Movimentacao;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoDescricao, campoCategoria, campoData;
    private EditText campoValor;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoData = findViewById(R.id.despesa_data);
        campoCategoria = findViewById(R.id.despesa_categoria);
        campoDescricao = findViewById(R.id.despesa_descricao);
        campoValor = findViewById(R.id.despesa_valor);


        campoData.setText(DateUtil.dataAtual());



    }

    public void salvarDespesa(View view){
        String data = campoData.getText().toString();
        movimentacao = new Movimentacao();
        movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()));
        movimentacao.setCategoria(campoCategoria.getText().toString());
        movimentacao.setDescricao(campoDescricao.getText().toString());
        movimentacao.setTipo("d");
        movimentacao.setData(data);

        movimentacao.salvar(data);

    }

}
