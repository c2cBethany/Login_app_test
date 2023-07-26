package com.example.login2.ui.forum

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.login2.DiscussionsAdaptor
import com.example.login2.databinding.FragmentForumBinding
import com.example.login2.datasource.DataClassForum
import com.example.login2.datasource.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    var databaseReferencePosts: DatabaseReference? = null
    var eventListener: ValueEventListener? = null
    private lateinit var dataList: ArrayList<DataClassForum>
    private lateinit var adapter: DiscussionsAdaptor
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.layoutManager = gridLayoutManager

        binding.progressBar.visibility = View.VISIBLE
        dataList = ArrayList()
        adapter = DiscussionsAdaptor(requireContext(), dataList)
        binding.recyclerView.adapter = adapter
        databaseReferencePosts = FirebaseDatabase.getInstance().getReference("ForumPosts")
        binding.progressBar.visibility = View.GONE

        eventListener = databaseReferencePosts!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClassForum::class.java)
                    if (dataClass != null) {
                        dataList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(), "Database error", Toast.LENGTH_SHORT
                ).show()
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dataList.clear()
    }
}