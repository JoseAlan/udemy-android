package com.example.alan.organizze.Activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.alan.organizze.R;
import com.example.alan.organizze.helper.DateUtil;

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoDescricao, campoCategoria, campoData;
    private EditText campoValor;

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
}
