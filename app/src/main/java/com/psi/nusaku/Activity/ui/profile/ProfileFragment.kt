package com.psi.nusaku.Activity.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.psi.nusaku.Activity.LoginActivity
import com.psi.nusaku.Activity.PostingActivity
import com.psi.nusaku.Activity.ui.feed.FeedAdapter
import com.psi.nusaku.Activity.ui.feed.FeedRepo
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref


class ProfileFragment : Fragment() {
    lateinit var rvPosting: RecyclerView
    private lateinit var  profileRepo : ProfileRepo
    private var postList : MutableList<Posting> = ArrayList()
    private var profileAdapter : ProfileAdapter = ProfileAdapter(postList)
    lateinit var preferences: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.layout_profile, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        preferences = SharedPref(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = preferences.GetUser().username
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        profileRepo = ProfileRepo()
        view.findViewById<TextView>(R.id.tv_nama).text = username
        view.findViewById<Button>(R.id.btn_logout).setOnClickListener {
            preferences.Clear()
            startActivity(Intent(view.context, LoginActivity::class.java))
        }
        loadPostData()
        rvPosting = view.findViewById(R.id.rv_profile)
        rvPosting.layoutManager = LinearLayoutManager(activity)
        rvPosting.adapter = profileAdapter
    }
    private fun loadPostData(){
        val username = preferences.GetUser().username
        profileRepo.getPostlist(username!!)
            .addOnCompleteListener{
                postList = it.result!!.toObjects(Posting::class.java)
                profileAdapter.postListItems= postList
                profileAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.e("Dashboard", "Error, $it")
            }
    }
}