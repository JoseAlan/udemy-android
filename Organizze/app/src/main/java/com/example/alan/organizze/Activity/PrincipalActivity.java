package com.example.alan.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.alan.organizze.R;
import com.example.alan.organizze.adapter.AdapterMovimentacao;
import com.example.alan.organizze.config.ConfigFirebase;
import com.example.alan.organizze.helper.Base64Util;
import com.example.alan.organizze.model.Movimentacao;
import com.example.alan.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView saldo, saldacao;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth = ConfigFirebase.getAuth();
    private DatabaseReference dataref = ConfigFirebase.getReferenciaDataBase();
    private Double receitaTotal = 0.0, despesaTotal = 0.0, resumoSaldo = 0.0;
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListener;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.principal_calendarView);
        saldo = findViewById(R.id.principal_saldo);
        saldacao = findViewById(R.id.principal_saudacao);
        recyclerView = findViewById(R.id.principal_recyclerView);
        configCalenadario();

        //configg adapetr
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //config recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setAdapter();



    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaResumo();
    }

    public void recuperaResumo() {

        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Util.codeficar(emailUsuario);
        usuarioRef = dataref.child("usuarios").child(idUsuario);

        valueEventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
                despesaTotal = usuario.getDespesaTotal();
                resumoSaldo = receitaTotal - despesaTotal;

                saldacao.setText("Ola, " + usuario.getNome());
                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoSaldo);
                saldo.setText("R$ " + resultadoFormatado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sair:
                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void adicionaReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void adicionaDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));

    }

    public void configCalenadario() {
        CharSequence meses[] = {"Jan", "Fev", "Mar", "Abr", "Maio", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        calendarView.setTitleMonths(meses);

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
    }
}
