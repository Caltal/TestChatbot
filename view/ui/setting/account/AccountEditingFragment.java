package com.example.asg02.view.ui.setting.account;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.asg02.R;
import com.example.asg02.controller.account.DeleteAccountController;
import com.example.asg02.controller.GetProvinceController;
import com.example.asg02.controller.account.UpdateAccountController;
import com.example.asg02.databinding.FragmentAccountEditingBinding;
import com.example.asg02.model.User;
import com.example.asg02.utils.ImageUtils;
import com.example.asg02.utils.ViewUtils;
import com.example.asg02.view.LoginActivity;
import com.example.asg02.view.SettingsActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountEditingFragment extends Fragment {
    private FragmentAccountEditingBinding binding;
    private boolean change = false;
    private User user;
    private UpdateAccountController updateAccountController;
    private DeleteAccountController deleteAccountController;
    private EditText editName;
    private EditText editPhone;
    private EditText editEmail;
    private EditText editPassword;
    private AutoCompleteTextView editBirthDate;
    private AutoCompleteTextView editSex;
    private AutoCompleteTextView editRegion;
    private AutoCompleteTextView editFavorite;
    private Button changeAvatar;
    private ImageView avatar;
    private String avtUri;
    private boolean isHidePassword = true;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountEditingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // assign
        editName = binding.editName;
        editPhone = binding.editPhone;
        editEmail = binding.editEmail;
        editPassword = binding.editPassword;
        ImageView passwordIcon = binding.hidePasswordButton;
        editBirthDate = binding.editBirthDate;
        editSex = binding.editSex;
        editRegion = binding.editRegion;
        editFavorite = binding.editFavorite;
        changeAvatar = binding.changeAvt;
        avatar = binding.avatar;

        updateAccountController = new UpdateAccountController();
        deleteAccountController = new DeleteAccountController();

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        if (user != null) {
            editName.setText(user.getName());
            editEmail.setText(user.getEmail());
            editPhone.setText(user.getPhone());
            editPassword.setText(user.getPassword());
            editBirthDate.setText(user.getBirthDate());
            editSex.setText(user.getSex());
            editRegion.setText(user.getRegion());
            avtUri = user.getAvatar();
            if (avtUri != null) {
                binding.avatar.setImageBitmap(ImageUtils.cropToCircleWithBorder(ImageUtils.decodeBitmap(avtUri), 20, Color.parseColor("#59351A")));
            } else {
                binding.avatar.setImageResource(R.drawable.choosing_avatar);
            }



            //-----------add eventListener--------------
            editName.addTextChangedListener(ViewUtils.afterEditTextChanged(editName, v -> {
                if (!user.getName().equals(editName.getText().toString())) {
                    change = true;
                }
            }));

            editPassword.addTextChangedListener(ViewUtils.afterEditTextChanged(editPassword, v -> {
                if (editPassword.getText().toString().length() < 8) {
                    editPassword.setError("Mật khẩu cần dai ít nhất 8 ký tự");
                    return;
                }

                if (!editPassword.getText().toString().matches(".*\\d.*")) {
                    editPassword.setError("Mật khẩu cần chứa ít nhất 1 số");
                    return;
                }

                if (!editPassword.getText().toString().matches(".*[a-zA-Z].*")) {
                    editPassword.setError("Mật khẩu cần chứa ít nhất 1 chữ cái");
                    return;
                }

                editPassword.setError(null);

                if (!user.getPassword().equals(editPassword.getText().toString())) {
                    change = true;
                }
            }));

            editBirthDate.setOnClickListener(v -> {
                String date = user.getBirthDate();
                String[] dateArr = date.split("/");
                int[] iDate = new int[3];
                for (int i = 0; i < 3; i++) {
                    iDate[i] = Integer.parseInt(dateArr[i]);
                }
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                editBirthDate.setText(String.format("%02d/%02d/%04d", i2, i1 + 1, i));
                            }
                        }, iDate[2], iDate[1] - 1, iDate[0]).show();
            });
            editBirthDate.addTextChangedListener(ViewUtils.afterEditTextChanged(editBirthDate, v -> {
                if (!editBirthDate.getText().toString().isEmpty()) {
                    editBirthDate.setError(null);
                }

                if (!user.getBirthDate().equals(editBirthDate.getText().toString())) {
                    change = true;
                }
            }));

            editSex.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Nam", "Nữ", "Khác"}));
            editSex.addTextChangedListener(ViewUtils.afterEditTextChanged(editSex, v -> {
                if (!editSex.getText().toString().isEmpty()) {
                    editSex.setError(null);
                }
                if (!user.getSex().equals(editSex.getText().toString())) {
                    change = true;
                }
            }));

            editRegion.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item
                    , new GetProvinceController().getAllProvinces()));
            editRegion.addTextChangedListener(ViewUtils.afterEditTextChanged(editRegion, v -> {
                if (!user.getRegion().equals(editRegion.getText().toString())) {
                    change = true;
                }
            }));

            editFavorite.setOnClickListener(v -> {
                // do nothing

            });
            editFavorite.addTextChangedListener(ViewUtils.afterEditTextChanged(editFavorite, v -> {
                if (!user.getFavouriteCinema().equals(editFavorite.getText().toString())) {
                    change = true;
                }
            }));

            changeAvatar.setOnClickListener(v -> {
                selectImage();
            });

            changePasswordVisibility(passwordIcon, editPassword, isHidePassword);
            passwordIcon.setOnClickListener(v -> {
                isHidePassword = !isHidePassword;
                changePasswordVisibility(passwordIcon, editPassword, isHidePassword);
            });

            binding.save.setOnClickListener(v -> {
                if (change) {
                    if (isFilledEditTexts()) {
                        String message = "Bạn có muốn lưu thông tin đã thay đổi không?";
                        ViewUtils.createAskingDialog(message, getContext(), v1 -> {
                            // save change
                            user.setName(binding.editName.getText().toString());
                            user.setPassword(binding.editPassword.getText().toString());
                            user.setBirthDate(binding.editBirthDate.getText().toString());
                            user.setSex(binding.editSex.getText().toString());
                            user.setRegion(binding.editRegion.getText().toString());
                            user.setAvatar(avtUri);
                            change = false;
                            updateAccountController.updateCurrentAccount(user);
                        }).show();
                    }
                }
            });

            binding.deleteAccount.setOnClickListener(v -> {
                String message = "Bạn có chắc chắn muốn xóa tài khoản không?";
                ViewUtils.createAskingDialog(message, getContext(), v1 -> {
                    deleteAccountController.deleteAccount();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }).show();
            });
        }


        binding.scrollView.setOnTouchListener((v, e) -> {
            ((SettingsActivity) getActivity()).hideKeyboard();
            return true;
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static final String ERROR_MESSAGE = "Mục này không được để trống";
    private boolean isFilledEditTexts() {
        List<EditText> editTexts = Arrays.asList(editName, editPhone, editEmail, editPassword);
        List<EditText> notFilledEditTexts = new ArrayList<>();

        for (EditText editText : editTexts) {
            if (editText == null) {
                return false;
            }
            if (editText.getText().toString().isEmpty()) {
                notFilledEditTexts.add(editText);
                editText.setError(ERROR_MESSAGE);
            }
        }

        List<AutoCompleteTextView> autoCompleteTextViews = Arrays.asList(editBirthDate, editSex, editRegion);
        List<AutoCompleteTextView> notFilledAutoCompleteTextViews = new ArrayList<>();
        for (AutoCompleteTextView a : autoCompleteTextViews) {
            if (a == null) {
                return false;
            }
            if (a.getText().toString().isEmpty()) {
                notFilledAutoCompleteTextViews.add(a);
                a.setError(ERROR_MESSAGE);
            }
        }

        return notFilledEditTexts.isEmpty() && notFilledAutoCompleteTextViews.isEmpty();
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

    private static final int PICK_IMAGE = 1;
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap avatarBitmap = ImageUtils.cropToCircleWithBorder(bitmap, 40, Color.parseColor("#59351A"));
                avatar.setImageBitmap(avatarBitmap);
                avtUri = ImageUtils.encodeImage(bitmap);
                user.setAvatar(avtUri);
                change = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}