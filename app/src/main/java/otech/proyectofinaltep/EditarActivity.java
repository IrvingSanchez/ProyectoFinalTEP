package otech.proyectofinaltep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class EditarActivity extends AppCompatActivity {

    Toolbar tb;
    EditText nombre, apellido, telefono, email;
    Spinner spinner;
    String sNombre, sApellido, sTelefono, sEmail, sCiudad;
    String urlws, contacto;
    static ArrayList<String> data = new ArrayList<String>();
    ArrayList<String> lista;

    Boolean esNuevo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        nombre = (EditText) findViewById(R.id.nombreEditar);
        apellido = (EditText) findViewById(R.id.apellidoEditar);
        telefono = (EditText) findViewById(R.id.telefonoEditar);
        email = (EditText) findViewById(R.id.emailEditar);
        spinner = (Spinner) findViewById(R.id.spinner);
        tb = (Toolbar)findViewById(R.id.toolbarEditar);
        setSupportActionBar(tb);

        //Obtener datos de wb

        SharedPreferences preferencias = getSharedPreferences("Preferencias",MODE_PRIVATE);


        String WS_REGION = preferencias.getString("region","");
        urlws = preferencias.getString("url","") + Uri.encode(WS_REGION);
        new ProcessJson().execute(urlws);


        esNuevo = getIntent().getExtras().getBoolean("nuevo",false);
        lista = (ArrayList<String>) getIntent().getExtras().get("lista");
        // Editar
        if (!esNuevo) {
            //posicion = getIntent().getExtras().getInt("posicion");
            //lista = (ArrayList<String>) getIntent().getExtras().get("lista");

            contacto = getIntent().getExtras().getString("contacto");
            String s = contacto;
            sNombre = s.substring(0, s.indexOf("\n"));
            if (sNombre.contains(",")){
                sCiudad = sNombre.substring((sNombre.indexOf(",")+1));
                sNombre = sNombre.substring(0,sNombre.indexOf(","));


            }
            s = s.substring((s.indexOf("\n") + 1), s.length());
            sTelefono = s.substring(0, s.indexOf("\n"));
            sEmail = s.substring((s.indexOf("\n") + 1), s.length());

            tb.setTitle(sNombre);
            nombre.setText(sNombre);
            telefono.setText(sTelefono);
            email.setText(sEmail);


        }
        else // es nuevo
        {
            lista = (ArrayList<String>) getIntent().getExtras().get("lista");
        }


        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditarActivity.this,"Cargando Agenda...",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditarActivity.this, ContactosActivity.class);
                startActivity(i);
                EditarActivity.this.finish();
            }
        });



    }

    public void onClickGuardar(View v)
    {
        sNombre = nombre.getText().toString();
        sApellido = apellido.getText().toString();
        sTelefono = telefono.getText().toString();
        sEmail = email.getText().toString();
        sCiudad = spinner.getSelectedItem().toString();

        String resultdo = sNombre + " " + sApellido +", " + sCiudad + "\n" +
                sTelefono + "\n" + sEmail;

        if (!esNuevo)
            lista.remove(contacto);
        lista.add(resultdo);
        Collections.sort(lista);

        Intent i = new Intent(this, ContactosActivity.class);
        i.putExtra("lista",lista);
        Toast.makeText(this,"Guardando cambios...",Toast.LENGTH_SHORT).show();
        startActivity(i);
        this.finish();

    }

    private class ProcessJson extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                try{
                    data.clear();
                    JSONArray jsonArray = new JSONArray(s);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        data.add(jsonObject.getString("name"));
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(EditarActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    data);
                    adapter.notifyDataSetChanged();
                    spinner.setAdapter(adapter);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0];
            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.getHTTPData(urlString);
            return stream;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ContactosActivity.class);
        i.putExtra("lista",lista);
        Toast.makeText(this,"Cargando agenda...",Toast.LENGTH_SHORT).show();
        startActivity(i);
        this.finish();
    }
}
