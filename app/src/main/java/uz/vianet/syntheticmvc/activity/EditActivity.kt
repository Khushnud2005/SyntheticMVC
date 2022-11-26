package uz.vianet.syntheticmvc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uz.vianet.syntheticmvc.R
import kotlinx.android.synthetic.main.activity_edit.*
class EditActivity : AppCompatActivity() {
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        initViews()
    }
    fun initViews(){
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            et_userIdEdit.setText(extras.getString("user_id"))
            et_titleEdit.setText(extras.getString("title"))
            et_textEdit.setText(extras.getString("body"))
            id = extras.getString("id")!!
        }
        btn_submitEdit.setOnClickListener {
            val title = et_titleEdit.text.toString()
            val post = et_textEdit.text.toString().trim { it <= ' ' }
            val id_user = et_userIdEdit.text.toString().trim { it <= ' ' }
            val intent = Intent(this@EditActivity, MainActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("post", post)
            intent.putExtra("id_user", id_user)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}