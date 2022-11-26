package uz.vianet.syntheticmvc.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uz.vianet.syntheticmvc.R
import kotlinx.android.synthetic.main.activity_details.*
class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initViews()
    }

    private fun initViews() {
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            tv_title_detail.setText(extras.getString("title")!!.uppercase())
            tv_body_detail.setText(extras.getString("body"))
        }
    }
}