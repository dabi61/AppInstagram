package com.example.appinstagram.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import com.example.appinstagram.databinding.FragmentGenderDetailBinding

private const val GENDER = "gender"

class GenderDetailFragment : Fragment() {
    private var gender: String? = null
    private lateinit var binding : FragmentGenderDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gender = it.getString(GENDER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenderDetailBinding.inflate(inflater, container, false)
        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        when (gender) {
            "Nam" -> {
                binding.rdMale.isChecked = true
            }
            "Nữ" -> {
                binding.rdFemale.isChecked = true
                }
            "Khác" -> {
                binding.rdOther.isChecked = true
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rdMale.id -> {
                    gender = "Nam"
                }
                binding.rdFemale.id -> {
                    gender = "Nữ"
                }
                binding.rdOther.id -> {
                    gender = "Khác"
                }
                else -> {
                    gender = "Khác"
                }
            }
        }
        binding.icDone.setOnClickListener{
            Toast.makeText(context, "Chỉnh giới tính thành công! ${gender}", Toast.LENGTH_SHORT).show()
            val result = Bundle().apply {
                putString("gender", gender)
            }
            setFragmentResult("gender", result)
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(gender: String) =
            GenderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(GENDER, gender)
                }
            }
    }
}