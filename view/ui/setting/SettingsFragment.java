package com.example.asg02.view.ui.setting;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.asg02.R;
import com.example.asg02.databinding.FragmentSettingsBinding;
import com.example.asg02.model.User;
import com.example.asg02.view.chat.ChatActivity;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private User user;
    private String userId;
//    private NavController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        userId = getActivity().getIntent().getStringExtra("userId");

        binding.name.setText(user.getName());
        binding.moveToAccountSettings.setOnClickListener(v -> {
            // move to account settings
            navigate(R.id.action_nav_settings_to_nav_account);
        });

        binding.chat.setOnClickListener(v -> {
            // move to chat
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        binding.version.setOnClickListener(v -> {
            Snackbar.make(binding.version, "Version 1.0", Snackbar.LENGTH_LONG).show();
        });

        binding.tos.setOnClickListener(v -> {
            navigate(R.id.action_nav_settings_to_nav_tos);
        });

        binding.privacyPolicy.setOnClickListener(v -> {
            navigate(R.id.action_nav_settings_to_nav_privacy_policy);
        });

        return root;
    }

    private void navigate(int id) {
        NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_settings);
        controller.navigate(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}