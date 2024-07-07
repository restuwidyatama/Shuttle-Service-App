package com.example.shuttleserviceapp.feature.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shuttleserviceapp.R
import com.example.shuttleserviceapp.database.entity.User
import com.example.shuttleserviceapp.databinding.ActivityRegisterBinding
import com.example.shuttleserviceapp.feature.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        observeViewModel()

        val roles = resources.getStringArray(R.array.roles)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRole.adapter = adapter

        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            val age = binding.editTextAge.text.toString().toIntOrNull()
            val role = binding.spinnerRole.selectedItem.toString()

            if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && age != null) {
                val newUser = User(name = name, username = username, password = password, umur = age, role = role)
                viewModel.insertUser(newUser)
            } else {
                Toast.makeText(this, "Tolong Isi Semua Field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel(){
        viewModel.insertLiveData.observe(this) {
            if (it) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}