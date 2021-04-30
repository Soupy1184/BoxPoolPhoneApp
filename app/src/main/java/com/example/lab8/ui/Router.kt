package com.example.lab8.ui

import android.app.Activity
import com.example.lab8.*
import com.example.lab8.model.League

class Router {

    fun startHomeScreen(activity: Activity) {
        val intent = MainActivity.createIntent(activity)
        activity.startActivity(intent)
    }

    fun startLoginScreen(activity: Activity) {
        val intent = LoginActivity.createIntent(activity)
        activity.startActivity(intent)
    }

    fun startNewLeagueScreen(activity: Activity) {
        val intent = NewLeagueActivity.createIntent(activity)
        activity.startActivity(intent)
    }

    fun startLeagueDetailsActivity(activity: Activity, league: League) {
        val intent = LeagueDetailsActivity.createIntent(activity, league)
        activity.startActivity(intent)
    }

    fun startNewTeamActivity(activity: Activity, league: League) {
        val intent = NewTeamActivity.createIntent(activity, league)
        activity.startActivity(intent)
    }
}