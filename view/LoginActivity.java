package com.example.asg02.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
import com.example.asg02.R;
import com.example.asg02.controller.account.LoginController;
import com.example.asg02.databinding.ActivityLoginBinding;
import com.example.asg02.model.User;
import com.example.asg02.utils.FirebaseUtils;
import com.example.asg02.utils.ViewUtils;

public class LoginActivity extends BaseActivity {
    private boolean isHidePassword = true;
    private ActivityLoginBinding binding;
    private LoginController loginController;
    private EditText editPassword;
    private EditText editId;
    private Button loginButton;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // use binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("LoginSharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // assign
        setSupportActionBar(binding.toolbar);
        loginController = new LoginController();
        editId = binding.editId;
        editPassword = binding.editPassword;
        loginButton = binding.loginButton;
        ImageView passwordIcon = binding.hidePasswordButton;

        editId.addTextChangedListener(ViewUtils.afterEditTextChanged(editId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editId.getText().toString().isEmpty()) {
                    editId.setError("Vui lòng nhập số điện thoại hoặc email!");
                } else {
                    editId.setError(null);
                }
                if (isEnableLoginButton()) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        }));

        changePasswordVisibility(passwordIcon, editPassword, isHidePassword);
        editPassword.addTextChangedListener(ViewUtils.afterEditTextChanged(editPassword, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPassword.getText().toString().isEmpty()) {
                    editPassword.setError("Vui lòng nhập mật khẩu!");
                } else {
                    editPassword.setError(null);
                }
                if (isEnableLoginButton()) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        }));

        passwordIcon.setOnClickListener(v -> {
            isHidePassword = !isHidePassword;
            changePasswordVisibility(passwordIcon, editPassword, isHidePassword);
        });

        loginButton.setOnClickListener(v -> {
            String id = editId.getText().toString();
            String password = editPassword.getText().toString();
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.forgotPassword.setClickable(false);
            binding.registerButton.setClickable(false);
            loginController.login(id, password).thenAccept(account -> {
                User user = (User) account;
                if (user != null) {
                    binding.errorLogin.setVisibility(View.GONE);
                    editor.putString("id", id);
                    editor.putString("password", password);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    String userID = FirebaseUtils.getAuth().getCurrentUser().getUid();
                    intent.putExtra("userId", userID);
                    startActivity(intent);
                    finish();
                } else {
                    binding.errorLogin.setVisibility(View.VISIBLE);
                }
                binding.forgotPassword.setClickable(true);
                binding.registerButton.setClickable(true);
                binding.progressBar.setVisibility(View.GONE);
            });
        });

        binding.forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
            finish();
        });

        binding.registerButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String i = sharedPreferences.getString("id", "");
        String p = sharedPreferences.getString("password", "");
        if (!i.isEmpty() || !p.isEmpty()) {
            editId.setText(i);
            editPassword.setText(p);
            loginButton.callOnClick();
        }
    }

    private boolean isEnableLoginButton() {
        return !editId.getText().toString().isEmpty() && !editPassword.getText().toString().isEmpty();
    }
    private void changePasswordVisibility(ImageView imageView, EditText editText, boolean isHidePassword) {
        if (isHidePassword) {
            imageView.setImageResource(R.drawable.hide_password_icon);
            editText.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            imageView.setImageResource(R.drawable.show_password_icon);
            editText.setTransformationMethod(null);
        }
    }
}