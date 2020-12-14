package com.psi.nusaku.Activity.ui.feed

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.psi.nusaku.Activity.PostingActivity
import com.psi.nusaku.Model.Posting
import com.psi.nusaku.R


class FeedFragment : Fragment() {

    lateinit var btnTambahPost : FloatingActionButton
    lateinit var rvPosting: RecyclerView
    private lateinit var  feedRepo : FeedRepo
    private var postList : MutableList<Posting> = ArrayList()
    private var feedAdapter : FeedAdapter = FeedAdapter(postList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_feed, container, false)
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        feedRepo = FeedRepo()
        loadPostData()
        rvPosting = view.findViewById(R.id.rv_feed)
        btnTambahPost = view.findViewById(R.id.btn_tambahPost)
        btnTambahPost.setOnClickListener{
            val intent = Intent(getActivity(), PostingActivity::class.java)
            getActivity()?.startActivity(intent)
        }
        rvPosting.layoutManager = LinearLayoutManager(activity)
        rvPosting.adapter = feedAdapter
    }
    private fun loadPostData(){
        feedRepo.getPostlist()
            .addOnCompleteListener{
                postList = it.result!!.toObjects(Posting::class.java)
                feedAdapter.postListItems= postList
                Log.i("Feed", postList.get(0).toString())
                feedAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.e("Dashboard", "Error, $it")
            }
    }
}