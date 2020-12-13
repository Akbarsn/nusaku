package com.psi.nusaku.Activity.ui.home

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psi.nusaku.Activity.ui.feed.FeedAdapter
import com.psi.nusaku.Activity.ui.feed.FeedRepo
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R
import com.psi.nusaku.Utils.SharedPref

class HomeFragment : Fragment() {
    private var postList : MutableList<Posting> = ArrayList()
    private var homeAdapter : HomeAdapter = HomeAdapter(postList)
    lateinit var rvPostHome: RecyclerView
    private lateinit var  homeRepo : HomeRepo
    lateinit var preferences: SharedPref

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.layout_home, container, false)
        preferences = SharedPref(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_nama_akun).text = preferences.GetUser().username
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        homeRepo = HomeRepo()
        loadPostData()
        rvPostHome = view.findViewById(R.id.rv_home)
        rvPostHome.layoutManager = LinearLayoutManager(activity)
        rvPostHome.adapter = homeAdapter
    }
    private fun loadPostData(){
        homeRepo.getPostlist()
            .addOnCompleteListener{
                postList = it.result!!.toObjects(Posting::class.java)
                homeAdapter.postListItems = postList
//                HomeAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.e("Home", "Error, $it")
            }
    }
}