package com.example.appinstagram.ui.activity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appinstagram.R
import com.example.appinstagram.databinding.ActivitySignUpBinding
import com.example.appinstagram.model.SignUpRequest
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.LoginViewModel
import okhttp3.internal.wait
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.ivBack.setOnClickListener {
            finish()
        }
        var isPasswordVisible = false
        var isPasswordVisible2 = false
        binding.icEye.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.transformationMethod = null
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

        binding.icEyeConfirm.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2
            if (isPasswordVisible2) {
                binding.etConfirmPass.transformationMethod = null
            } else {
                binding.etConfirmPass.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
            binding.etConfirmPass.setSelection(binding.etConfirmPass.text.length)
        }
        binding.btCreateAccount.setOnClickListener {
            if (binding.etName.text == null || binding.etPassword.text == null || binding.etUsername.text == null || binding.etConfirmPass.text == null) {
                Toast.makeText(this, "Bạn cần nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else {
                val name = binding.etName.text.trim().toString()
                val username = binding.etUsername.text.trim().toString()
                val password = binding.etPassword.text.trim().toString()
                val confirmPass = binding.etConfirmPass.text.trim().toString()
                if (password != confirmPass) {
                    Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    val request = SignUpRequest(username, password, name)
                    viewModel.signUp(request)
                    viewModel.newProfile.observe(this) {
                        Log.d("HomeFragment", "onCreateView: ${it.data}")
                        if (it.data?.status == true) {
                            when (it.status) {
                                DataStatus.Status.LOADING -> {
                                    Log.d("HomeFragment", "onCreateView: Loading")
                                }
                                DataStatus.Status.SUCCESS -> {
                                    if (it.data != null) {
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Bạn đã nhập sai tài khoản hoặc mật khẩu",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                DataStatus.Status.ERROR -> {
                                }
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "${it.data?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}