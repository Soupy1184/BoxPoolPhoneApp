package com.example.lab8

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lab8.model.League
import com.example.lab8.ui.Router
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_newteam.*
import java.lang.Exception

class NewTeamActivity: AppCompatActivity() {

    private lateinit var league: League
    private val router by lazy { Router() }

    companion object {
        const val LEAGUE_EXTRA = "league_extra"
        fun createIntent(context: Context, league: League) = Intent(context, NewTeamActivity::class.java).apply {
            putExtra(LEAGUE_EXTRA, league)
        }
    }

    private var mUserDatabaseRef: DatabaseReference? = null
    private var mLeagueDatabaseRef: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private val TAG = "LoggedInActivity"

    private var box1: String? = null
    private var box2: String? = null
    private var box3: String? = null
    private var box4: String? = null
    private var teamName: String? = null
   /* var box5: String = ""
    var box6: String = ""
    var box7: String = ""
    var box8: String = ""
    var box9: String = ""
    var box10: String = ""
    var box11: String = ""
    var box12: String = ""*/

    private var btnSubmitTeam: Button? = null
    private var etTeamName: EditText? = null
    private var radBox1: RadioGroup? = null
    private var radBox2: RadioGroup? = null
    private var radBox3: RadioGroup? = null
    private var radBox4: RadioGroup? = null

    private var etTest: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newteam)

        initialise()
    }

    private fun initialise() {
        extractArguments()

        etTest = findViewById<View>(R.id.textView) as TextView

        try {
            etTest?.text = league.leagueName
        }
        catch (e: Exception){
            etTest?.text = e.toString()
        }

        etTeamName = findViewById<View>(R.id.et_team_name) as EditText
        btnSubmitTeam = findViewById<View>(R.id.btn_submit_team) as Button
        radBox1 = findViewById<View>(R.id.box1choices) as RadioGroup
        radBox2 = findViewById<View>(R.id.box2choices) as RadioGroup
        radBox3 = findViewById<View>(R.id.box3choices) as RadioGroup
        radBox4 = findViewById<View>(R.id.box4choices) as RadioGroup
        mDatabase = FirebaseDatabase.getInstance()
        mUserDatabaseRef = mDatabase!!.reference!!.child("Users")
        mLeagueDatabaseRef = mDatabase!!.reference!!.child("Leagues")
        mAuth = FirebaseAuth.getInstance()
        btnSubmitTeam!!.setOnClickListener { createNewTeam() }
        radBox1!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            box1name.text = "Box 1: ${radio.text}"
            box1 = radio.text.toString()
        })
        radBox2!!.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            box2name.text = "Box 2: ${radio.text}"
            box2 = radio.text.toString()
        })
        radBox3!!.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            box3name.text = "Box 3: ${radio.text}"
            box3 = radio.text.toString()
        })
        radBox4!!.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            box4name.text = "Box 4: ${radio.text}"
            box4 = radio.text.toString()
        })
    }


    private fun createNewTeam() {
        teamName = etTeamName?.text.toString()

        if (!TextUtils.isEmpty(teamName)) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createNewTeam:success")
            val userId = mAuth!!.currentUser!!.uid
            val teamList = listOf(box1, box2, box3, box4)


            //update user profile information
//            val currentUserDb = mUserDatabaseRef!!.child(userId)
//            currentUserDb.child("teams").setValue(newTeam)
            var i: Int = 0

            val leagueDbRef = mLeagueDatabaseRef!!.child(league.leagueId).child("leagueMembers").child(userId)
            for(x in teamList){
                if (x != null) {
                    leagueDbRef.child("team").child("box" + (i+1)).child("playerName").setValue(teamList[i])
                }
                if (x != null) {
                    leagueDbRef.child("team").child("box" + (i+1)).child("points").setValue(0)
                }
                if (x != null) {
                    leagueDbRef.child("team").child("box" + (i+1)).child("boxNumber").setValue(i+1)
                }
                i++
            }

            leagueDbRef.child("teamPicked").setValue(true)
            leagueDbRef.child("teamName").setValue(teamName)
            updateUserInfoAndUI()
        }
        else {
            // If sign in fails, display a message to the user.
            Log.d(TAG, "Create new team failure")
            Toast.makeText(this@NewTeamActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun extractArguments() {
        league = intent.getParcelableExtra(LEAGUE_EXTRA)!!
    }

    private fun updateUserInfoAndUI(){
        router.startLeagueDetailsActivity(this, league)
    }
}
