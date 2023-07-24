package com.example.login2.ui.newPost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is post Fragment"
    }
    val text: LiveData<String> = _text
}