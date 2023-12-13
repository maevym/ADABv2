package com.example.adabv2.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.LoginActivity;
import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.DataUser;
import com.example.adabv2.RegisterActivity;
import com.example.adabv2.databinding.FragmentRegisterBinding;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentRegisterBinding binding;
    String[] roleUniversity = {"Select Role","Mahasiswa", "Dosen", "LSC"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.spinnerRole.setOnItemSelectedListener(this);

        ArrayAdapter adapterRole = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roleUniversity)
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

        binding.buttonRegister.setOnClickListener(v -> {
            binding.usernameRegister.setError(null);
            binding.passwordRegister.setError(null);
            binding.emailRegister.setError(null);
            binding.idRegister.setError(null);
            validate();
        });

        return view;
    }

    private void validate() {
        String usernameText = binding.usernameRegister.getText().toString();
        String passwordText = binding.passwordRegister.getText().toString();
        String emailText = binding.emailRegister.getText().toString();
        String idText = binding.idRegister.getText().toString();
        String selectedValue = binding.spinnerRole.getSelectedItem().toString();
        String defaultRole = "Select Role";
        View selectedView = binding.spinnerRole.getSelectedView();
        String mahasiswaValue = "Mahasiswa";
        String dosenValue = "Dosen";
        String lscValue = "LSC";
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

        if(idText.isEmpty()){
            binding.idRegister.setError("id tidak boleh kosong");
            binding.idRegister.requestFocus();
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

        if (selectedValue.equals(mahasiswaValue)){
            role = "M";
            Log.wtf("masuk mahasiswa",role);

            if (idText.length() != 10){
                binding.idRegister.setError("id harus mengandung 10 karakter");
                binding.idRegister.requestFocus();
                return;
            }
        }

        else if (selectedValue.equals(dosenValue)){
            role = "D";
            Log.wtf("masuk dosen",role);
            if (idText.length() != 5){
                binding.idRegister.setError("id harus mengandung 5 karakter");
                binding.idRegister.requestFocus();
                return;
            }
        }

        else if (selectedValue.equals(lscValue)) {
            role = "L";
            Log.wtf("masuk lsc",role);
            if (idText.length() != 5){
                binding.idRegister.setError("id harus mengandung 5 karakter");
                binding.idRegister.requestFocus();
                return;
            }
        }

        DataUser dataUser = new DataUser();
        dataUser.setEmail(emailText);
        Log.wtf("masuk","berhasil data email" );
        dataUser.setPassword(passwordText);
        Log.wtf("masuk","berhasil data pass" );
        dataUser.setName(usernameText);
        Log.wtf("masuk","berhasil data name" );
        dataUser.setUser_nim(idText);
        Log.wtf("masuk","berhasil data id" );
        dataUser.setUser_type(role);
        Log.wtf("masuk","berhasil data role ");
        Call<ResponseBody> dataUserCall = ApiClient.request().saveUser(dataUser);
        dataUserCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    Toast.makeText(requireContext(),"Registrasi berhasil", Toast.LENGTH_SHORT).show();

                    binding.usernameRegister.setText("");
                    binding.passwordRegister.setText("");
                    binding.emailRegister.setText("");
                    binding.idRegister.setText("");
                    binding.spinnerRole.setSelection(0);

                } else {
                    if(response.code() == 401){
                        Toast.makeText(requireContext(), "Akun sudah diregistrasi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(requireContext(),"Registrasi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(requireContext(), "pilih peran", Toast.LENGTH_SHORT).show();
    }
}