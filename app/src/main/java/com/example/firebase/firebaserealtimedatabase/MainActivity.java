package com.example.firebase.firebaserealtimedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtNombre;
    private Button btnSave;
    private TextView lblNombre;

    private FirebaseDatabase database;

    private ArrayList<Persona> personas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = findViewById(R.id.txtNombre);
        btnSave = findViewById(R.id.btnSave);
        lblNombre = findViewById(R.id.lblNombre);

        personas = new ArrayList<>();
        // creaPersonas();

        database = FirebaseDatabase.getInstance("https://fir-example-7dc0f-default-rtdb.europe-west1.firebasedatabase.app/");

        // REFERENCIAS
        DatabaseReference refNombre = database.getReference("nombre");
        DatabaseReference refPersona = database.getReference("persona");
        DatabaseReference refPersonas = database.getReference("personas_list");

        // INSERCIONES
        Persona p = new Persona("Eduardo", 44);
        refPersona.setValue(p);

        // refPersonas.setValue(personas);

        // LECTURAS
        refPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Persona p = snapshot.getValue(Persona.class);
                    Toast.makeText(MainActivity.this, p.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator< ArrayList<Persona> >() {};
                personas = snapshot.getValue(gti);
                Toast.makeText(MainActivity.this, "Elementos descargados: "+personas.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = txtNombre.getText().toString();
                refNombre.setValue(texto);
            }
        });

        refNombre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombre = snapshot.getValue(String.class);
                lblNombre.setText(nombre);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void creaPersonas() {
        for (int i = 0; i < 1000; i++) {
            personas.add(new Persona("Personas "+i, i+10));
        }
    }
}