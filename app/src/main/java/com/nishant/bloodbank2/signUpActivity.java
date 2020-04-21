package com.nishant.bloodbank2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nishant.bloodbank2.common.Constants;
import com.nishant.bloodbank2.databinding.ActivitySignUpBinding;

import java.util.Objects;

public class signUpActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            userRegister(view);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAuth = FirebaseAuth.getInstance();

        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUpActivity.this, MainActivity.class));
            }
        });
        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUpActivity.this, findDonorActivity.class));
            }
        });

        binding.edtConfirmPasswordEditText.setOnKeyListener(this);
        binding.layoutBackground.setOnClickListener(this);
        binding.txtLogo.setOnClickListener(this);
        binding.txtAlready.setOnClickListener(this);
        binding.txtCreateAccount.setOnClickListener(this);
    }

    public void userRegister(View view) {
        String username = Objects.requireNonNull(binding.edtUsernameEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.edtPasswordEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.edtConfirmPasswordEditText.getText()).toString().trim();

        if (username.equals("")) {
            Toast.makeText(this, "Username Can't be Empty", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Confirm Password and Password is not Equal", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                FirebaseDatabase.getInstance().getReference().child("Non Registered User").child(Objects.requireNonNull(user).getUid()).setValue(Constants.userNotRegistered);
                                Intent intent = new Intent(signUpActivity.this, userDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(signUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //Hide Keyboard
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_background || view.getId() == R.id.txt_logo || view.getId() == R.id.txt_already || view.getId() == R.id.txt_createAccount) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }
}
