package com.example.alan.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ValueEventListener valueEventListenerMovimentacoes;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

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
        swipe();

        //configg adapetr
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //config recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);

    }

    public void swipe() {
        final ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragflags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragflags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.i("swipe", "item arastado com sucesso");
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Movimentacao de conta");
        alertDialog.setMessage("Tem certeza ue deseja excluir essa Movimentacao?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);
                String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
                String idUsuario = Base64Util.codeficar(emailUsuario);
                movimentacaoRef = dataref.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);
                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this,
                        "Cancelado",
                        Toast.LENGTH_LONG).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atualizarSaldo() {

        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Util.codeficar(emailUsuario);
        usuarioRef = dataref.child("usuarios").child(idUsuario);

        if (movimentacao.getTipo().equals("r")) {
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }

        if (movimentacao.getTipo().equals("d")) {
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }


    }


    public void recuperarMovimentacoes() {
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Util.codeficar(emailUsuario);
        movimentacaoRef = dataref.child("movimentacao").child(idUsuario).child(mesAnoSelecionado);
        //Log.i("Dados","retorno: "+ mesAnoSelecionado);
        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movimentacoes.clear();
                for (DataSnapshot dates : dataSnapshot.getChildren()) {

                    Movimentacao movimentacao = dates.getValue(Movimentacao.class);
                    movimentacao.setKey(dates.getKey());
                    movimentacoes.add(movimentacao);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
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

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1));
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                String mesSelecionado = String.format("%02d", (calendarDay.getMonth() + 1));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + calendarDay.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
                // Log.i("dados", "dados" + mesAnoSelecionado);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperaResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }
}
