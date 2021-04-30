package com.example.lab8.ui.leaguehome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders.of
import com.example.lab8.R
import com.example.lab8.firebase.authentication.AuthenticationManager
import com.example.lab8.model.League
import com.example.lab8.ui.Router
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LeagueHomeFragment: Fragment() {

    private var league: League? = null

//    companion object {
//        const val LEAGUE_REF = "league_extra"
//        fun newInstance(league: League): LeagueHomeFragment {
//            val fragment = LeagueHomeFragment()
//            val bundle = Bundle().apply {
//                putParcelable(LEAGUE_REF, league)
//            }
//            fragment.arguments = bundle
//            return fragment
//        }
//    }

    private lateinit var viewModel: LeagueHomeViewModel
    private val router by lazy { Router() }
    private val authenticationManager by lazy { AuthenticationManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            league = it.getParcelable("league_extra")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_leaguehome, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = of(this).get(LeagueHomeViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        //league = requireArguments().getParcelable(LEAGUE_REF)!!
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
