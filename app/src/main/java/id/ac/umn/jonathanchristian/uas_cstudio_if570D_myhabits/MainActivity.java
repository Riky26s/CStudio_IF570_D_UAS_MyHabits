package id.ac.umn.jonathanchristian.uas_cstudio_if570D_myhabits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private DatabaseReference databaseReference;
    String UserID="";

    CircleImageView uimage;
    DatabaseReference dbreference;

    DatabaseReference databaseU;
    DatabaseReference databaseD;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FloatingActionButton fab;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<Habitku> listHabit;
    private RecyclerView rvView;
    TextView tvakun;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        rvView = findViewById(R.id.rvView);

        uimage = findViewById(R.id.profile_pic2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        dbreference=FirebaseDatabase.getInstance().getReference().child("User");

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        rvView.setLayoutManager(mLayout);
        rvView.setItemAnimator(new DefaultItemAnimator());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Detilactivity detilactivity = new Detilactivity("","","","","","Tambah");
                detilactivity.show(getSupportFragmentManager(),"form");
            }
        });
        mAuth = FirebaseAuth.getInstance();
        showData();
    }

    private void showData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseU = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        databaseU.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    final TextView tvakun = (TextView) findViewById(R.id.tvakun);
                    String Username = userProfile.Username;
                    String email = userProfile.email;
                    String password = userProfile.password;
                    tvakun.setText("Selamat datang, " + Username + "!");


                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        databaseD = FirebaseDatabase.getInstance().getReference();
        databaseD.child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listHabit = new ArrayList<>();
                for(DataSnapshot item : snapshot.getChildren()) {
                    Habitku hbt = item.getValue(Habitku.class);
                    hbt.setKey(item.getKey());
                    listHabit.add(hbt);
                }
                recyclerAdapter = new RecyclerAdapter(listHabit, MainActivity.this);
                rvView.setAdapter(recyclerAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan saat pengambilan data!", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserID=user.getUid();
        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Glide.with(getApplicationContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_settings) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }else{
            if (id == R.id.delete_settings ){
                database.child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).removeValue();
                return true;
            }else{
                if (id == R.id.editprofil){
                    startActivity(new Intent(MainActivity.this, edit_profile.class));
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }



}