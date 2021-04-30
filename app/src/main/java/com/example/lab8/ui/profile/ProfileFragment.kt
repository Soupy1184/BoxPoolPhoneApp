package com.example.lab8.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders.of
import com.example.lab8.EditProfileActivity
import com.example.lab8.R
import com.example.lab8.firebase.authentication.AuthenticationManager
import com.example.lab8.ui.Router
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception


class ProfileFragment: Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private val router by lazy { Router() }
    private val authenticationManager by lazy { AuthenticationManager() }

    //database elements
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    //UI elements
    private var tvUsername: TextView? = null
    private var tvFirstName: TextView? = null
    private var tvLastName: TextView? = null
    private var tvEmail: TextView? = null
    private var btnEditProfile: Button? = null
    private var btnLogout: Button? = null
//    private var imgBtnProfile: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        tvUsername = root.findViewById<View>(R.id.tv_user_name) as TextView
        tvFirstName = root.findViewById<View>(R.id.tv_first_name) as TextView
        tvLastName = root.findViewById<View>(R.id.tv_last_name) as TextView
        tvEmail = root.findViewById<View>(R.id.tv_email) as TextView
        btnEditProfile = root.findViewById<View>(R.id.edit_profile) as Button
        btnLogout = root.findViewById<View>(R.id.logout) as Button
//        imgBtnProfile = root.findViewById<View>(R.id.ib_profile_pic) as ImageView

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = of(this).get(ProfileViewModel::class.java)

        edit_profile.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            authenticationManager.signOut(this.requireContext())
            router.startLoginScreen(this.requireActivity())
        }
    }

    override fun onStart() {
        super.onStart()




        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        tvEmail!!.text = mUser.email

        val userId = mAuth!!.currentUser!!.uid
        val currentUserDb = mDatabaseReference!!.child(userId)
        val acct = GoogleSignIn.getLastSignedInAccount(activity)



        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild("userId")){
                    currentUserDb.child("userId").setValue(userId)
                }

                if (snapshot.hasChild("username")){
                    tvUsername?.text = snapshot.child("username").value as String
                }
                else if (acct != null){
                    tvUsername?.text = acct.displayName
                    currentUserDb.child("username").setValue(acct.displayName)
                }
                else{
                    currentUserDb.child("username").setValue("")
                }

                if (snapshot.hasChild("firstName")){
                    tvFirstName?.text = snapshot.child("firstName").value as String
                }
                else if (acct != null){
                    tvFirstName?.text = acct.givenName
                    currentUserDb.child("firstName").setValue(acct.givenName)
                }
                else{
                    currentUserDb.child("firstName").setValue("")
                }

                if (snapshot.hasChild("lastName")){
                    tvLastName?.text = snapshot.child("lastName").value as String
                }
                else if (acct != null){
                    tvLastName?.text = acct.familyName
                    currentUserDb.child("lastName").setValue(acct.familyName)
                }
                else{
                    currentUserDb.child("lastName").setValue("")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}


/*

profileViewModel.text.observe(this, Observer {
            textView.text = it
        })*/

//val textView: TextView = root.findViewById(R.id.tv_user_name)
//val textView: TextView? = root?.findViewById(R.id.tv_user_name)
//tvTeamName = root.findViewById<View>(R.id.tv_team_name) as TextView
//tvTeamName!!.text = snapshot.child("teams").child("teamName").value as String
//tvEmailVerified = root.findViewById<View>(R.id.tv_email_verifiied) as TextView


//private var tvTeamName: TextView? = null
//private var tvEmailVerified: TextView? = null
//tvEmailVerified!!.text = mUser.isEmailVerified.toString()
