package com.example.groupup;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    String customer;
    SignInButton signInButton;
    String name ="",email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.sign_in_button);
        if (mAuth.getCurrentUser()!= null){
//            Log.d("footer",mAuth.getCurrentUser().getEmail());
            Intent intent = new Intent(MainActivity.this,home.class);
            intent.putExtra("email",mAuth.getCurrentUser().getEmail()+"");
            startActivity(intent);
        }else {
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        }





    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.sign_out_button:
//                signout();
//                break;
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

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
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Snackbar.make(findViewById(R.id.textView), "Authentication Success.", Snackbar.LENGTH_SHORT).show();
                            customer = user.getUid() + " " + user.getEmail() + " " + user.getDisplayName() + " " + user.getProviderId() + " ";
                            Log.d("footer",customer);
                            name = user.getDisplayName();
                            email = user.getEmail();
                            saveData();
                            Intent intent = new Intent(MainActivity.this,home.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
//                            customer = user.getDisplayName()+"/"+user.getEmail()+"/"+user.getPhoneNumber()+"/"+user.getUid();
//                            txt.setText(customer);
//                            updateUI(customer);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.textView), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(String account) {
        if (account != null) {
            Snackbar.make(findViewById(R.id.textView), "account pass", Snackbar.LENGTH_SHORT).show();

//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
            Snackbar.make(findViewById(R.id.textView), "account = null", Snackbar.LENGTH_SHORT).show();
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    public void skipbutton(View v){
        Intent in = new Intent(this,home.class);
        in.putExtra("email","thanapatza2011@gmail.com");
        startActivity(in);
    }
    public void skipPlace(View v){
        Intent in = new Intent(this,place.class);
        startActivity(in);
    }
    public boolean saveData() {
        String url = "http://www.groupupdb.com/android/createuser.php";
        url += "?sName=" + name;
        url += "&sEmail=" + email;
//        Log.d("footer",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Log", "Volley::onResponse():" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        return true;
    }

}
