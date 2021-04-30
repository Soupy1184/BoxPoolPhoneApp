package com.example.lab8.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.example.lab8.R
import com.example.lab8.firebase.authentication.AuthenticationManager
import com.example.lab8.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.example.lab8.model.League
import com.example.lab8.model.Team
import com.example.lab8.ui.Router
import com.example.lab8.ui.leagues.LeaguesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_leagues.*
import kotlinx.android.synthetic.main.fragment_team.*

const val LEAGUE_REF = "league_extra"

class TeamFragment: Fragment() {

    private lateinit var league: League
//
//    companion object {
//
//        fun newInstance(league: League): TeamFragment {
//            val fragment = TeamFragment()
//            val bundle = Bundle().apply {
//                putParcelable(LEAGUE_REF, league)
//            }
//            fragment.arguments = bundle
//            return fragment
//        }
//    }

    private var text: TextView? = null
    private var pickTeamBtn: Button? = null

    private val router by lazy { Router() }
    private val feedAdapter by lazy { TeamAdapter() }
    private val authenticationManager by lazy { AuthenticationManager() }
    private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

    //database elements
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_team, container, false)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        pickTeamBtn = root.findViewById<View>(R.id.pickTeamBtn) as Button
        text = root.findViewById<View>(R.id.tv_team) as TextView
        league = arguments?.getParcelable(LEAGUE_REF)!!

        text?.text = league.leagueName

        //pickTeamBtn!!.isVisible


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamFeed.layoutManager = LinearLayoutManager(context)
        teamFeed.setHasFixedSize(true)
        teamFeed.adapter = feedAdapter

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        teamFeed.addItemDecoration(divider)

        pickTeamBtn?.setOnClickListener {
            router.startNewTeamActivity(requireActivity(), league)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        listenForTeamsUpdates()
    }

    override fun onStop() {
        super.onStop()
        realtimeDatabaseManager.removeTeamsValuesChangesListener()
    }

    private fun initialize() {
        feedAdapter.onTeamItemClick().observe(this, Observer(::onTeamItemClick))
    }

    private fun listenForTeamsUpdates() {
        realtimeDatabaseManager.onTeamsValuesChange(league).observe(this, Observer(::onTeamsUpdate))
    }

    private fun onTeamsUpdate(team: List<Team>){
        feedAdapter.onFeedUpdate(team)
    }

    private fun onTeamItemClick(team: Team) {

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
