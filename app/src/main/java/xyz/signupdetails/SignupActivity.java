package xyz.signupdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCfm;
    private EditText etFname;
    private EditText etLname;
    private EditText etEml;
    private EditText etPwd;
    private EditText etRpwd;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        btnCfm = (Button) findViewById(R.id.btnCfm);
        etFname = (EditText) findViewById(R.id.etFname);
        etLname = (EditText) findViewById(R.id.etLname);
        etEml = (EditText) findViewById(R.id.etEml);
        etPwd = (EditText) findViewById(R.id.etPwd);
        etRpwd = (EditText) findViewById(R.id.etRpwd);
        btnCfm.setOnClickListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(SignupActivity.this, DetailActivity.class));
                } else {
                    // User is signed out
                    Log.d("SignUp", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        final String firstName = etFname.getText().toString().trim();
        final String lastName = etLname.getText().toString().trim();
        final String email = etEml.getText().toString().trim();
        String password = etPwd.getText().toString().trim();
        String confirm_password = etRpwd.getText().toString().trim();
        if (etFname.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignupActivity.this, "Enter the first name", Toast.LENGTH_SHORT).show();
            return;
        } else if (etLname.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignupActivity.this, "Enter the last name", Toast.LENGTH_SHORT).show();
            return;
        } else if (etEml.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignupActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (etPwd.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignupActivity.this, "Enter the password", Toast.LENGTH_SHORT).show();
            return;
        } else if (etRpwd.getText().toString().trim().length() <= 0) {
            Toast.makeText(SignupActivity.this, "Re-Enter the password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirm_password.equals(password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Signup", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "ADDING extra info", Toast.LENGTH_SHORT).show();
                                String uid = task.getResult().getUser().getUid();
                                HashMap<String, Object> dataMap = new HashMap<String, Object>();
                                dataMap.put("email", email);
                                dataMap.put("first_name", firstName);
                                dataMap.put("last_name", lastName);
                                FirebaseDatabase.getInstance().getReference("tiffin_provider_profile")
                                        .child(uid)
                                        .setValue(dataMap, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    Toast.makeText(SignupActivity.this, "success", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignupActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
        v.setEnabled(false);
    }
}
