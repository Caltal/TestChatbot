package com.example.asg02.view.ui.recovery.auth;

import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.asg02.R;
import com.example.asg02.controller.account.AuthenticaionController;
import com.example.asg02.databinding.FragmentAuthenticationBinding;

public class AuthenticationFragment extends Fragment {
    private FragmentAuthenticationBinding binding;
    private CountDownTimer countDownTimer;
    TextView countdown;
    private static final String ARG_PARAM = "id";
    private String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        id = getArguments().getString(ARG_PARAM);

        binding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // assign
        countdown = binding.countdown;

        binding.noti.setText("Chúng tôi sẽ gửi tin nhắn đến email của bạn:\n" + id + ".\nHãy kiểm tra email để đặt lại mật khẩu.");

//        sendcode()
        setCountdown(60000);
        countdown.setOnClickListener(v -> {
            if (!countdown.getText().toString().contains("Gửi lại trong")) {
                new AuthenticaionController().sendVerificationCode(id);
                setCountdown(60000);
            }
        });

        binding.resetPasswordButton.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_recovery);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            controller.navigate(R.id.action_nav_auth_to_nav_reset, bundle);
        });
        return root;
    }

    //countdown time
    private void setCountdown(long duration) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                countdown.setText("Gửi lại trong " + millisUntilFinished / 1000 + "s");
                countdown.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }

            public void onFinish() {
                SpannableString spannableString = new SpannableString("Gửi lại mã");
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                countdown.setText(spannableString);
                countdown.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_autorenew_24, 0);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}