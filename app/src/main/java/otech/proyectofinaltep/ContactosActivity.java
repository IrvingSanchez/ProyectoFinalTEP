package otech.proyectofinaltep;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;

public class ContactosActivity extends AppCompatActivity {

    public ListView lv;
    EditText buscar;
    Toolbar tb;
    ArrayList<String> listagente;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        buscar = (EditText) findViewById(R.id.buscar);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        lv = (ListView) findViewById(R.id.listView);


        listagente = consultaAgenda();


        if (getIntent().getExtras() != null) {
            listagente = (ArrayList<String>)getIntent().getExtras().get("lista");
        }

        adapter = new
                ArrayAdapter<String>(
                ContactosActivity.this,
                android.R.layout.simple_expandable_list_item_1,
                listagente);

        lv.setAdapter(adapter);

        final Context c = this;

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog dialogo;
                AlertDialog.Builder constructor = new AlertDialog.Builder(c);

                CharSequence[] items = {"Llamar", "Enviar Email", "Editar", "Eliminar"};

                constructor.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                int permissionCheck = ContextCompat.checkSelfPermission(
                                        c, Manifest.permission.CALL_PHONE);
                                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                    Log.i("Mensaje", "No se tiene permiso para realizar llamadas telefónicas.");
                                    ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.CALL_PHONE}, 225);
                                } else {
                                    Log.i("Mensaje", "Se tiene permiso!");
                                }

                                String numero = lv.getItemAtPosition(position).toString();
                                numero = numero.substring((numero.indexOf("\n") + 1), numero.length());
                                numero = numero.substring(0, numero.indexOf("\n"));
                                try{
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numero, null));
                                    startActivity(intent);
                                }catch (Exception e)
                                {
                                    Log.e("MSJ",e.getMessage());
                                }

                                break;
                            case 1:
                                Intent in = new Intent(Intent.ACTION_SEND);
                                in.setData(Uri.parse("mailto:"));
                                in.setType("text/plain");
                                startActivity(in);
                                break;
                            case 2:
                                Intent i = new Intent(c,EditarActivity.class);
                                i.putExtra("lista",listagente);
                                i.putExtra("contacto",lv.getItemAtPosition(position).toString());
                                startActivity(i);
                                ((Activity)c).finish();
                                break;
                            case 3:
                                Toast.makeText(c,"Eliminado",Toast.LENGTH_SHORT).show();
                                listagente.remove(lv.getItemAtPosition(position).toString());
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                constructor.create();

                dialogo = constructor.create();
                dialogo.show();

                return false;
            }
        });
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ContactosActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.preferencias:
                Intent i = new Intent(this,PreferenciasActivity.class);
                startActivity(i);
                break;
            case R.id.salir:
                this.finish();
                break;
        }
        return true;
    }

    public ArrayList<String> consultaAgenda(){

        ArrayList<String> datos = new ArrayList<String>();


        Cursor c =
                getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

        while(c.moveToNext())
        {
            String telefono = "";
            String email = "";
            String ContactID =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

            String nombre =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            //System.out.println("Nombre: "+nombre+" _ID: "+ContactID);

            String hasPhone =c.getString(
                    c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if(Integer.parseInt(hasPhone) == 1)
            {

                Cursor phoneCursor =
                        getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        +"='"+ContactID+"'",
                                null, null);

                while(phoneCursor.moveToNext()){
                    String number =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String numberType =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    switch(Integer.parseInt(numberType)){
                        // inserta switch cases para manejar todos los tipos de
                        ///números de teléfono. Son 20 tipos.
                        default:
                            //System.out.println("Número de teléfono: "+number+"("+numberType+")");
                            break;
                    }
                    telefono = number;

                }
                phoneCursor.close();
            }
            else
            {
                System.out.println("No se encontraron números de teléfono");
            }

            Cursor emailCursor =
                    getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            new String[]
                                    { ContactsContract.CommonDataKinds.Email.DATA,
                                            ContactsContract.CommonDataKinds.Email.TYPE},
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    +"='"+ContactID+"'", null, null);

            while(emailCursor.moveToNext()){
                String emai =
                        emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                String emailType =
                        emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                /*
                switch(Integer.parseInt(emailType)){
                    case 1: datos.add(nombre + " Home: " + email);  break;
                    case 2: datos.add(nombre + " Work: " + email);  break;
                    case 3: datos.add(nombre + " Other: " + email); break;
                    case 4: datos.add(nombre + " Mobile: " + email); break;
                    case 5: datos.add(nombre + " Custom: " + email); break;
                }
                */email = emai;

            }
            datos.add(nombre + "\n" + telefono + "\n" + email);

            emailCursor.close();
        }
        c.close();

        Collections.sort(datos);
        return datos;
    } // Find de consultaAgenda()


    public void onClickInsertar(View v)
    {
        Intent i = new Intent(this,EditarActivity.class);
        i.putExtra("lista",listagente);
        i.putExtra("nuevo",true);
        startActivity(i);
        this.finish();
    }

}
