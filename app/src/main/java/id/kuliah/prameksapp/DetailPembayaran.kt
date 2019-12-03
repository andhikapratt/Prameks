package id.kuliah.prameksapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_pembayaran.*
import kotlinx.android.synthetic.main.activity_pembayaran.*

class DetailPembayaran : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pembayaran)
        val actionbar = supportActionBar
        actionbar!!.title = "Selesai"

        bt_kembali.setOnClickListener{
            val bundle = intent.extras
            val id_penum = bundle?.get("id_penumpang").toString()

            intent = Intent(this, CariTiket::class.java)
            intent.putExtra("id_penumpang", id_penum)
            startActivity(intent)
            finish()
        }
    }
}
