package com.example.lab8.ui.standings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R
import com.google.android.material.snackbar.Snackbar

class StandingsAdapter : RecyclerView.Adapter<StandingsAdapter.ViewHolder> (){

    private val images = intArrayOf(R.drawable.ic_leagues, R.drawable.ic_profile, R.drawable.ic_standings)
    private val leagues = arrayOf("Don't Toews Me Bro", "Shannahand me a beer", "League 1")
    private val teams = arrayOf("Campbell's Team", "Soupy's team", "Guaco Taco")
    private val rank = arrayOf("3", "1", "43")

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemLeagueName: TextView
        var itemTeamName: TextView
        var itemRank: TextView

        init{
            itemImage = itemView.findViewById(R.id.rec_profilePic)
            itemLeagueName = itemView.findViewById(R.id.author)
            itemTeamName = itemView.findViewById(R.id.content)
            itemRank = itemView.findViewById(R.id.timestamp)

            itemView.setOnClickListener{v:View ->
                var position: Int = adapterPosition
                Snackbar.make(v, "Clicked on position $position", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
       val v = LayoutInflater.from(viewGroup.context)
           .inflate(R.layout.leagues_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemImage.setImageResource(images[i])
        viewHolder.itemLeagueName.text = leagues[i]
        viewHolder.itemTeamName.text = teams[i]
        viewHolder.itemRank.text = rank[i]
    }

    override fun getItemCount(): Int {
        return leagues.size
    }


}