package com.example.appinstagram.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.R
import com.example.appinstagram.adapters.ImagePostAdapter
import com.example.appinstagram.adapters.PicturePostAdapter
import com.example.appinstagram.databinding.FragmentAddBinding
import com.example.appinstagram.extension.toRequestBody
import com.example.appinstagram.model.PostRequest
import com.example.appinstagram.viewmodel.MainViewModel

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File

class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentAddBinding
    private var selectedImages : MutableList<Uri> ?= null
    private lateinit var pictureAdapter : PicturePostAdapter
    private val viewModel : MainViewModel by  activityViewModel()
    private val imageParts = arrayListOf<MultipartBody.Part>()


    private val pickMultipleMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        if (uris.isNotEmpty()) {
            selectedImages?.clear()
            Log.d("AddFragment", "$selectedImages")
            selectedImages?.addAll(uris)
            pictureAdapter.submitList(uris)  // Cập nhật RecyclerView nếu cần
            for (uri in uris) { // selectedImages là danh sách ảnh bạn chọn
                val file = File(uri.path)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("images", file.name, requestFile)
                imageParts.add(imagePart)
            }
        } else {
            binding.rvImage.visibility = View.GONE
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT // Full-screen
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentAddBinding.inflate(inflater, container, false)
        pictureAdapter = PicturePostAdapter(requireContext())
        setUpRecyclerView()
        binding.rvImage.setOnClickListener{
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.icImage.setOnClickListener{
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.icBack.setOnClickListener{
            dismiss()
        }
        binding.btShare.setOnClickListener{
            if (selectedImages.isNullOrEmpty())
            {
                val sharePref =  requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE)
                val _id = sharePref.getString("_id", "")
                val request = PostRequest(
                    _id?.toRequestBody()!!,
                    imageParts,
                    binding.etCaption.text.toString().toRequestBody()
                )
                viewModel.addPost(request)
                viewModel.addPost.observe(viewLifecycleOwner) {
                    Log.d("AddFragment", "Status: ${it}")
                }
                Toast.makeText(context, "Đăng bài thành công!", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "Bạn phải có tối thiểu 1 ảnh !", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }
    private fun setUpRecyclerView(){
        with(binding.rvImage) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = pictureAdapter
        }
    }

}