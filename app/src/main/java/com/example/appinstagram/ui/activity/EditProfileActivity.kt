package com.example.appinstagram.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.appinstagram.R
import com.example.appinstagram.databinding.ActivityEditProfileBinding
import com.example.appinstagram.model.Profile
import com.example.appinstagram.model.UpdateProfileRequest
import com.example.appinstagram.ui.fragment.ChangePassFragment
import com.example.appinstagram.ui.fragment.GenderDetailFragment
import com.example.appinstagram.utils.DataStatus
import com.example.appinstagram.viewmodel.MainViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: MainViewModel by viewModel()
    private var selectedAvatarUri: Uri? = null
    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.ivAvatar.setImageURI(uri) // Hiển thị ảnh
                selectedAvatarUri = uri
                Log.d("EditProfileActivity", "Selected URI: $uri")
            } else {
                Toast.makeText(this, "Không chọn ảnh nào", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var profile = intent.getParcelableExtra<Profile>("profile")
        with(binding) {
            etUsername.setText(profile?.username)
            etName.setText(profile?.name)
            tvResultGender.text = checkGender(profile?.gender.toString())
            etAddress.setText(profile?.address)
            etIntroduce.setText(profile?.introduce)
        }
        Glide.with(this)
            .load(profile?.avatar)
            .error(R.drawable.ic_profile)
            .into(binding.ivAvatar)

        binding.icBack.setOnClickListener {
            finish()
        }

        binding.tvResultGender.setOnClickListener {
            val gender = checkGender(profile?.gender.toString())
            val genderDetailFragment = GenderDetailFragment.newInstance(gender)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_right,
                    R.anim.slide_out_right,
                )
                .add(R.id.fl_edit_profile, genderDetailFragment)
                .addToBackStack(null)
                .commit()
        }
        supportFragmentManager.setFragmentResultListener("gender", this) { _, result ->
            val gender = result.getString("gender")
            binding.tvResultGender.text = gender
            profile?.gender = gender
        }
        binding.icDone.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue))

        binding.tvChangePass.setOnClickListener {
            val changePassFragment = ChangePassFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_right,
                    R.anim.slide_out_right,
                )
                .add(R.id.fl_edit_profile, changePassFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.ivAvatar.setOnClickListener {
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.tvEditAvatar.setOnClickListener {
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.icDone.setOnClickListener {
            val request = profile?.let { it1 -> getProfile(it1) }
            Log.d("EditProfileActivity", "Request: $request")
            if (request != null) {
                if(request.name != null){
                    viewModel.updateProfile(request)
                    viewModel.profile.observe(this@EditProfileActivity) {
                        Log.d("EditProfileActivity", "Status: ${it.status}")
                        when (it.status) {
                            DataStatus.Status.LOADING -> {}
                            DataStatus.Status.SUCCESS -> {
                                val name = binding.etName.text.toString()
                                Log.d("EditProfileActivity", "Received name: $selectedAvatarUri")
                                val resultIntent = Intent().apply {
                                    putExtra("name", name)
                                    if (selectedAvatarUri != null) {
                                        putExtra("avatar", selectedAvatarUri.toString())
                                    } else
                                        putExtra("avatar", profile?.avatar)
                                }
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                            DataStatus.Status.ERROR -> {}
                        }
                    }
                } else{
                    Toast.makeText(this@EditProfileActivity, "Tên không được để trống!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun checkGender(gender: String): String {
        if (gender == "Nam") {
            return "Nam"
        } else if (gender == "Nữ") {
            return "Nữ"
        } else if (gender == "Khác") {
            return "Khác"
        }
        return "Chọn giới tính của bạn"
    }

    fun getProfile(profile: Profile): UpdateProfileRequest {
        profile.username = binding.etUsername.text.toString()
        profile.name = binding.etName.text.toString()
        profile.address = binding.etAddress.text.toString()
        profile.introduce = binding.etIntroduce.text.toString()

        val sharePref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val _id = sharePref.getString("_id", "")

        val avatar = prepareImageFileAsJpg(this, selectedAvatarUri)  // Lấy ảnh dạng Multipart

        return UpdateProfileRequest(
            profile.name.toRequestBody(),
            avatar?.let { getImageMultipart(it) },
            profile.gender?.toRequestBody(),
            profile.address?.toRequestBody(),
            profile.introduce?.toRequestBody(),
           _id?.toRequestBody()!!
        )
    }
    fun prepareImageFileAsJpg(context: Context, imageUri: Uri?): File? {
        imageUri ?: return null

        return try {
            val contentResolver = context.contentResolver
            val fileName = "photo_${System.currentTimeMillis()}.jpg" // Luôn lưu dưới dạng JPG

            val file = File(context.cacheDir, fileName)

            contentResolver.openInputStream(imageUri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return null

            Log.d("prepareImageFileAsJpg", "File path: ${file.absolutePath}, MIME: image/jpeg")
            file
        } catch (e: Exception) {
            Log.e("prepareImageFileAsJpg", "Lỗi khi tạo file: ${e.message}")
            null
        }
    }
    fun getImageMultipart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("avatar", file.name, requestFile)
    }
    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

}
