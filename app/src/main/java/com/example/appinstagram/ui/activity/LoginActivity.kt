package com.example.appinstagram.ui.activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appinstagram.databinding.ActivityLoginBinding
import com.example.appinstagram.model.LoginRequest
import com.example.appinstagram.model.Profile
import com.example.appinstagram.ui.main.MainActivity
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel



class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userBefore = getUser()
        if(userBefore[0] != null && userBefore[1] != null && userBefore[2] != null && userBefore[3] != null){
            if (userBefore[3] == "1") {
                login(userBefore[0].toString(), userBefore[1].toString())
            } else {
                binding.etUsername.setText(userBefore[0])
                binding.etPassword.setText(userBefore[1])
                binding.cbRememberMe.isChecked = true
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        var isPasswordVisible = true
        binding.icEye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.transformationMethod = null
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
        binding.btLogin.setOnClickListener {
            val username = binding.etUsername.text.trim().toString()
            val password = binding.etPassword.text.trim().toString()
            login(username, password)
        }

        }
        fun saveUser(profile: Profile, password: String) {
            val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("username", profile.username)
            editor.putString("password", password)
            editor.putString("_id", profile._id)
            editor.putString("status", 1.toString())
            editor.apply()
        }

        fun getUser(): List<String?> {
            val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", null)
            val password = sharedPreferences.getString("password", null)
            val uid = sharedPreferences.getString("_id", null)
            val status = sharedPreferences.getString("status", null)
            return listOf(username, password, uid, status)
        }
    fun login(username: String, password: String) {
        val request = LoginRequest(username, password)
        lifecycleScope.launch {
            viewModel.login(request)
            viewModel.profile.observe(this@LoginActivity) {
                Log.d("HomeFragment", "onCreateView: ${it.data}")
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        Log.d("HomeFragment", "onCreateView: Loading")
                    }
                    DataStatus.Status.SUCCESS -> {
                        if (it.data != null) {
                            val profile = it.data
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("profile", profile)
                            saveUser(profile, password)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Bạn đã nhập sai tài khoản hoặc mật khẩu",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    DataStatus.Status.ERROR -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "There is someThing wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }
}