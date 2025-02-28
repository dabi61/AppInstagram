package com.example.appinstagram.ui.fragment

import ListLikeAdapter
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinstagram.R
import com.example.appinstagram.adapters.PicturePostAdapter
import com.example.appinstagram.databinding.FragmentAddBinding
import com.example.appinstagram.databinding.FragmentListLoveBinding
import com.example.appinstagram.model.HomeData
import com.example.appinstagram.model.User
import com.example.appinstagram.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ListLoveFragment(val post: HomeData.Post) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentListLoveBinding
    private val viewModel: MainViewModel by activityViewModel()
    private var listLoveAdapter : ListLikeAdapter ?= null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
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
        binding = FragmentListLoveBinding.inflate(inflater, container, false)
        listLoveAdapter = ListLikeAdapter(requireContext())
        setUpRecyclerView()
        listLoveAdapter?.submitList(post.listLike)
        return binding.root
    }

    private fun setUpRecyclerView() {
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = listLoveAdapter
        }
    }
}
