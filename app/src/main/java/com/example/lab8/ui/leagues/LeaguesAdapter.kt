package com.example.lab8.ui.leagues

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.model.League
import com.example.lab8.R
import kotlinx.android.synthetic.main.leagues_list.view.*

class LeaguesAdapter() : RecyclerView.Adapter<LeaguesAdapter.ViewHolder> (){

//    private val images = intArrayOf(R.drawable.ic_leagues, R.drawable.ic_profile, R.drawable.ic_standings)
//    private val teams = arrayOf("Campbell's Team", "Soupy's team", "Guaco Taco")
//    private val rank = arrayOf("3", "1", "43")
//    private val leagues = arrayOf<List>()
//    private val userLeagues = LeagueDataClass().userLeagues?.values

    private val leagues = mutableListOf<League>()
    private val onItemClickLiveData = MutableLiveData<League>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.leagues_list, viewGroup, false)
        return ViewHolder(v, onItemClickLiveData)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) = viewHolder.setItem(leagues[i])

    override fun getItemCount(): Int = leagues.size

    fun onFeedUpdate(leagues: List<League>) {
        this.leagues.clear()
        this.leagues.addAll(leagues)
        notifyDataSetChanged()
    }

    fun onLeagueItemClick(): LiveData<League> = onItemClickLiveData

    class ViewHolder(
        itemView: View,
        private val onItemClickLiveData: MutableLiveData<League>
    ): RecyclerView.ViewHolder(itemView){

        private lateinit var league: League

        init{
            itemView.setOnClickListener{
                onItemClickLiveData.postValue(league)
            }
        }

        fun setItem(league: League){
            this.league = league
            with(itemView) {
                author.text = league.leagueName
            }
        }
    }








}

//        var itemImage: ImageView = itemView.findViewById(R.id.rec_profilePic)
//        var itemLeagueName: TextView = itemView.findViewById(R.id.rec_leagueName)
//        var itemTeamName: TextView = itemView.findViewById(R.id.rec_teamName)
//        var itemRank: TextView = itemView.findViewById(R.id.rec_rank)

//fun bind(league: League, context: Context) {
//            itemLeagueName.text = league.leagueName
//        }

//viewHolder.bind(leagues[i], context)
//
//viewHolder.itemImage.setImageResource(images[i])
//        viewHolder.itemLeagueName.text = leagues[i]
//        viewHolder.itemTeamName.text = teams[i]
//        viewHolder.itemRank.text = rank[i]