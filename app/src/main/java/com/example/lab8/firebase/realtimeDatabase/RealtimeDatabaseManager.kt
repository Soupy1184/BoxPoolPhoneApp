package com.example.lab8.firebase.realtimeDatabase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lab8.firebase.authentication.AuthenticationManager
import com.example.lab8.model.League
import com.example.lab8.model.Post
import com.example.lab8.model.Team
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val LEAGUES_REFERENCE = "Leagues" //POSTS_REFERENCE
private const val TEAMS_REFERENCE = "Teams" //POSTS_REFERENCE
private const val USER_REFERENCE = "Users" //COMMENTS_REFERENCE
private const val POSTS_REFERENCE = "Posts" //COMMENTS_REFERENCE
private const val LEAGUE_CONTENT_PATH = "userLeagues"
private const val LEAGUE_USER_ID_PATH = "leagueId" //

class RealtimeDatabaseManager {
    private val authenticationManager = AuthenticationManager()
    private val database = FirebaseDatabase.getInstance()
    private var mAuth: FirebaseAuth? = null

    private val leaguesValues = MutableLiveData<List<League>>()
    private val teamsValues = MutableLiveData<List<Team>>()
    private val postsValues = MutableLiveData<List<Post>>()
    //private var author = MutableLiveData<String>()
    //private val commentsValues = MutableLiveData<List<Comment>>()

    private lateinit var leaguesValueEventListener: ValueEventListener
    private lateinit var teamsValueEventListener: ValueEventListener
    private lateinit var postsValueEventListener: ValueEventListener
    //private lateinit var commentsValueEventListener: ValueEventListener


    fun addLeague(leagueName: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
        val leaguesReference = database.getReference(LEAGUES_REFERENCE)
        val key = leaguesReference.push().key ?: ""
        val league = createLeague(key, leagueName)

        leaguesReference.child(key)
            .setValue(league)
            .addOnSuccessListener { onSuccessAction() }
            .addOnFailureListener { onFailureAction() }
    }

    fun addTeam(teamName: String, playerName: String, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
        val teamsReference = database.getReference(TEAMS_REFERENCE)
        val key = teamsReference.push().key ?: ""
        val team = createTeam(key, teamName, playerName)

        teamsReference.child(key)
            .setValue(team)
            .addOnSuccessListener { onSuccessAction() }
            .addOnFailureListener { onFailureAction() }
    }

    fun addPost(author: String, content: String, league: League, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
        //1
        val postsReference = database.getReference(LEAGUES_REFERENCE)
            .child(league.leagueId)
            .child("Messages")
        //2
        val key = postsReference.push().key ?: ""
        val post = createPost(key, author, content)

        //3
        postsReference.child(key)
            .setValue(post)
            .addOnSuccessListener { onSuccessAction() }
            .addOnFailureListener { onFailureAction() }
    }

    fun onLeaguesValuesChange(): LiveData<List<League>> {
        listenForLeaguesValueChanges()
        return leaguesValues
    }

    fun onTeamsValuesChange(league: League): LiveData<List<Team>> {
        listenForTeamsValueChanges(league)
        return teamsValues
    }

    fun onPostsValuesChange(league: League): LiveData<List<Post>> {
        listenForPostsValueChanges(league)
        return postsValues
    }

    fun removeLeaguesValuesChangesListener() {
        database.getReference(LEAGUES_REFERENCE).removeEventListener(leaguesValueEventListener)
    }

    fun removeTeamsValuesChangesListener() {
        database.getReference(TEAMS_REFERENCE).removeEventListener(teamsValueEventListener)
    }

    fun removePostsValuesChangesListener() {
        database.getReference(POSTS_REFERENCE).removeEventListener(postsValueEventListener)
    }

    fun updatePostContent(key: String, content: String, league: League) {
        //1
        database.getReference(LEAGUES_REFERENCE)
            //2
            .child(league.leagueId)
            //3
            .child("Messages")
            .child(key)
            //4
            .setValue(content)
    }

    fun updateLeagueContent(key: String, content: String) {
        database.getReference(LEAGUES_REFERENCE)
            .child(key)
            .setValue(content)
    }

    fun deleteLeague(key: String) {
        database.getReference(LEAGUES_REFERENCE)
            .child(key)
            .removeValue()
        //deletePostComments(key)
    }

