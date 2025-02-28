package com.example.appinstagram.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.R
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
    private lateinit var binding: FragmentAddBinding
    private var selectedImages: MutableList<File> = mutableListOf()
    private lateinit var pictureAdapter: PicturePostAdapter
    private val viewModel: MainViewModel by activityViewModel()
    private val imageParts = arrayListOf<MultipartBody.Part>()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
//        setFragmentResult("bottom_sheet_dismiss", bundleOf("reload" to true))
    }

    private val pickMultipleMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        if (uris.isNotEmpty()) {
            binding.rvImage.visibility = View.VISIBLE
            val newFiles = uris.mapNotNull { getFileFromUri(requireContext(), it) }

            // Đảm bảo ảnh đã chọn trước đó vẫn được hiển thị
            val updatedFiles = (selectedImages + newFiles).distinctBy { it.absolutePath }
            selectedImages.clear()
            selectedImages.addAll(updatedFiles)

            Log.d("PhotoPicker", "Chọn được ${selectedImages.size} ảnh")

            pictureAdapter.submitList(updatedFiles.map { Uri.fromFile(it) }) // Cập nhật RecyclerView
            updateImageParts()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.layoutParams?.height =
            ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentAddBinding.inflate(inflater, container, false)

        pictureAdapter = PicturePostAdapter(requireContext())
        setUpRecyclerView()

        binding.icImage.setOnClickListener { pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        binding.icBack.setOnClickListener { dismiss() }

        binding.btShare.setOnClickListener {
            if (imageParts.isNotEmpty()) {
                val sharePref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val userId = sharePref.getString("_id", "") ?: ""
                val username = sharePref.getString("username", "") ?: ""

                val request = PostRequest(
                    userId.toRequestBody(),
                    imageParts,
                    binding.etCaption.text.toString().toRequestBody()
                )

                viewModel.addPost(request)
                viewModel.addPost.observe(viewLifecycleOwner) {
                    if (it.data?.data != null) {
                        Toast.makeText(context, "Đăng thành công!", Toast.LENGTH_SHORT).show()
                        viewModel.getAllPosts()
                        viewModel.getMyPost(username)
                        dismiss()
                        setFragmentResult("bottom_sheet_upload", bundleOf("reload" to true))
                    } else {
                        Toast.makeText(context, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(context, "Bạn phải chọn ít nhất 1 ảnh!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setUpRecyclerView() {
        with(binding.rvImage) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = pictureAdapter
        }
    }

    private fun updateImageParts() {
        imageParts.clear()
        selectedImages.forEach { file ->
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("images", file.name, requestFile)
            imageParts.add(imagePart)
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: "unknown"

            if (!mimeType.startsWith("image/")) {
                Log.e("getFileFromUri", "Không đúng định dạng tệp! MIME: $mimeType")
                return null
            }

            val fileName = "photo_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)

            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return null

            Log.d("getFileFromUri", "Lưu ảnh thành công: ${file.absolutePath}")
            file
        } catch (e: Exception) {
            Log.e("getFileFromUri", "Lỗi khi lấy file từ Uri: ${e.message}")
            null
        }
    }
}
