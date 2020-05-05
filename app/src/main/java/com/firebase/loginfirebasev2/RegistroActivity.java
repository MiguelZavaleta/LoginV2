package com.firebase.loginfirebasev2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.loginfirebasev2.RecursosJava.Registro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    EditText Nom, Apellidos, Curp, Telefono, Correro, Usuario, Contra;//Arreglo de edit text, el mismo numero del los que existen en el xml
    Registro Alumno = new Registro();//Instanciamos la Clase Registro
    ListView Lista;//Referencia a nuestro listview
    List<Registro> ListaAlumnos = new ArrayList<>();//Lista del Tipo Registro
    ArrayAdapter<Registro> AdapterAAlumno;//Adapter generico para alimentar nuestro listview
    Registro Seleccionado;//variable del tipo Registro
    final String NombreTabla = "Alta";/*Asignamos un nombre ala Tabla de destino de
                 Firebase que va crear dentro de nuestra BD
                 Primer child=Nombre de la tabla
                 SegundoChild=Identificador de nuestro registro dentro de la tabla*/
    int index = -1;
    //Variables para firebase
    private FirebaseDatabase Database;
    private DatabaseReference DataB_Reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        this.setTitle("Crear una Cuenta");
        IniciarVariablesDB(this);//Inicializamos las variables para firebase
        //Inicializamos nuestro arreglo con cada uno de los componentes existentes del xml
        Nom = findViewById(R.id.txtNomb);
        Apellidos = findViewById(R.id.txtAp);
        Curp = findViewById(R.id.txtCurp);
        Telefono = findViewById(R.id.txtTel);
        Correro = findViewById(R.id.txtCorreo);
        Usuario = findViewById(R.id.txtUsuario);
        Contra = findViewById(R.id.txtContra);

        Lista = findViewById(R.id.idMylista);//Hacemos referencia a nuestro listview

        Lista.setChoiceMode(Lista.CHOICE_MODE_SINGLE);
        ListarDatos();
        //Asignamos el evento onclick a nuestra lista de datos
        Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Seleccionado = (Registro) parent.getItemAtPosition(position);
                index = position;
                System.out.println(index + "-------------");
                AlumnoModificar();
            }
        });
    }

    private void AlumnoModificar() {
        //Asignamos el Texto del Alumno Seleccionado
        Nom.setText(Seleccionado.getNombre());
        Apellidos.setText(Seleccionado.getApellidos());
        Curp.setText(Seleccionado.getCurp());
        Telefono.setText(Seleccionado.getTelefono());
        Correro.setText(Seleccionado.getCorreoelectronico());
        Usuario.setText(Seleccionado.getNombredeusuario());
        Contra.setText(Seleccionado.getContraseña());

    }

    private void ListarDatos() {
        /*Consultamos los datos de nuestra base de datos, especificamente en la tabla registro*/
        DataB_Reference.child(NombreTabla).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListaAlumnos.clear();
                for (DataSnapshot ObjSnapshot : dataSnapshot.getChildren()) {
                    Registro Al = ObjSnapshot.getValue(Registro.class);
                    ListaAlumnos.add(Al);
                }
                AdapterAAlumno = new ArrayAdapter<Registro>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, ListaAlumnos);

                Lista.setAdapter(AdapterAAlumno);//Cargamos nuestra lista con nuestro Adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Limpiar() {
        Nom.setText("");
        Apellidos.setText("");
        Curp.setText("");
        Telefono.setText("");
        Correro.setText("");
        Usuario.setText("");
        Contra.setText("");
    }

    public void CargarObjeto_Registro() {//Asignamos el Contenido de nuestras Variables al Objeto
        Alumno.setNombre(Nom.getText().toString());
        Alumno.setApellidos(Apellidos.getText().toString());
        Alumno.setCurp(Curp.getText().toString());
        Alumno.setTelefono(Telefono.getText().toString());
        Alumno.setCorreoelectronico(Correro.getText().toString());
        Alumno.setNombredeusuario(Usuario.getText().toString());
        Alumno.setContraseña(Contra.getText().toString());
    }

    public void Onclick(View view) {

        switch (view.getId()) {
            case R.id.btnRegistrar:

                    if(ValidarCampos()){
                        Alumno.setId(UUID.randomUUID().toString());//Generamos un Id Aleatorio
                        CargarObjeto_Registro();
                        System.out.println(Alumno.toString());
                        if(ValidaEmail(Correro.getText().toString()) && ValidaTelefono(Telefono.getText().toString())){
                            DataB_Reference.child(NombreTabla).child(Alumno.getId()).setValue(Alumno).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistroActivity.this, "Usuario Creado Correctamente", Toast.LENGTH_SHORT).show();
                                        Limpiar();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegistroActivity.this, "Correo o Telefono Incorrectos", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(RegistroActivity.this, "Campos Vacios verifica", Toast.LENGTH_SHORT).show();
                    }
                break;
            case R.id.btnModificar:
                if (index != -1 && Seleccionado != null) {

                    Registro AlumnoModificar = new Registro();
                    AlumnoModificar.setNombre(Nom.getText().toString());
                    AlumnoModificar.setApellidos(Apellidos.getText().toString());
                    AlumnoModificar.setCurp(Curp.getText().toString());
                    AlumnoModificar.setTelefono(Telefono.getText().toString());
                    AlumnoModificar.setCorreoelectronico(Correro.getText().toString());
                    AlumnoModificar.setNombredeusuario(Usuario.getText().toString());
                    AlumnoModificar.setContraseña(Contra.getText().toString());
                    DataB_Reference.child(NombreTabla).child(Seleccionado.getId()).setValue(AlumnoModificar).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistroActivity.this, "Cambio Guardado Exitosamente", Toast.LENGTH_SHORT).show();
                                Seleccionado = null;
                                Limpiar();

                            }
                        }
                    });


                } else {
                    Toast.makeText(RegistroActivity.this, "Selecciona Algun Item", Toast.LENGTH_SHORT).show();
                    Limpiar();
                }
                break;
            case R.id.btnEliminar:
                if (index != -1 && Seleccionado != null) {
                    DataB_Reference.child(NombreTabla).child(Seleccionado.getId()).removeValue();
                    Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                    Limpiar();
                } else {
                    Toast.makeText(getApplicationContext(), "No Seleccionaste nada", Toast.LENGTH_SHORT).show();
                    Limpiar();
                }
                break;

        }
    }

    public boolean  ValidarCampos() {
        if (!ValidarVacio(Nom.getText().toString())&
        !ValidarVacio(Apellidos.getText().toString())&
        !ValidarVacio(Curp.getText().toString())&
        !ValidarVacio(Telefono.getText().toString())&
        !ValidarVacio(Correro.getText().toString())&
        !ValidarVacio(Usuario.getText().toString())&
        !ValidarVacio(Contra.getText().toString()))
            return true;//validacion correcta
        else
            return false;//no Pasaste la validacion

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
    public boolean ValidaTelefono(String tel){
        //devuelve verdadero si existe la cadena
        return(tel.matches("(\\+?[0-9]{2,3}\\-)?([0-9]{10})"))?true:false;
    }
    public void IniciarVariablesDB(Activity act) {
        FirebaseApp.initializeApp(act);//persistencia
        Database = FirebaseDatabase.getInstance();
        DataB_Reference = Database.getReference();
    }
}
