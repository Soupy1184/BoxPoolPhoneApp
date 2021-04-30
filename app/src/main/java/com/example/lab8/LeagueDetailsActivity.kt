package com.example.lab8

import android.content.Context
import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lab8.firebase.authentication.AuthenticationManager
import com.example.lab8.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.example.lab8.model.League
import com.example.lab8.ui.leaguehome.LeagueHomeFragment
import com.example.lab8.ui.team.TeamFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_leaguedetails.*

class LeagueDetailsActivity : AppCompatActivity() {

    private lateinit var league: League
    //private val commentsAdapter by lazy { CommentsAdapter(DateUtils()) }
    private val authenticationManager by lazy { AuthenticationManager() }
    private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }
    private val leagueHome by lazy { LeagueHomeFragment() }

    companion object {
        const val LEAGUE_EXTRA = "league_extra"
        fun createIntent(context: Context, league: League) = Intent(context, LeagueDetailsActivity::class.java).apply {
            putExtra(LEAGUE_EXTRA, league)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaguedetails)

        initialize()

        val navView: BottomNavigationView = findViewById(R.id.nav_view_league)
        val navController = findNavController(R.id.nav_host_fragment_league)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_league_home, R.id.navigation_team, R.id.navigation_standings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val args = bundleOf("league_extra" to league)
        navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId, args)
            true
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun initialize() {
        extractArguments()

    }

    private fun extractArguments() {
        league = intent.getParcelableExtra(LEAGUE_EXTRA)!!
    }
}


