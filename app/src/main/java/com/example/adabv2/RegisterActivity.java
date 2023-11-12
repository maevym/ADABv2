package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.DataUser;
import com.example.adabv2.databinding.ActivityRegisterBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityRegisterBinding binding;
    String[] roleUniversity = {"Select Role","Mahasiswa", "Dosen"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.spinnerRole.setOnItemSelectedListener(this);
        ArrayAdapter adapterRole = new ArrayAdapter(this, android.R.layout.simple_spinner_item, roleUniversity)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };

        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRole.setAdapter(adapterRole);

        binding.buttonLoginInRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        binding.buttonRegister.setOnClickListener(v -> {
            validate();
        });

    }

    private void validate() {
        String usernameText = binding.usernameRegister.getText().toString();
        String passwordText = binding.passwordRegister.getText().toString();
        String emailText = binding.emailRegister.getText().toString();
        String nimText = binding.nimRegister.getText().toString();
        String selectedValue = binding.spinnerRole.getSelectedItem().toString();
        String defaultRole = "Select Role";
        View selectedView = binding.spinnerRole.getSelectedView();
        String mahasiwsaValue = "Mahasiswa";
        String dosenValue = "Dosen";
        String role = "";


        if(usernameText.isEmpty()){
            binding.usernameRegister.setError("nama tidak boleh kosong");
            binding.usernameRegister.requestFocus();
            return;
        }

        if(usernameText.length() > 20){
            binding.usernameRegister.setError("username tidak boleh lebih dari 20 karakter");
            binding.usernameRegister.requestFocus();
            return;
        }

        if(emailText.isEmpty()){
            binding.emailRegister.setError("email tidak boleh kosong");
            binding.emailRegister.requestFocus();
            return;
        }

        if(!emailText.endsWith(".com")){
            binding.emailRegister.setError("email harus mengandung com");
            binding.emailRegister.requestFocus();
            return;
        }

        if(!emailText.contains("@")){
            binding.emailRegister.setError("email harus mengandung @");
            binding.emailRegister.requestFocus();
            return;
        }

        if(nimText.isEmpty()){
            binding.nimRegister.setError("nim tidak boleh kosong");
            binding.nimRegister.requestFocus();
            return;
        }

        if (nimText.length() > 10 || nimText.length() < 10){
            binding.nimRegister.setError("nim harus mengandung 10 karakter");
            binding.nimRegister.requestFocus();
            return;
        }

        if (selectedValue.equals(defaultRole)){
            binding.spinnerRole.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error");
            selectedTextView.setTextColor(Color.RED);
            return;
        }

        if(passwordText.isEmpty()){
            binding.passwordRegister.setError("password tidak boleh kosong");
            binding.passwordRegister.requestFocus();
            return;
        }

        if (selectedValue.equals(mahasiwsaValue)){
            role = "M";
            Log.wtf("masuk mahasiswa",role);
        }

        else if (selectedValue.equals(dosenValue)){
            role = "D";
            Log.wtf("masuk dosen",role);
        }

        DataUser dataUser = new DataUser();
        dataUser.setEmail(emailText);
        Log.wtf("masuk","berhasil data email" );
        dataUser.setPassword(passwordText);
        Log.wtf("masuk","berhasil data pass" );
        dataUser.setName(usernameText);
        Log.wtf("masuk","berhasil data name" );
        dataUser.setUser_nim(nimText);
        Log.wtf("masuk","berhasil data nim" );
        dataUser.setUser_type(role);
        Log.wtf("masuk","berhasil data role ");
        Call<ResponseBody> dataUserCall = ApiClient.request().saveUser(dataUser);
        dataUserCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    Toast.makeText(RegisterActivity.this,"Daftar akun berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    if(response.code() == 401){
                        Toast.makeText(RegisterActivity.this, "Email sudah didaftarkan sebelumnya", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Gagal mendaftarkan", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//        String selectedRole = (String) parent.getSelectedItem();
//        if(position > 0){
//            Toast.makeText(getApplicationContext(), "Selected : " + selectedRole, Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(RegisterActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
    }
}