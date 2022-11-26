package uz.vianet.syntheticmvc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.vianet.syntheticmvc.R
import uz.vianet.syntheticmvc.adapter.PostAdapter
import uz.vianet.syntheticmvc.model.Post
import uz.vianet.syntheticmvc.network.RetrofitHttp
import uz.vianet.syntheticmvc.utils.Utils.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var posts = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        floating.setOnClickListener { openCreateActivity() }
        apiPostList()

        val extras = intent.extras
        if (extras != null) {
            editPost(extras)
        }
    }

    private fun refreshAdapter(posts: ArrayList<Post>) {
        val adapter = PostAdapter(this, posts)
        recyclerView.setAdapter(adapter)
    }
    fun openCreateActivity() {
        val intent = Intent(this@MainActivity, CreateActivity::class.java)

        launchCreateActivity.launch(intent)
    }

    var launchCreateActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            pb_loading.visibility = View.VISIBLE
            val data = result.data
            if (data != null) {
                val new_title = data.getStringExtra("title")
                val new_post = data.getStringExtra("post")
                val new_userId = data.getStringExtra("id_user")
                val post = Post(new_userId!!.toInt(), new_title!!, new_post!!)
                toast(this@MainActivity,"Title modified")
                apiPostCreate(post)
            }
            // your operation....
        } else {
            Toast.makeText(this@MainActivity, "Operation canceled", Toast.LENGTH_LONG).show()
        }
    }
    private fun editPost(extras:Bundle) {
        Log.d("###", "extras not NULL - ")
        val edit_title = extras.getString("title")
        val edit_post = extras.getString("post")
        val edid_userId = extras.getString("id_user")
        val id = extras.getString("id")!!
        val post = Post(id.toInt(),edid_userId!!.toInt(), edit_title!!, edit_post!!)
        Toast.makeText(this@MainActivity,"Post Edited", Toast.LENGTH_LONG).show()
        apiPostUpdate(post)
    }


    private fun apiPostList() {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.postService.listPost().enqueue(object : Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                //Log.d("@@@", response.body().toString())
                pb_loading.visibility = View.GONE
                posts.clear()
                val items = response.body()
                if (items!=null){
                    for (item in items){
                        val post = Post(item.id,item.userId,item.title,item.body)
                        posts.add(post)
                    }
                }
                refreshAdapter(posts)
            }

            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.e("@@@", t.message.toString())
            }
        })
    }

    private fun apiPostCreate(post: Post) {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.postService.createPost(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.d("@@@", response.body().toString())
                Toast.makeText(this@MainActivity,post.title + " Created",Toast.LENGTH_LONG).show()
                apiPostList()
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("@@@", t.toString())
            }
        })
    }

    private fun apiPostUpdate(post: Post) {
        pb_loading.visibility = View.VISIBLE
        RetrofitHttp.postService.updatePost(post.id, post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    Toast.makeText(this@MainActivity,post.title +" Updated",Toast.LENGTH_LONG).show()
                    if (response.body() != null) {
                        Log.d("@@@", response.body().toString())

                        apiPostList()
                    } else {
                        pb_loading.visibility = View.GONE
                        Log.d("@@@", response.toString())
                    }
                }

                override fun onFailure(call: Call<Post?>, t: Throwable) {
                    pb_loading.visibility = View.GONE
                    Log.d("@@@", t.toString())
                }
            })
    }

    fun apiPostDelete(post: Post) {
        RetrofitHttp.postService.deletePost(post.id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                apiPostList()
                toast(this@MainActivity,"${post.title} Deleted")
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {

            }
        })
    }
}