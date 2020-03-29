package com.view.aula4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tvNome, tvTelefone;
    Button btContato, btContatos, btWeb, btCall, btMaps1, btMaps2, btMaps3, btPicIti;
    ImageButton btApagarImagem;
    ImageView ivImagemCapturada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNome = (TextView) findViewById(R.id.tvNome);
        tvTelefone = (TextView) findViewById(R.id.tvTelefone);
        btContato = (Button) findViewById(R.id.btContato);
        btContato.setOnClickListener(trataEvento);
        btContatos = (Button) findViewById(R.id.btContatos);
        btContatos.setOnClickListener(trataEvento);
        btWeb = (Button) findViewById(R.id.btWeb);
        btWeb.setOnClickListener(trataEvento);
        btCall = (Button) findViewById(R.id.btCall);
        btCall.setOnClickListener(trataEvento);
        btMaps1 = (Button) findViewById(R.id.btMaps1);
        btMaps1.setOnClickListener(trataEvento);
        btMaps2 = (Button) findViewById(R.id.btMaps2);
        btMaps2.setOnClickListener(trataEvento);
        btMaps3 = (Button) findViewById(R.id.btMaps3);
        btMaps3.setOnClickListener(trataEvento);
        btPicIti = (Button) findViewById(R.id.btPicIti);
        btPicIti.setOnClickListener(trataEvento);
        btApagarImagem = (ImageButton) findViewById(R.id.btApagarImagem);
        btApagarImagem.setOnClickListener(trataEvento);
        ivImagemCapturada = (ImageView) findViewById(R.id.ivImagemCapturada);
    }

    View.OnClickListener trataEvento = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btContato) {
                Uri uri = Uri.parse("content://com.android.contacts/contacts/1");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            } else if (view == btContatos) {
                Uri uri = Uri.parse("content://com.android.contacts/contacts");
                Intent it = new Intent(Intent.ACTION_PICK, uri);
                startActivityForResult(it, 1);
            } else if (view == btWeb) {
                Uri uri = Uri.parse("https://www.unisc.br/pt/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            } else if (view == btCall) {
                try {
                    verificaPermissaoLigacao();
                    Uri uri = Uri.parse("tel: 998556622");
                    Intent it = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(it);
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Erro: "+ex, Toast.LENGTH_SHORT).show();
                }
            }else if(view == btMaps1){
                Uri uriGeo = Uri.parse("geo:0,0?q=henrique+guilherme+uhlmann,1480,venancio+aires");
                Intent it = new Intent(android.content.Intent.ACTION_VIEW, uriGeo);
                startActivity(it);
            }else if(view == btMaps2){
                Uri uriCoo = Uri.parse("geo:-29.607368,-52.192024");
                Intent it = new Intent(Intent.ACTION_VIEW, uriCoo);
                startActivity(it);
            }else if(view == btMaps3){
                String partida = "-29.6040786,-52.2223276";
                String destino = "-29.6979873,-52.4384315";
                String url = "http://maps.google.com/maps?f=d&saddr="+partida+"&daddr="+destino+"&hl=pt";
                Uri uriLink = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_VIEW, uriLink);
                startActivity(it);
            }else if(view == btPicIti){
                try {
                    verificandoPermissaoCamera();
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, 2);
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Erro: "+ex, Toast.LENGTH_SHORT).show();
                }
            }else if(view == btApagarImagem){
                ivImagemCapturada.setImageResource(R.drawable.imagem);
            }
        }
    };

    public void verificandoPermissaoCamera(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    public void verificandoPermissaoDosContatos(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }
    }

    public void verificaPermissaoLigacao(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        }
    }

    /**
     * Request Code == 1 vem da seleção dos contatos
     * Request Code == 2 vem da captura de imagem
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            try {
                Uri uri = data.getData();
                String nome = null;
                String id = null;

                verificandoPermissaoDosContatos();

                Cursor c = getContentResolver().query(uri, null, null,
                        null, null);
                c.moveToNext();
                int nomeCol = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                int idCol = c.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                nome = c.getString(nomeCol);
                id = c.getString(idCol);
                c.close();

                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                " = " + id,null,null);
                phones.moveToNext();
                String phoneNumber = phones.getString(phones.getColumnIndexOrThrow
                        (ContactsContract.CommonDataKinds.Phone.NUMBER));
                phones.close();

                tvNome.setText(nome);
                tvTelefone.setText(phoneNumber);
            }catch (Exception ex){

            }
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            Bundle extra = data.getExtras();
            Bitmap bmp = (Bitmap) extra.get("data");
            ivImagemCapturada.setImageBitmap(bmp);
        }
    }
}
