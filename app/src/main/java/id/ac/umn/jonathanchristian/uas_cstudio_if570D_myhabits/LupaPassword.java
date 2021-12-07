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
import com.google.firebase.auth.FirebaseAuth;

public class LupaPassword extends AppCompatActivity {
    private EditText etmail1;
    private TextView tvRegis3, tvlogin2;
    private AppCompatButton btnresetpass;
    FirebaseAuth mAuth;
    private ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        etmail1= (EditText) findViewById(R.id.etmail1);
        ProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        tvRegis3 = (TextView) findViewById(R.id.tvRegis3);
        tvRegis3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LupaPassword.this, RegisActivity.class);
                startActivity(intent);
            }
        });

        tvlogin2 = (TextView) findViewById(R.id.tvlogin2);
        tvlogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LupaPassword.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnresetpass = (AppCompatButton) findViewById(R.id.btnresetpass);
        btnresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    public void resetPassword(){
        String email = etmail1.getText().toString();
        if (TextUtils.isEmpty(email)) {
            input((EditText) etmail1, "Email");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etmail1.setError("Tolong masukan email yang valid");
            etmail1.requestFocus();
            return;
        } else {
            ProgressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener
                    (new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ProgressBar.setVisibility(View.GONE);
                                Toast.makeText(LupaPassword.this, "Cek email anda untuk mereset password", Toast.LENGTH_SHORT).show();

                            } else {
                                ProgressBar.setVisibility(View.GONE);
                                Toast.makeText(LupaPassword.this, "Mohon maaf ada kesalahan! Silahkan coba lagi", Toast.LENGTH_SHORT).show();

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