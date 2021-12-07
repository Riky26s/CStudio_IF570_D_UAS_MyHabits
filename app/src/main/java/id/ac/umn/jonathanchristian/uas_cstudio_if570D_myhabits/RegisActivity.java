package id.ac.umn.jonathanchristian.uas_cstudio_if570D_myhabits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisActivity extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private TextView btnsignin;
    private EditText etusername, etemail, etpassword;
    private AppCompatButton btncreate;
    private ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        etusername = (EditText) findViewById(R.id.etusername);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword= (EditText) findViewById(R.id.etpassword);
        ProgressBar= (ProgressBar) findViewById(R.id.ProgressBar);
        btncreate = (AppCompatButton) findViewById(R.id.btncreate);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        btnsignin = (TextView) findViewById(R.id.btnsignin);
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register() {
        String Username = etusername.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            input((EditText) etemail, "Email");
        } else if (TextUtils.isEmpty(password)) {
            input((EditText) etpassword, "Password");
        } else if(TextUtils.isEmpty(Username)) {
            input((EditText) etusername, "Username");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("Tolong tuliskan email yang valid");
            etemail.requestFocus();
            return;
        } else {
            ProgressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(Username, email, password);
                        FirebaseDatabase.getInstance().getReference
                                ("Users").child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(new OnCompleteListener
                                        <Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            ProgressBar.setVisibility

                                                    (View.GONE);

                                            Toast.makeText(RegisActivity.this, "Akun sukses terdaftar", Toast.LENGTH_SHORT).show();

                                        } else {
                                            ProgressBar.setVisibility
                                                    (View.GONE);
                                            Toast.makeText(RegisActivity.this, "Gagal untuk mendaftar, coba lagi!",
                                            Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    } else {
                        ProgressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisActivity.this, "Gagal mendaftar, Silahkan coba lagi", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void input(EditText txt, String s) {
        txt.setError(s+" tidak boleh kosong");
        txt.requestFocus();
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}