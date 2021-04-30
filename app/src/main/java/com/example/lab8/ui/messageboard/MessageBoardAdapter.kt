package com.example.lab8.ui.messageboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R
import com.example.lab8.model.Post
import com.example.lab8.util.DateUtils
import kotlinx.android.synthetic.main.team_list.view.*

class MessageBoardAdapter(private val dateUtils: DateUtils) : RecyclerView.Adapter<MessageBoardAdapter.ViewHolder> () {

    private val posts = mutableListOf<Post>()
    private val onItemClickLiveData = MutableLiveData<Post>()
    //private val dateUtils: DateUtils? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.messages_list, viewGroup, false)
        return ViewHolder(v, onItemClickLiveData, dateUtils)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) = viewHolder.setItem(posts[i])

    override fun getItemCount(): Int = posts.size

    fun onFeedUpdate(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun onPostItemClick(): LiveData<Post> = onItemClickLiveData

    class ViewHolder(
        itemView: View,
        private val onItemClickLiveData: MutableLiveData<Post>,
        private val dateUtils: DateUtils
    ) : RecyclerView.ViewHolder(itemView) {

        private lateinit var post: Post

        init {
            itemView.setOnClickListener {
                onItemClickLiveData.postValue(post)
            }
        }

        fun setItem(post: Post) {
            this.post = post
            with(itemView) {
                author.text = post.author
                timestamp.text = dateUtils.mapToNormalisedDateText(post.timestamp)
                content.text = post.content
            }
        }
    }
}