package otech.proyectofinaltep;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    //  Referencias a la UI
    EditText usr, pass;

    SharedPreferences preferencias;
    SharedPreferences.Editor editorP;
    String usuario;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        usr = (EditText)findViewById(R.id.rUsuario);
        pass = (EditText)findViewById(R.id.rPass);


        preferencias = getSharedPreferences("Credenciales",MODE_PRIVATE);
        editorP = preferencias.edit();
    }

    /**
     * Metodo que se ejecuta al presionar
     * boton Registrar
     */
    public void onClickRegistro(View v)
    {
        usuario = usr.getText().toString();
        password = pass.getText().toString();

        if (!hayCamposVacios())
        {
            editorP.putString("usuario",usuario);
            editorP.putString("pass",password);

            editorP.commit();

            Toast.makeText(this,"Registro Exitoso",Toast.LENGTH_SHORT).show();
            this.finish();
        }

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
}
