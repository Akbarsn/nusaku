package com.psi.nusaku.Activity.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.psi.nusaku.Activity.DashboardActivity
import com.psi.nusaku.Activity.PostingActivity
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R

class DashboardFragment : Fragment() {

    lateinit var btnTambahPost : Button
    private lateinit var dashboardViewModel: DashboardViewModel
    lateinit var rvPosting: RecyclerView
    private lateinit var  dashboardRepo : DashboardRepo
    private var postList : MutableList<Posting> = ArrayList()
    private var dashboardAdapter : DashboardAdapter = DashboardAdapter(postList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //loadPostData()
        rvPosting = view.findViewById(R.id.rv_posting)
        dashboardRepo = DashboardRepo()
        btnTambahPost = view.findViewById(R.id.btn_tambahPost)
        btnTambahPost.setOnClickListener(){
            val intent = Intent (getActivity(), PostingActivity::class.java)
            getActivity()?.startActivity(intent)
        }
        rvPosting.layoutManager = LinearLayoutManager(activity)
        rvPosting.adapter = dashboardAdapter
    }
    private fun loadPostData(){
        dashboardRepo.getPostlist()
            .addOnCompleteListener{
                postList = it.result!!.toObjects(Posting::class.java)
                dashboardAdapter.postListItems= postList
                dashboardAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.e("Dashboard", "Error, $it")
            }
    }
}