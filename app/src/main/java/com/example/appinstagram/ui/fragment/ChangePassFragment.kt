package com.example.appinstagram.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.appinstagram.databinding.FragmentChangePassBinding
import com.example.appinstagram.model.ChangePassRequest
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.MainViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChangePassFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding : FragmentChangePassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChangePassBinding.inflate(inflater, container, false)
        binding.icBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
        binding.icDone.setOnClickListener {
//            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()
            val sharePref = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val old_password = sharePref.getString("password", "")
            val _id = sharePref.getString("_id", "")
            Log.d("EditProfileActivity", "Old password: $_id")
            if (binding.etOldPass.text.toString() == old_password && binding.etOldPass.text.toString().isNotEmpty()) {
                if (binding.etNewPass.text.toString().isNotEmpty()) {
                    if (binding.etNewPass.text.toString() == binding.etNewPass2.text.toString()) {
                        val request = ChangePassRequest(
                            binding.etOldPass.text.toString().toRequestBody(),
                            binding.etNewPass.text.toString().toRequestBody(),
                            _id?.toRequestBody()!!
                        )
                        viewModel.changePass(request)
                        Log.d("EditProfileActivity", "Request: $request")
                        viewModel.pass.observe(viewLifecycleOwner) {
                            if (it.data?.statusI == true) {
                                Log.d("EditProfileActivity", "Status: ${it.status}")
                                when (it.status) {
                                    DataStatus.Status.LOADING -> {
                                        Log.d("EditProfileActivity", "Loading...")
                                    }
                                    DataStatus.Status.SUCCESS -> {
                                        if (it.data?.data != null) {
                                            Log.d("EditProfileActivity", "Status: ${it.status}")
                                            Toast.makeText(context, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                                            Log.d("EditProfileActivity", "Success: ${it.data}")
                                            sharePref.edit().putString("password", binding.etNewPass.text.toString()).apply()
                                            val fragmentManager = requireActivity().supportFragmentManager
                                            fragmentManager.popBackStack()
                                        } else {
                                            Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    DataStatus.Status.ERROR -> {
                                        Log.d("EditProfileActivity", "Error: ${it.message}")
                                        Toast.makeText(context, "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Mật khẩu mới không khớp !", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Vui lòng nhập lại mật khẩu mới !", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Vui lòng nhập lại mật khẩu !", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())
}