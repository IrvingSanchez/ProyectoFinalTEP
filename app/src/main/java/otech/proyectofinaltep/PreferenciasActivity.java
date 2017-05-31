package otech.proyectofinaltep;

import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PreferenciasActivity extends AppCompatActivity {

    Toolbar tb;
    EditText url,email,telefono;
    RadioGroup region;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        url = (EditText)findViewById(R.id.webService);
        email = (EditText) findViewById(R.id.emailUsuario);
        telefono = (EditText) findViewById(R.id.telefonoUsuario);
        region = (RadioGroup) findViewById(R.id.continentesGroup);

        tb = (Toolbar) findViewById(R.id.tbPreferencias);
        setSupportActionBar(tb);

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cargarPreferencias();

    }


    public void onclickGuardar(View v)
    {
        String sUrl = url.getText().toString();
        String sEmail = email.getText().toString();
        String sTelefono = telefono.getText().toString();
        int idRegion = region.getCheckedRadioButtonId();
        String sRegion = "";
        switch (idRegion)
        {
            case R.id.africa:
                sRegion = "Africa";
                break;

            case R.id.america:
                sRegion = "Americas";
                break;

            case R.id.asia:
                sRegion = "Asia";
                break;

            case R.id.europa:
                sRegion = "Europe";
                break;

            case R.id.oceania:
                sRegion = "Oceania";
                break;
        }

        SharedPreferences preferencias = getSharedPreferences("Preferencias",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();

        editor.putString("url",sUrl);
        editor.putString("region",sRegion);
        editor.putString("email",sEmail);
        editor.putString("telefono",sTelefono);

        editor.commit();

        Toast.makeText(this,"Preferencias Guardadas",Toast.LENGTH_SHORT).show();

        onBackPressed();

    }

    public void cargarPreferencias()
    {
        SharedPreferences preferencias = getSharedPreferences("Preferencias",MODE_PRIVATE);

        url.setText(preferencias.getString("url","https://restcountries.eu/rest/v2/region/"));
        email.setText(preferencias.getString("email",""));
        telefono.setText(preferencias.getString("telefono",""));
        String sRegion = preferencias.getString("region","Americas");
        switch (sRegion)
        {
            case "Africa":
                region.check(R.id.africa);
                break;

            case "Americas":
                region.check(R.id.america);
                break;

            case "Asia":
                region.check(R.id.asia);
                break;

            case "Europe":
                region.check(R.id.europa);
                break;

            case "Oceania":
                region.check(R.id.oceania);
                break;
        }
    }

}