    fun deletePost(key: String) {
        database.getReference(POSTS_REFERENCE)
            .child(key)
            .removeValue()
    }

//    fun addComment(postId: String, content: String) {
//        val commentsReference = database.getReference(COMMENTS_REFERENCE)
//        val key = commentsReference.push().key ?: ""
//        val comment = createComment(postId, content)
//
//        commentsReference.child(key).setValue(comment)
//    }

//    fun onCommentsValuesChange(postId: String): LiveData<List<Comment>> {
//        listenForPostCommentsValueChanges(postId)
//        return commentsValues
//    }

//    fun removeCommentsValuesChangesListener() {
//        database.getReference(COMMENTS_REFERENCE).removeEventListener(commentsValueEventListener)
//    }

//    private fun listenForPostCommentsValueChanges(postId: String) {
//        commentsValueEventListener = object : ValueEventListener {
//            override fun onCancelled(databaseError: DatabaseError) {
//                /* No op */
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    val comments = dataSnapshot.children.mapNotNull { it.getValue(Comment::class.java) }.toList()
//                    commentsValues.postValue(comments)
//                } else {
//                    commentsValues.postValue(emptyList())
//                }
//            }
//        }
//
//        database.getReference(COMMENTS_REFERENCE)
//            .orderByChild(COMMENT_POST_ID_PATH)
//            .equalTo(postId)
//            .addValueEventListener(commentsValueEventListener)
//    }

    private fun createLeague(key: String, leagueName: String): League {
        val user = authenticationManager.getCurrentUser().toString()
        //val timestamp = getCurrentTime()
        return League(key, leagueName, user) // timestamp
    }

    private fun createTeam(key: String, teamName: String, playerName: String): Team {
        //val user = authenticationManager.getCurrentUser().toString()
        //val timestamp = getCurrentTime()
        return Team(teamName, playerName) // timestamp, user, key
    }

    private fun createPost(key: String, author:String, content: String): Post {
        mAuth = FirebaseAuth.getInstance()

        val timestamp = getCurrentTime()
        return Post(key, author, content, timestamp)
    }



//    private fun deletePostComments(postId: String) {
//        database.getReference(COMMENTS_REFERENCE)
//            .orderByChild(COMMENT_POST_ID_PATH)
//            .equalTo(postId)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(databaseError: DatabaseError) {
//                    /* No op */
//                }
//
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    dataSnapshot.children.forEach { it.ref.removeValue() }
//                }
//            })
//    }

    private fun listenForLeaguesValueChanges() {
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth!!.currentUser!!.uid
        leaguesValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val leagues = dataSnapshot.children.mapNotNull { it.getValue(League::class.java) }.toList()
                    leaguesValues.postValue(leagues)
                }
                else {
                    leaguesValues.postValue(emptyList())
                }
            }
        }
        database.getReference(USER_REFERENCE)
            .child(userId)
            .child("userLeagues")
            .addValueEventListener(leaguesValueEventListener)
    }

    private fun listenForTeamsValueChanges(league: League) {
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth!!.currentUser!!.uid
        teamsValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val teams = dataSnapshot.children.mapNotNull { it.getValue(Team::class.java) }.toList()
                    teamsValues.postValue(teams)
                }
                else {
                    teamsValues.postValue(emptyList())
                }
            }
        }
        database.getReference(LEAGUES_REFERENCE)
            .child(league.leagueId)
            .child("leagueMembers")
            .child(userId)
            .child("team")
            .addValueEventListener(teamsValueEventListener)
    }

    private fun listenForPostsValueChanges(league: League) {
        //1
        postsValueEventListener = object : ValueEventListener {
            //2
            override fun onCancelled(databaseError: DatabaseError) {
                /* No op */
            }

            //3
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //4
                if (dataSnapshot.exists()) {
                    val posts = dataSnapshot.children.mapNotNull { it.getValue(Post::class.java) }.toList()
                    postsValues.postValue(posts)
                } else {
                    //5
                    postsValues.postValue(emptyList())
                }
            }
        }

        //6
        database.getReference(LEAGUES_REFERENCE)
            .child(league.leagueId)
            .child("Messages")
            .addValueEventListener(postsValueEventListener)
    }

//    private fun createComment(postId: String, content: String): Comment {
//        val user = authenticationManager.getCurrentUser()
//        val timestamp = getCurrentTime()
//        return Comment(postId, user, timestamp, content)
//    }

    private fun getCurrentTime() = System.currentTimeMillis()
}