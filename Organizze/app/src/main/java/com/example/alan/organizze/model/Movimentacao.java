package com.example.alan.organizze.model;

import com.example.alan.organizze.config.ConfigFirebase;
import com.example.alan.organizze.helper.Base64Util;
import com.example.alan.organizze.helper.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {


    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private Double valor;
    private String key;

    public Movimentacao() {
    }

    public void salvar(String dataEscolhida){
        FirebaseAuth auth = ConfigFirebase.getAuth();
        String idUsuario = Base64Util.codeficar(auth.getCurrentUser().getEmail());
        String mesAno = DateUtil.mesAnoEscolhido(dataEscolhida);
        DatabaseReference firebase = ConfigFirebase.getReferenciaDataBase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
