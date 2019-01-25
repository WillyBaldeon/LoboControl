package sac.lobosistemas.loboventas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText usuario;
    SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = findViewById(R.id.txtUsuario);
        preferencias = getSharedPreferences("Datos", Context.MODE_PRIVATE);

    }

    public void ingresar(View v){
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("Usuario", ""+usuario);
        editor.commit();

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
