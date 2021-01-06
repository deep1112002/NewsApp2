package com.example.newsapp2

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchData()
        recycleId.layoutManager=LinearLayoutManager(this)
        mAdapter = NewsListAdapter(this)
        recycleId.adapter=mAdapter


    }
    private fun fetchData(){
        val url="https://inshortsapi.vercel.app/news?category=science"
        val newArray=ArrayList<News>()

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest=JsonObjectRequest(Request.Method.GET,url,null, Response.Listener{
            val newsJsonArray=it.getJSONArray("data")
            for(i in 0 until newsJsonArray.length()){
                val newsJsonObject=newsJsonArray.getJSONObject(i)
                val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("imageUrl")
                )
                newArray.add(news)
            }
            mAdapter.updateNews(newArray)
            progressBar.visibility=View.INVISIBLE
        },
                Response.ErrorListener{
                    Toast.makeText(this,"Some error with Volley has happened $it",Toast.LENGTH_LONG).show()
                })
        queue.add(jsonObjectRequest)


    }

    override fun onItemClicked(item: News) {
        val builder=CustomTabsIntent.Builder()
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}