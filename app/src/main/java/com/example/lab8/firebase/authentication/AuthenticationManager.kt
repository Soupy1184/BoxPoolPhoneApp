package com.example.lab8.firebase.authentication

import android.app.Activity
import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

const val RC_SIGN_IN = 1000

class AuthenticationManager {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    fun startSignInFlow(activity: Activity) {
        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )
    }

    fun isUserSignedIn() = firebaseAuth.currentUser != null

    fun getCurrentUser() = firebaseAuth.currentUser

    fun signOut(context: Context) {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }
}