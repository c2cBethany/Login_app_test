package com.example.login2.ui.forum

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.login2.DiscussionsAdaptor
import com.example.login2.databinding.FragmentForumBinding
import com.google.firebase.firestore.FirebaseFirestore

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var mList = mutableListOf<String>()
    private lateinit var adapter: DiscussionsAdaptor

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val forumViewModel =
//            ViewModelProvider(this).get(ForumViewModel::class.java)

        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textForum
//        forumViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        initVars()
        getText()

        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getText() {
        binding.progressBar.visibility = View.VISIBLE
        firebaseFirestore.collection("forum posts")
            .get().addOnSuccessListener {
                for (i in it) {
                    val dataForum = i.data["text post"].toString()
                    val dataDate = i.data["date"].toString()
                    mList.add(dataForum + dataDate)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DiscussionsAdaptor(mList)
        binding.recyclerView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mList.clear()
        _binding = null
    }
}