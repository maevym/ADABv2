package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.UpdatePasswordRequest;
import com.example.adabv2.databinding.ActivityChangePasswordBinding;
import com.example.adabv2.databinding.ActivitySettingBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPasswordEdtTxt;
    EditText newPasswordEdtTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChangePasswordBinding binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView backBtn = binding.buttonBack;
        Button saveBtn = binding.buttonSave;
        oldPasswordEdtTxt = binding.oldPassword;
        newPasswordEdtTxt = binding.newPassword;

        backBtn.setOnClickListener(v -> finish());

        saveBtn.setOnClickListener(v -> {
            oldPasswordEdtTxt.setError(null);
            newPasswordEdtTxt.setError(null);
            validate();
        });
    }

    private void validate() {
        String oldPassword = oldPasswordEdtTxt.getText().toString();
        String newPassword = newPasswordEdtTxt.getText().toString();

        if (oldPassword.isEmpty()) {
            oldPasswordEdtTxt.setError("kata sandi lama tidak boleh kosong");
            oldPasswordEdtTxt.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            newPasswordEdtTxt.setError("kata sandi baru tidak boleh kosong");
            newPasswordEdtTxt.requestFocus();
            return;
        }

        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setUser_secret(userPreferences.getUserSecret());
        updatePasswordRequest.setUser_oldPassword(oldPassword);
        updatePasswordRequest.setUser_newPassword(newPassword);

        // call api
        Call<ResponseBody> updatePasswordResponseCall = ApiClient.request().updatePassword(updatePasswordRequest);
        updatePasswordResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ChangePasswordActivity.this,"Data tersimpan", Toast.LENGTH_SHORT).show();

                    oldPasswordEdtTxt.setText("");
                    newPasswordEdtTxt.setText("");

                } else {
                    if (response.code() == 401) {
                        oldPasswordEdtTxt.setError("kata sandi lama tidak sesuai");
                        oldPasswordEdtTxt.requestFocus();
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this, "Gagal mengubah data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(ChangePasswordActivity.this,"Gagal mengubah data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}