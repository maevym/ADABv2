package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.databinding.ActivityRegisterBinding;

import java.lang.reflect.Method;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityRegisterBinding binding;
    DataUser dataUser;
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


        if(usernameText.isEmpty()){
            binding.usernameRegister.setError("username cannot be empty");
            binding.usernameRegister.requestFocus();
            return;
        }

        if(usernameText.length() > 20){
            binding.usernameRegister.setError("username invalid");
            binding.usernameRegister.requestFocus();
            return;
        }

        if(emailText.isEmpty()){
            binding.emailRegister.setError("email cannot be empty");
            binding.emailRegister.requestFocus();
            return;
        }

        if(!emailText.endsWith(".com")){
            binding.emailRegister.setError("email invalid");
            binding.emailRegister.requestFocus();
            return;
        }

        if(nimText.isEmpty()){
            binding.nimRegister.setError("email cannot be empty");
            binding.nimRegister.requestFocus();
            return;
        }

        if (selectedValue.equals(defaultRole)){
            binding.spinnerRole.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
//            Toast.makeText(RegisterActivity.this,"error role", Toast.LENGTH_SHORT).show();
            return;
        }

        if(passwordText.isEmpty()){
            binding.passwordRegister.setError("password cannot be empty");
            binding.passwordRegister.requestFocus();
            return;
        }

        else {
            createRegister(dataUser);

        }


    }

    public void createRegister(DataUser dataUser){
        Call<ResponseBody> callRegister = ApiClient.request().createRegister(dataUser);
        callRegister.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    dataUser.setEmail(dataUser.getEmail());
                    dataUser.setPassword(dataUser.getPassword());
                    dataUser.setName(dataUser.getName());
                    dataUser.setUser_type(dataUser.getUser_type());
                    dataUser.setUser_nim(dataUser.getUser_nim());

                }
                else{
                    if(response.code() == 401){
                        Toast.makeText(RegisterActivity.this, "Data sudah pernah diinput sebelumnya", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//        Toast.makeText(getApplicationContext(), roleUniversity[position], Toast.LENGTH_LONG).show();
//        Toast.makeText(parent.getContext(),
//                "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
//                Toast.LENGTH_SHORT).show();
        String selectedRole = (String) parent.getSelectedItem();
        if(position > 0){
            // Notify the selected item text
            Toast.makeText(getApplicationContext(), "Selected : " + selectedRole, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        Toast.makeText(RegisterActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
    }
}