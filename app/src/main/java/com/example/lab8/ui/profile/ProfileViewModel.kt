package com.example.lab8.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProfileViewModel : ViewModel() {
    private var firstNameText = ""
    private var lastNameText = ""
    private var userNameText = ""



    fun resetText(fnText: String, lnText: String, unText: String) {

    }

}