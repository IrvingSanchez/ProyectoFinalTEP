package otech.proyectofinaltep;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    //  Referencias a la UI
    EditText usr, pass;
    SharedPreferences preferencias;
    String usuario;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usr = (EditText)findViewById(R.id.iUsuario);
        pass = (EditText)findViewById(R.id.iPassw);
        preferencias = getSharedPreferences("Credenciales",MODE_PRIVATE);

        //  Verificar permisos
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso!");
        }

    }

    /**
     * Metodo que se ejecuta al presionar
     * boton Ingresar
     */
    public void onClickIngresar(View v)
    {
        usuario = usr.getText().toString();
        password = pass.getText().toString();

        String spUsr = preferencias.getString("usuario"," ");
        String spPass = preferencias.getString("pass", " ");

        if (!hayCamposVacios() && sonCredencialesValidas(spUsr,spPass))
        {
            Toast.makeText(this,"Cargando agenda...",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this.getApplicationContext(),ContactosActivity.class);
            this.startActivity(i);
            this.finish();
        }
    }

    /**
     * Metodo que se ejecuta al presionar
     * boton Registrar
     */
    public void onClickRegistrar(View v)
    {
        Intent i = new Intent(this.getApplicationContext(),RegistroActivity.class);
        this.startActivity(i);
    }

    private boolean hayCamposVacios()
    {
        boolean b = true;

        if (usuario.isEmpty())
        {
            usr.requestFocus();
            usr.setError("Es campo es necesario");
        }

        else if (password.isEmpty())
        {
            pass.requestFocus();
            pass.setError("Este campo es necesario");
        }
        else
            b = false;

        return b;
    }

    private boolean sonCredencialesValidas(String spUsr, String spPass)
    {
        boolean r = false;

        if (!spUsr.equals(usuario))
        {
            usr.requestFocus();
            usr.setError("Usuario incorrecto");
        }
        else if (!spPass.equals(password))
        {
            pass.requestFocus();
            pass.setError("Contraseña incorrecta");
        }
        else
            r = true;

        return r;
    }
}


