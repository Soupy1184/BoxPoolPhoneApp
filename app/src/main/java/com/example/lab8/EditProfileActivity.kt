package com.example.lab8

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var etUsername: EditText? = null
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null

    private var btnSubmitChanges: Button? = null
    private var mProgressBar: ProgressDialog? = null

    private val TAG = "SignUpActivity"
    //global variables
    private var username: String? = null
    private var firstName: String? = null
    private var lastName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        initialise()
    }

    private fun initialise() {
        etUsername = findViewById<View>(R.id.et_username) as EditText
        etFirstName = findViewById<View>(R.id.et_firstName) as EditText
        etLastName = findViewById<View>(R.id.et_lastName) as EditText
        btnSubmitChanges = findViewById<View>(R.id.btn_submitChanges) as Button
        mProgressBar = ProgressDialog(this)
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        btnSubmitChanges!!.setOnClickListener { submitChanges() }
    }

    private fun submitChanges() {
        username = etUsername?.text.toString()
        firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()

        val userId = mAuth!!.currentUser!!.uid
        val currentUserDb = mDatabaseReference!!.child(userId)

        //update user profile information
        if (!TextUtils.isEmpty(username)) {
            currentUserDb.child("username").setValue(username)
        }

        if (!TextUtils.isEmpty(firstName)) {
            currentUserDb.child("firstName").setValue(firstName)
        }

        if (!TextUtils.isEmpty(lastName)) {
            currentUserDb.child("lastName").setValue(lastName)
        }
        updateUserInfoAndUI()
    }

    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}