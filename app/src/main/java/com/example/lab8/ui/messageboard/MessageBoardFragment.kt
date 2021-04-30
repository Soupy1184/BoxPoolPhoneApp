package com.example.lab8.ui.messageboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab8.R
import com.example.lab8.firebase.realtimeDatabase.RealtimeDatabaseManager
import com.example.lab8.model.League
import com.example.lab8.model.Post
import com.example.lab8.util.DateUtils
import com.example.lab8.util.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_messageboard.*

const val LEAGUE_REF = "league_extra"
const val USER_REF = "Users"

class MessageBoardFragment : Fragment() {

    private lateinit var league: League
    private val feedAdapter by lazy { MessageBoardAdapter(DateUtils()) }
    private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

    private var postBtn: Button? = null
    private var etPost: EditText? = null
    private var postText: String? = null
    private var author: String? = null

    //database elements
    private var mDatabaseMessageRef: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_messageboard, container, false)



        postBtn = root.findViewById<View>(R.id.postBtn) as Button
        etPost = root.findViewById<View>(R.id.et_post) as EditText

        league = arguments?.getParcelable(LEAGUE_REF)!!
        mDatabaseMessageRef = mDatabase!!.reference.child("Leagues").child(league.leagueId).child("Messages")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsFeed.layoutManager = LinearLayoutManager(context)
        postsFeed.setHasFixedSize(true)
        postsFeed.adapter = feedAdapter

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        postsFeed.addItemDecoration(divider)

        postBtn?.setOnClickListener {
            postText = etPost?.text.toString().trim()
            if (postText!!.isNotEmpty()) {
                realtimeDatabaseManager.addPost(postText!!,
                    author!!, league,
                    { activity?.showToast(getString(R.string.posted_successfully)) },
                    { activity?.showToast(getString(R.string.posting_failed)) })
            }
            it.hideKeyboard()
            etPost!!.text.clear()
        }
    }

    override fun onStart() {
        super.onStart()
        listenForPostsUpdates()
    }

    override fun onStop() {
        super.onStop()
        realtimeDatabaseManager.removePostsValuesChangesListener()
    }

    private fun initialize() {
        mDatabase = FirebaseDatabase.getInstance()

        mAuth = FirebaseAuth.getInstance()

        feedAdapter.onPostItemClick().observe(this, Observer(::onPostItemClick))

        val userId = mAuth!!.currentUser!!.uid
        mDatabase!!.getReference(USER_REF)
            .child(userId)
            .child("username")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    /* No op */
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    author = dataSnapshot.value as String
                }
            })

    }

    private fun listenForPostsUpdates() {
        realtimeDatabaseManager.onPostsValuesChange(league).observe(this, Observer(::onPostsUpdate))
    }

    private fun onPostsUpdate(post: List<Post>){
        feedAdapter.onFeedUpdate(post)
    }

    private fun onPostItemClick(post: Post) {

    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}