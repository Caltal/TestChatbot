package com.example.asg02.view.ui.recovery.reset;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asg02.controller.account.LoginController;
import com.example.asg02.controller.account.UpdateAccountController;
import com.example.asg02.databinding.FragmentResetPasswordBinding;
import com.example.asg02.model.User;
import com.example.asg02.R;
import com.example.asg02.utils.ViewUtils;
import com.example.asg02.view.MainActivity;

public class ResetPasswordFragment extends Fragment {
    private FragmentResetPasswordBinding binding;
    private boolean isHidePassword = true;
    private boolean isHideConfirmPassword = true;
    private EditText editPassword;
    private EditText editID;
    private String PARAM_ID = "id";
    private String id;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        id = getArguments().getString(PARAM_ID);

        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // assign

        editPassword = binding.password;
        editID = binding.id;

        editID.setText(id);
        editID.setEnabled(false);

        changePasswordVisibility(binding.password, isHideConfirmPassword);

        editPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getRawX() >= (editPassword.getRight() - editPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 32)) {
                    isHidePassword = !isHidePassword;
                    changePasswordVisibility(editPassword, isHidePassword);
                    return true;
                }
            }
            return false;
        });

        editPassword.addTextChangedListener(ViewUtils.afterEditTextChanged(editPassword, v -> {
            if (isEnableLoginButton()) {
                binding.finishResetPassword.setEnabled(true);
            } else {
                binding.finishResetPassword.setEnabled(false);
            }
        }));

        binding.finishResetPassword.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            new LoginController().login(id, editPassword.getText().toString()).thenAccept(account -> {
                User user = (User) account;
                if (user != null) {
                    binding.wrongPassword.setVisibility(View.GONE);
                    user.setPassword(editPassword.getText().toString());
                    new UpdateAccountController().updateCurrentAccount(user);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    Log.e("a", user.getPassword());
                } else {
                    binding.wrongPassword.setVisibility(View.VISIBLE);
                }
                binding.progressBar.setVisibility(View.GONE);
            });
        });

        return root;
    }

    private boolean isEnableLoginButton() {
        return !editPassword.getText().toString().isEmpty() && !editID.getText().toString().isEmpty();
    }

    private void changePasswordVisibility(EditText editText, boolean isHidePassword) {
        if (isHidePassword) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.hide_password_icon), null);
            editText.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.show_password_icon), null);
            editText.setTransformationMethod(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}