package com.firebase.loginfirebasev2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.loginfirebasev2.RecursosJava.Registro;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText txtCorreo, txtpas;//creamos las variables del tipo edittex
    //Variables para firebase
    private FirebaseDatabase Database;
    private DatabaseReference DataB_Reference;
    List<Registro> ListaAlumnos;
    final String NombreTabla = "Alta";/*Asignamos un nombre ala Tabla de destino de
                 Firebase que va crear dentro de nuestra BD
                 Primer child=Nombre de la tabla
                 SegundoChild=Identificador de nuestro registro dentro de la tabla*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //inicializamos las variables con las que estan en nuestro xml
        txtCorreo = findViewById(R.id.TxtCorreo);
        txtpas = findViewById(R.id.Txtpass);
        //iNICIALIZAMOS VARIABLES fIREBASE
        IniciarVariablesDB(this);
        //Cargamos Usuarios
        CargarUsuarios();
    }

    public void Onclick(View view) {
        switch (view.getId()){
            case R.id.btnIniciar:
                //accion a realizar
                boolean ExisteCorreo=false,ExistePass=false;
                if(!ValidarVacio(txtCorreo.getText().toString() )&& !ValidarVacio(txtpas.getText().toString())){
                    if(ValidaEmail(txtCorreo.getText().toString())){
                        int i=0;
                        //Recorremos nuestro Arreglo con los datos
                        while(i<ListaAlumnos.size()){
                            if(ListaAlumnos.get(i).getCorreoelectronico().equals(txtCorreo.getText().toString())
                                    && ListaAlumnos.get(i).getContraseña().equals(txtpas.getText().toString())){
                                ExisteCorreo=true;
                                break;
                            }else{
                                ExisteCorreo=false;
                            }
                            i++;
                        }
                        if(ExisteCorreo){
                            startActivity(new Intent(this,PortalActivity.class));
                            finish();
                        }else{
                            Toast.makeText(this, "Verifica los datos", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, "Correo o Pass invalidas", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Campos Vacios Verifica", Toast.LENGTH_SHORT).show();
                }
            break;
            case  R.id.btnCrear:
                //accion a realizar
                startActivity(new Intent(getApplicationContext(),RegistroActivity.class));
                break;

        }
    }
    public  void CargarUsuarios() {
        ListaAlumnos = new ArrayList<>();
        DataB_Reference.child(NombreTabla).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ListaAlumnos.clear();
                    for (DataSnapshot CtrlSnap : dataSnapshot.getChildren()) {
                        Registro Alumnos = new Registro();
                        Alumnos.setId(CtrlSnap.child("id").getValue().toString());
                        Alumnos.setNombre(CtrlSnap.child("nombre").getValue().toString());
                        Alumnos.setApellidos(CtrlSnap.child("apellidos").getValue().toString());
                        Alumnos.setCurp(CtrlSnap.child("curp").getValue().toString());
                        Alumnos.setTelefono(CtrlSnap.child("telefono").getValue().toString());
                        Alumnos.setCorreoelectronico(CtrlSnap.child("correoelectronico").getValue().toString());
                        Alumnos.setNombredeusuario(CtrlSnap.child("nombredeusuario").getValue().toString());
                        Alumnos.setContraseña(CtrlSnap.child("contraseña").getValue().toString());
                        ListaAlumnos.add(Alumnos);

                        Log.d("Datos>>", "f");
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public boolean ValidarVacio(String x) {//deuvleve verdadero si esta vacia la cadena
        return (x.trim().isEmpty());
    }
    public static  boolean ValidaEmail(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public  void IniciarVariablesDB(Activity act) {
        FirebaseApp.initializeApp(act);//persistencia
        Database = FirebaseDatabase.getInstance();
        DataB_Reference = Database.getReference();
    }
}
