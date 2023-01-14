package com.example.productlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;


import java.io.ByteArrayOutputStream;

@SuppressWarnings("ALL")
public class AjoutProd extends AppCompatActivity {
    EditText libelle,codebarre, prix;
    Button ajouter,camera;
    CheckBox dispo;
    String aa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajoutprod);

        libelle= findViewById(R.id.libelle);
        codebarre= findViewById(R.id.codebr);
        dispo = findViewById(R.id.dispo);
        prix= findViewById(R.id.price);

        ajouter= findViewById(R.id.ajouter);
        camera= findViewById(R.id.camera);

        if(ContextCompat.checkSelfPermission(AjoutProd.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AjoutProd.this,new String[]{
                    Manifest.permission.CAMERA
            },101);
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,101);
                //ImagePicker.Companion.with(MainActivityAjtP.this).cropp().MaxResultSize(1080,1080).start(101);
            }
        });

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent();
                i.putExtra("libelle","libelle: "+libelle.getText().toString());
                i.putExtra("codeBarre","Code: "+codebarre.getText().toString());
                i.putExtra("disponible",dispo.isChecked());
                i.putExtra("prix","prix: "+prix.getText().toString());
           /*     if(dispo.isChecked()){
                    String x= "disponible";
               i.putExtra("disponible",x);
                     }else{
                   String x="Hors ,String disponible";
                    i.putExtra("disponible","Hors ,String disponible");
                }*/
                i.putExtra("image",aa);
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });
    }
    @Override
    protected  void  onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK)
        {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ImageView imageview = (ImageView) findViewById(R.id.image);
            imageview.setImageBitmap(image);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            aa= Base64.encodeToString(byteArray, Base64.DEFAULT);


        }
    }
}