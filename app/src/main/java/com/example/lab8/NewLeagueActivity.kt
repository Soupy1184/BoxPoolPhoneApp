package com.example.lab8

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NewLeagueActivity: AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, NewLeagueActivity::class.java)
    }

    private var mDatabaseReference: DatabaseReference? = null
    private var mUserReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var etLeagueName: EditText? = null
    private var btnCreateLeague: Button? = null
    private var leagueName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newleague)

        initialise()
    }

    private fun initialise() {
        etLeagueName = findViewById<View>(R.id.et_createLeagueName) as EditText
        btnCreateLeague = findViewById<View>(R.id.btn_registerLeague) as Button

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Leagues")
        mUserReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
        btnCreateLeague!!.setOnClickListener { createNewLeague() }
    }

    private fun createNewLeague() {
        leagueName = etLeagueName?.text.toString()

        if (!TextUtils.isEmpty(leagueName)) {
            val key = mDatabaseReference?.push()?.key
            val userId = mAuth!!.currentUser!!.uid

            val leagueRef = mDatabaseReference!!.child(key!!)
            leagueRef.child("leagueName").setValue(leagueName)
            leagueRef.child("playerCount").setValue(1)
            leagueRef.child("leagueId").setValue(key)
            //leagueRef.child("leagueMembers").child("user").setValue(userId)
            leagueRef.child("leagueMembers").child(userId).child("teamPicked").setValue(false)
            leagueRef.child("leagueMembers").child(userId).child("leagueManager").setValue(true)

            val userRef = mUserReference!!.child(userId).child("userLeagues").child(key)
            userRef.child("leagueName").setValue(leagueName)
            userRef.child("playerCount").setValue(1)
            userRef.child("leagueId").setValue(key)
            //userRef.child("leagueMembers").child("user").setValue(userId)
            userRef.child("leagueMembers").child(userId).child("teamPicked").setValue(false)
            userRef.child("leagueMembers").child(userId).child("leagueManager").setValue(true)

            updateUserInfoAndUI()
        }
        else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@NewLeagueActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}