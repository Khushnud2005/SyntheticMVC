package uz.vianet.syntheticmvc.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import uz.vianet.syntheticmvc.R
import uz.vianet.syntheticmvc.activity.DetailsActivity
import uz.vianet.syntheticmvc.activity.EditActivity
import uz.vianet.syntheticmvc.activity.MainActivity
import uz.vianet.syntheticmvc.model.Post
import java.lang.String
import kotlinx.android.synthetic.main.item_post_list.view.*
import uz.vianet.syntheticmvc.utils.Utils

class PostAdapter(val activity: MainActivity, val items:ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: Post = items[position]

        holder.sl_swipe.showMode = SwipeLayout.ShowMode.PullOut
        holder.sl_swipe.addDrag(SwipeLayout.DragEdge.Left,holder.linear_left)
        holder.sl_swipe.addDrag(SwipeLayout.DragEdge.Right,holder.linear_right)

        holder.tv_title.setText(post.title.toUpperCase())
        holder.tv_body.setText(post.body)

        holder.tv_delete.setOnClickListener {
            deletePostDialog(post)
        }
        holder.tv_edit.setOnClickListener {
            val intent = Intent(activity.baseContext, EditActivity::class.java)
            intent.putExtra("id", String.valueOf(post.id))
            intent.putExtra("user_id", String.valueOf(post.userId))
            intent.putExtra("title", post.title)
            intent.putExtra("body", post.body)
            activity.startActivity(intent)
        }
        holder.ll_item.setOnClickListener {
            val intent = Intent(activity.baseContext, DetailsActivity::class.java)
            intent.putExtra("id",post.id)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sl_swipe: SwipeLayout
        var tv_title: TextView
        var tv_body: TextView
        var tv_delete: TextView
        var tv_edit: TextView
        var linear_left: LinearLayout
        var linear_right: LinearLayout
        var ll_item: LinearLayout

        init {
            sl_swipe = itemView.sl_swipe
            linear_left = itemView.ll_linear_left
            linear_right = itemView.ll_linear_right
            ll_item = itemView.ll_item
            tv_title = itemView.tv_title
            tv_body = itemView.tv_body
            tv_delete = itemView.tv_delete
            tv_edit = itemView.tv_edit
        }
    }
    private fun deletePostDialog(post: Post) {
        val title = "Delete"
        val body = "Do you want to delete?"
        Utils.customDialog(activity, title, body, object : Utils.DialogListener {
            override fun onPositiveClick() {
                activity.apiPostDelete(post)
            }

            override fun onNegativeClick() {
            }
        })
    }
}