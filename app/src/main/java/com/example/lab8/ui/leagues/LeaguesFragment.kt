package com.example.lab8.ui.leagues

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab8.model.League
import com.example.lab8.NewLeagueActivity
import com.example.lab8.R
import com.example.lab8.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.example.lab8.ui.Router
import com.example.lab8.ui.profile.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_leagues.*

class LeaguesFragment : Fragment() {

    private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

    private val router by lazy { Router() }
    private val feedAdapter by lazy { LeaguesAdapter() }

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onStart() {
        super.onStart()
        listenForLeaguesUpdates()
    }

    override fun onStop() {
        super.onStop()
        realtimeDatabaseManager.removeLeaguesValuesChangesListener()
    }

    private fun initialize() {
        feedAdapter.onLeagueItemClick().observe(this, Observer(::onLeagueItemClick))
    }

    private fun listenForLeaguesUpdates() {
        realtimeDatabaseManager.onLeaguesValuesChange().observe(this, Observer(::onLeaguesUpdate))
    }

    private fun onLeaguesUpdate(leagues: List<League>){
        feedAdapter.onFeedUpdate(leagues)
    }

    private fun onLeagueItemClick(league: League) {

        router.startLeagueDetailsActivity(requireActivity(), league)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        return inflater.inflate(R.layout.fragment_leagues, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_new_league.setOnClickListener {
            val intent = Intent(context, NewLeagueActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leaguesFeed.layoutManager = LinearLayoutManager(context)
        leaguesFeed.setHasFixedSize(true)
        leaguesFeed.adapter = feedAdapter

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        leaguesFeed.addItemDecoration(divider)
    }
}

