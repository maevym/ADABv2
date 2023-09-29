package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.adabv2.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    String[] item = {"Mahasiswa", "Dosen", "Admin", "LSC"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        autoCompleteTextView = findViewById(R.id.actionRoleApp);
        adapterItem = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(RegisterActivity.this, "Item"+ item, Toast.LENGTH_SHORT).show();
            }
        });

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

        if(passwordText.isEmpty()){
            binding.passwordRegister.setError("password cannot be empty");
            binding.passwordRegister.requestFocus();
            return;
        }

        //else

    }
}