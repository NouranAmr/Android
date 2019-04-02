package com.day8.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterUser extends AppCompatActivity {

    private static final String TAG ="User" ;
    Button registerBtn ,loginBtn;
    EditText emailTxt,passwordtxt;
    private FirebaseAuth firebaseAuth;
    private SignInButton googleBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        firebaseAuth=FirebaseAuth.getInstance();
        registerBtn=findViewById(R.id.registerBtn);
        loginBtn=findViewById(R.id.loginBtn);
        emailTxt=findViewById(R.id.emailTxt);
        passwordtxt=findViewById(R.id.passwordTxt);
        googleBtn=findViewById(R.id.googleBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth=FirebaseAuth.getInstance();
                String email=emailTxt.getText().toString();
                String password=passwordtxt.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterUser.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                    Log.i(TAG,task.toString());
                                }
                                else
                                {
                                    Toast.makeText(RegisterUser.this, task.getResult().toString().trim(), Toast.LENGTH_SHORT).show();
                                    Log.i(TAG,task.toString());
                                }

                            }
                        }
                );
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailTxt.getText().toString();
                String password=passwordtxt.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterUser.this, "Login Scuessfully", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(RegisterUser.this,NoteList.class);
                                    startActivity(intent);

                                }
                                else
                                {
                                    Toast.makeText(RegisterUser.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(RegisterUser.this, gso);
                firebaseAuth = FirebaseAuth.getInstance();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
              //  updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            finish();
                            Toast.makeText(RegisterUser.this, "Login with facebook Sucessfully", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterUser.this, "Login with facebook Failes", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();

    }

}
