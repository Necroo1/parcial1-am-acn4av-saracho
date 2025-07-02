package com.example.parcial1_am_acn4av_saracho;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etPassword, etConfirmarPassword;
    private Button btnRegistrate;
    private TextView tvLoginLink;
    private SignInButton btnGoogleRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Usá tu XML

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnRegistrate = findViewById(R.id.btnRegistrate);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        btnGoogleRegister = findViewById(R.id.btnGoogleRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // --- Registro tradicional ---
        btnRegistrate.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmar = etConfirmarPassword.getText().toString().trim();

            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmar)) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmar)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("nombre", nombre);
                            userMap.put("apellido", apellido);
                            userMap.put("email", email);
                            userMap.put("creado", FieldValue.serverTimestamp());
                            db.collection("usuarios").document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        // Redirige a Login
                                        startActivity(new Intent(this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error guardando usuario", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            String msg = "Error: ";
                            if (task.getException() != null) {
                                msg += task.getException().getMessage();
                            }
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // --- Ir a login ---
        tvLoginLink.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        // --- Registro con Google ---
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleRegister.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Error con Google Sign-In: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            DocumentReference userRef = db.collection("usuarios").document(user.getUid());
                            userRef.get().addOnSuccessListener(document -> {
                                if (!document.exists()) {
                                    // Obtener nombre/apellido del displayName de Google
                                    String displayName = user.getDisplayName();
                                    String nombre = "";
                                    String apellido = "";
                                    if (displayName != null && displayName.trim().contains(" ")) {
                                        int idx = displayName.lastIndexOf(' ');
                                        nombre = displayName.substring(0, idx).trim();
                                        apellido = displayName.substring(idx + 1).trim();
                                    } else if (displayName != null) {
                                        nombre = displayName;
                                    }
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("nombre", nombre);
                                    userMap.put("apellido", apellido);
                                    userMap.put("email", user.getEmail());
                                    userMap.put("creado", FieldValue.serverTimestamp());
                                    userRef.set(userMap)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Registro con Google exitoso", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(this, LoginActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Error guardando usuario", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    // Usuario ya existe
                                    Toast.makeText(this, "Ya existe un usuario con esta cuenta", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        String msg = "Error autenticando con Google: ";
                        if (task.getException() != null) {
                            msg += task.getException().getMessage();
                        }
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
