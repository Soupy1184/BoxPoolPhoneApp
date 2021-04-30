package com.example.lab8.ui.team

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R
import com.example.lab8.model.Team
import kotlinx.android.synthetic.main.team_list.view.*

class TeamAdapter() : RecyclerView.Adapter<TeamAdapter.ViewHolder> () {

//    private val teams = arrayOf("Campbell's Team", "Soupy's team", "Guaco Taco")
//    private val rank = arrayOf("3", "1", "43")
//    private val leagues = arrayOf<List>()
//    private val userLeagues = LeagueDataClass().userLeagues?.values

    private val teams = mutableListOf<Team>()
    private val onItemClickLiveData = MutableLiveData<Team>()



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.team_list, viewGroup, false)
        return ViewHolder(v, onItemClickLiveData)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) = viewHolder.setItem(teams[i])

    override fun getItemCount(): Int = teams.size

    fun onFeedUpdate(teams: List<Team>) {
        this.teams.clear()
        this.teams.addAll(teams)
        notifyDataSetChanged()
    }

    fun onTeamItemClick(): LiveData<Team> = onItemClickLiveData

    class ViewHolder(
        itemView: View,
        private val onItemClickLiveData: MutableLiveData<Team>
    ) : RecyclerView.ViewHolder(itemView) {

        private lateinit var team: Team

        init {
            itemView.setOnClickListener {
                onItemClickLiveData.postValue(team)
            }
        }

        fun setItem(team: Team) {
            this.team = team
            with(itemView) {
                author.text = team.playerName
                timestamp.text = team.boxNumber.toString()
                content.text = team.points.toString()
            }
        }
    }
}