package com.example.login2.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login2.ImagesAdapter
import com.example.login2.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Collections

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var mList = mutableListOf<String>()
    private lateinit var adapter: ImagesAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initVars()
        getImages()

        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImages(){
        binding.progressBar.visibility = View.VISIBLE
        firebaseFirestore.collection("images")
            .get().addOnSuccessListener {
                for(i in it){
                    mList.add((i.data["pic"].toString()) + (i.data["caption"].toString()))
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        layoutManager.reverseLayout = true
//        layoutManager.stackFromEnd = true
        adapter = ImagesAdapter(mList)
        binding.recyclerView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mList.clear()
    }
}