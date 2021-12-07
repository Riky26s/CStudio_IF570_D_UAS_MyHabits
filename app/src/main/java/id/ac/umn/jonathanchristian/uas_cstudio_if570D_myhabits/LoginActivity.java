package id.ac.umn.jonathanchristian.uas_cstudio_if570D_myhabits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextView tvRegis2, tvLupaPassword;
    private Button btnsignin;
    private EditText etmail, etpass;
    private FirebaseAuth mAuth;
    private ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        tvRegis2 = (TextView) findViewById(R.id.tvRegis2);
        tvRegis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisActivity.class);
                startActivity(intent);
            }
        });

        tvLupaPassword = (TextView) findViewById(R.id.tvLupaPassword);
        tvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LupaPassword.class);
                startActivity(intent);
            }
        });

        etmail = (EditText) findViewById(R.id.etmail);
        etpass = (EditText) findViewById(R.id.etpass);
        btnsignin = (Button) findViewById(R.id.btnsignin);
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login();
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    private void Login() {
        String email = etmail.getText().toString();
        String password = etpass.getText().toString();
        if (TextUtils.isEmpty(email)) {
            input((EditText) etmail, "Email");
        } else if (TextUtils.isEmpty(password)) {
            input((EditText) etpass, "Password");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etmail.setError("Tolong masukan email yang valid");
            etmail.requestFocus();
            return;
        } else {
            ProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth
                                        .getInstance().getCurrentUser();
                                if(user.isEmailVerified()){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    user.sendEmailVerification();
                                    ProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Cek email untuk verifikasi akun anda", Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                ProgressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Gagal login, harap coba lagi!", Toast.LENGTH_SHORT).show();

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