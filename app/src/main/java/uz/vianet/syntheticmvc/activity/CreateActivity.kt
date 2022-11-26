package uz.vianet.syntheticmvc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import uz.vianet.syntheticmvc.R
import kotlinx.android.synthetic.main.activity_create.*
class CreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initViews()
    }

    private fun initViews() {

        btn_submitCreate.setOnClickListener(View.OnClickListener {
            val title: String = et_titleCreate.getText().toString()
            val post: String = et_textCreate.getText().toString().trim { it <= ' ' }
            val id_user: String = et_userIdCreate.getText().toString().trim { it <= ' ' }
            if (title.isNotEmpty() && post.isNotEmpty() && id_user.isNotEmpty()){
                val intent = Intent()
                intent.putExtra("title", title)
                intent.putExtra("post", post)
                intent.putExtra("id_user", id_user)
                setResult(RESULT_OK, intent)
                super@CreateActivity.onBackPressed()
            }

        })
    }
}