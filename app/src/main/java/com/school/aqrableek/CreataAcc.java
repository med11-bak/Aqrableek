package com.school.aqrableek;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aqrableek.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CreataAcc extends AppCompatActivity {
EditText nomuser,password,confirm,tel;

Spinner ville,txtnawahi;
Spinner ecole,pays;
    ImageView userpic;
    String linkImage;
    Button reg;
    public Uri imageURL=null;
    CheckBox stilstd;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressBar prg;
    private FirebaseAuth mAuth;
    //TODO:// declare a list to fill it from json
    ArrayList<String> scls = new ArrayList<>();
    ArrayList<String> villes = new ArrayList<>();
    ArrayList<String> dists = new ArrayList<>();
    ArrayList<String> payss = new ArrayList<>();
    ArrayList<String> regions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creata_acc);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ArrayAdapter<String> adapterpro = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ViewHolder.professions);
        ini();
        ecole.setAdapter(adapterpro);

        //TODO : calling methode to fill the list
         FillFromJson("scl.json","scls","A",scls);
        FillFromJson("ma.json","cities","city",villes);
        FillFromJson("fich.json","professions","name",dists);
        FillFromJson("world.json","countries","name",payss);
        FillFromJson("regions.json","cities","region",regions);




        //TODO : declare arrayAdpater to fill the spinner
        ArrayAdapter<String > adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, scls);
        ArrayAdapter<String>  adapter1= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,villes);
        ArrayAdapter<String>  adapter2= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,regions);
        ArrayAdapter<String>  adapterpay= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,payss);
        ArrayAdapter<String>  adapregions= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,regions);


        stilstd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stilstd.isChecked()){
                    ecole.setAdapter(adapter);
                }else{
                    ecole.setAdapter(adapterpro);
                }
            }
        });
        //TODO : Set adapter to spinner



        ville.setAdapter(adapter1);
        txtnawahi.setAdapter(adapregions);
        pays.setAdapter(adapterpay);


        pays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!= payss.indexOf("المغرب")){
                    ville.setEnabled(false);
                }else{
                    ville.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepic();
            }
        });

        //TODO: registre Button
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((nomuser.getText().toString() == "" || nomuser.getText().toString() == null)){
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!password.getText().toString().equals(confirm.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                    } else {


                        prg.setVisibility(View.VISIBLE);
                        reg.setVisibility(View.GONE);
                        mAuth.createUserWithEmailAndPassword(tel.getText() + "@gmail.com".toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            try {
                                                if(linkImage == null){
                                                    linkImage = "https://firebasestorage.googleapis.com/v0/b/aqrableek-f133a.appspot.com/o/585e4bcdcb11b227491c3396.png?alt=media&token=19ca3690-a442-4b44-9379-8f3451a472b8";
                                                }

                                                User user = new User(nomuser.getText().toString(), tel.getText()
                                                        .toString(), "maroc", ville.getSelectedItem().toString(), ecole.getSelectedItem().toString(), password.getText().toString(), linkImage.toString(),
                                                        txtnawahi.getSelectedItem().toString());

                                            FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("users/")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("info")
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        String currentuserid = user.getUid();

                                                        SharedPreferences sp = getSharedPreferences("currentid", Activity.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sp.edit();
                                                        editor.putString("cru",currentuserid);
                                                        editor.apply();
                                                        Toast.makeText(CreataAcc.this, "registred", Toast.LENGTH_SHORT).show();
                                                        prg.setVisibility(View.INVISIBLE);
                                                        reg.setVisibility(View.GONE);
                                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                                    } else {
                                                        prg.setVisibility(View.GONE);
                                                        reg.setVisibility(View.VISIBLE);
                                                        Toast.makeText(CreataAcc.this, "failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            });
                                            }catch(Exception ex){
                                                Toast.makeText(getApplicationContext(), ""+ex.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            prg.setVisibility(View.GONE);
                                            reg.setVisibility(View.VISIBLE);
                                            Toast.makeText(CreataAcc.this, "failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                });
                    } //end else
                }
            }
        });
    }

    //TODO : choosing pic profile
    private void choosepic() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        }catch(Exception ignored){

        }
    }

    //TODO : after choosing pic profile
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            imageURL = data.getData();

            Toast.makeText(getApplicationContext(), "image "+imageURL, Toast.LENGTH_SHORT).show();
            userpic.setImageURI(imageURL);
            uploadpicture();
        }catch(Exception ignored){

        }
    }

    //TODO : sending pic to DB
    private void uploadpicture() {
        final String randomkey = UUID.randomUUID().toString();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("en cours...");
        pd.show();
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child(randomkey);

        //TODO : uploading img

        riversRef.putFile(imageURL)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        pd.dismiss();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //TODO : keeping the link of inserted pic in a variable so we can affect it to current user
                                    linkImage = uri.toString();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), ""+exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double percent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("En cours..." + percent);
                    }
                });
    }

    //TODO : initilize controls from UI
    public void ini(){
        mAuth = FirebaseAuth.getInstance();
        nomuser = findViewById(R.id.txtnom);
        pays = findViewById(R.id.txtpay);
        ville = findViewById(R.id.txtville);
        stilstd = findViewById(R.id.stilstd);
        ecole = findViewById(R.id.txtecole);
        password = findViewById(R.id.txtpass);
        confirm=findViewById(R.id.txtpass2);
        reg = findViewById(R.id.reg);
        prg = findViewById(R.id.prg);
        tel = findViewById(R.id.txtphone);
         userpic = findViewById(R.id.userpic);
        txtnawahi = findViewById(R.id.regionsDrop);
    }

    //TODO: removing shared pref from storage if user logged out
    public static void removeshared(Context context){
        SharedPreferences pref = context.getSharedPreferences("currentid",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("cru");
        editor.apply();
    }

    //TODO : methode to get the json in object String
    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    //TODO : a methode to fill ArrayList from json file locally
    public void FillFromJson(String filename,String Arrayname,String column,ArrayList<String> array){
        try
        {
            String jsonLocation = AssetJSONFile(filename, getApplicationContext());
            JSONObject jsonobject = new JSONObject(jsonLocation);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray(Arrayname);
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject jb =(JSONObject) jarray.get(i);
                String formula = jb.getString(column);
                array.add(formula);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

    private void selectSpinnerValue(Spinner spinner, String myString)
    {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }
}