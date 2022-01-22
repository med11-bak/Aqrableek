package com.school.aqrableek;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aqrableek.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Secondfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Secondfragment extends Fragment {
    final ArrayList<String> mylist = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    public static TextView reply ;
    Post post = new Post();
    DatabaseReference ref,ref2;
    boolean upload=false;
    boolean isOpened = false;
    boolean isAudio = false;
    LocalDate currentdate = LocalDate.now();
    Uri imageURL;
    public static RelativeLayout linearreply;
    public static EditText inputcontent;
    private MediaRecorder recorder;
    public String linkImgPost,us,AudioLink;
    public static boolean isReply;

    DateTimeFormatter dtf;
    public static InputMethodManager imm;
    public static ImageView closebtn;
    public static final int RECORD_AUDIO = 0;
    RecyclerView rec;
    ImageView camselected,vocalOrmsg,btnpic,stp,btnmsg;
    String fileName;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Secondfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragement newInstance(String param1, String param2) {
        FirstFragement fragment = new FirstFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_fragement, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar;
        //    actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
        //  ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1B9098"));
        // actionBar.setBackgroundDrawable(colorDrawable);
        ini();
        //TODO: getting id user from shared pref
        SharedPreferences sp = getActivity().getSharedPreferences("currentid", Activity.MODE_PRIVATE);
        us = sp.getString("cru","");


        //TODO: gettting current user's id
        //1  FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        //  String currentuserid = user.getUid();
        fileName =getActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
        String finalFileName = fileName;

        //TODO: hidding audio or btnSend
        inputcontent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != ""){
                    btnmsg.setVisibility(View.VISIBLE);
                }
                else{
                    btnmsg.setVisibility(View.GONE);
                    stp.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    btnmsg.setVisibility(View.GONE);

                }
            }
        });

        //TODO: upload the pic in post
        btnpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepic();
            }
        });

        //TODO: record audio
        vocalOrmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stp.setVisibility(View.VISIBLE);
                if(ismic()){
                    getMicPer();
                }
                try {

                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    recorder.setOutputFile(finalFileName);
                    recorder.prepare();
                    recorder.start();

                } catch (IOException e) {

                }
            }
        });

        //TODO: stop audio and save it
        //TODO : insertion audio post
        stp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stp.setVisibility(View.GONE);
                try{
                    recorder.stop();     // stop recording
                    recorder.release();
                    uploadAudio();
                    recorder=null;
                }catch (Exception ignored){

                }


            }
        });


        //TODO: for scrolling problem isOpened
        isOpened = true;
        ref2 = firebaseDatabase.getReference().child("posts/");
        rec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        //TODO : getting post's key (unique by firebase)
        final String[] IDpost_virtuel = {FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("posts/").push().getKey()};
        post.setID(IDpost_virtuel[0]);


        //TODO: getting user's info (tof + name)
        ref =firebaseDatabase.getReference().child("users").child(us).child("info");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mylist.add(snapshot.getValue().toString());
                }
                post.setPicture(mylist.get(2));
                post.setNomUser(mylist.get(3));
                post.country=mylist.get(5);
                post.ville=mylist.get(7);
                Toast.makeText(getContext(), ""+mylist.get(7), Toast.LENGTH_SHORT).show();
                post.district=mylist.get(1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //TODO: insertion d'un post
        btnmsg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                post.setContent(inputcontent.getText().toString().trim());
                post.year =currentdate.getYear();
                post.month =currentdate.getMonthValue();
                post.day = currentdate.getDayOfMonth();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                post.fulltime = dtf.format(now);
                post.heur = LocalDateTime.now().getHour();
                post.min = LocalDateTime.now().getMinute();
                post.replyingto ="replying to " + ViewHolder.nomUserTOREPLY;
                post.replyContent =  reply.getText().toString();
                post.audio = "none";

                //TODO: check if user uploaed pic in post
                if(upload == true){
                    post.image = linkImgPost;
                }
                else{
                    post.image = "none";
                }

                //TODO: sending post to DB and scrolling to bottom automatically
                FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("posts/").child(IDpost_virtuel[0])
                        .setValue(post);
                IDpost_virtuel[0] =  FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("posts/").push().getKey();
                inputcontent.setText("");
                rec.smoothScrollToPosition(rec.getAdapter().getItemCount()-1);
                isOpened = false;
                reply.setVisibility(View.GONE);
                camselected.setVisibility(View.GONE);
                btnpic.setVisibility(View.VISIBLE);
            }
        });

        //TODO: user already scrolled so we dont need to force it scroll to bottom
        rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isOpened=false;
            }
        });


    }






    //TODO: affichage des données en recyclerview
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        try {
            super.onStart();
            FirebaseRecyclerAdapter<Post, ViewHolder> firebaseAdap = new FirebaseRecyclerAdapter<Post, ViewHolder>(
                    Post.class, R.layout.row, ViewHolder.class,ref2.orderByChild("ville").equalTo(FirstFragement.cityUser)) {
                @Override
                protected void populateViewHolder(ViewHolder viewHolder, Post model, int i) {
                    viewHolder.setdetails(getActivity().getApplicationContext(), model.getNomUser(), model.getPicture(), model.getContent(), model.fulltime, model.getID(), model.replyingto, model.replyContent
                            , model.year, model.month, model.day, model.heur, model.min, model.image, model.audio);
                    if (isOpened) {
                        // check if user has already opened the app before (for number views purpose)
                        rec.smoothScrollToPosition(rec.getAdapter().getItemCount() - 1);
                    } else {

                    }

                }
            };
            rec.setAdapter(firebaseAdap);
        }catch(Exception ignored){

        }
    }

    //TODO: closing pop up of reply
    public void clos(View view) {
        linearreply.setVisibility(View.GONE);
        closebtn.setVisibility(View.GONE);
        isReply = false;
    }


    //TODO: checking if user has mic
    public boolean ismic(){
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        return false;
    }

    //TODO: asking for permission to user mic
    private void getMicPer(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
        }
    }

    //TODO: uploading pic
    private void choosepic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    //TODO: uploading pic (after coming back from choosing pic)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageURL = data.getData();
        uploadpicture();
        camselected.setVisibility(View.VISIBLE);
        btnpic.setVisibility(View.GONE);
        vocalOrmsg.setVisibility(View.GONE);
        btnmsg.setVisibility(View.VISIBLE);
    }


    //TODO: uploading audio to DB
    private void uploadAudio() {
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("audios");
        Uri uri = Uri.fromFile(new File(fileName));
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //TODO: successfully audio sent to DB && we get the link ofc
                                AudioLink= uri.toString();
                                Toast.makeText(getActivity(), ""+AudioLink, Toast.LENGTH_SHORT).show();
                                //TODO : getting post's key (unique by firebase)
                                final String[] IDpost_virtuel = {FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("posts/").push().getKey()};
                                post.setID(IDpost_virtuel[0]);


                                //TODO: getting user's info (tof + name)
                                ref =firebaseDatabase.getReference().child("users").child(us).child("info");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            mylist.add(snapshot.getValue().toString());
                                        }
                                        post.setPicture(mylist.get(2));
                                        post.setNomUser(mylist.get(3));
                                        post.country=mylist.get(5);
                                        post.ville=mylist.get(7);
                                        post.district=mylist.get(1);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                post.setContent(inputcontent.getText().toString().trim());
                                post.year =currentdate.getYear();
                                post.month =currentdate.getMonthValue();
                                post.day = currentdate.getDayOfMonth();
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                LocalDateTime now = LocalDateTime.now();
                                post.fulltime = dtf.format(now);
                                post.heur = LocalDateTime.now().getHour();
                                post.min = LocalDateTime.now().getMinute();
                                post.replyingto ="replying to " + ViewHolder.nomUserTOREPLY;
                                post.replyContent =  reply.getText().toString();
                                post.audio = AudioLink;
                                //TODO: check if user uploaed pic in post
                                if(upload == true){
                                    post.image = linkImgPost;
                                }
                                else{
                                    post.image = "none";
                                }

                                //TODO: sending post to DB and scrolling to bottom automatically
                                FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("posts/").child(IDpost_virtuel[0])
                                        .setValue(post);
                                IDpost_virtuel[0] =  FirebaseDatabase.getInstance("https://aqrableek-f133a-default-rtdb.firebaseio.com/").getReference("posts/").push().getKey();
                                inputcontent.setText("");
                                rec.smoothScrollToPosition(rec.getAdapter().getItemCount()-1);
                                isOpened = false;
                                reply.setVisibility(View.GONE);

                                camselected.setVisibility(View.GONE);
                                btnpic.setVisibility(View.VISIBLE);

                                recorder=null;
                                // set state to idle
                                Toast.makeText(getActivity(), "recording stopped...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }


    //TODO: sending picture of post to DB and getting link

    private void uploadpicture() {
        final String randomkey = UUID.randomUUID().toString();
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("en cours...");
        pd.show();
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child(randomkey);
        riversRef.putFile(imageURL)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        pd.dismiss();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                linkImgPost = uri.toString();
                                upload = true;
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        pd.dismiss();
                        Toast.makeText(getContext(), ""+exception.toString(), Toast.LENGTH_SHORT).show();
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
    //TODO: initialize controls
    public void ini(){
        View view = getView();
        assert view != null;
        reply =getView().findViewById(R.id.inputreplyFrag);
        firebaseDatabase = FirebaseDatabase.getInstance();
        inputcontent = getView().findViewById(R.id.inputcontent);
        btnpic = getView().findViewById(R.id.btnpic);
        linearreply=getView().findViewById(R.id.linearreply);
        camselected=getView().findViewById(R.id.camselected);
        closebtn = getView().findViewById(R.id.closbtn);
        rec = getView().findViewById(R.id.recyclerHome);
        vocalOrmsg=getView().findViewById(R.id.vocalOrmsg);
        stp = getView().findViewById(R.id.stpAudio);
        btnmsg = getView().findViewById(R.id.btnmsg);
        vocalOrmsg.setVisibility(View.VISIBLE);
    }
}