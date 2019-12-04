package id.kuliah.prameksapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_review_tiket.*
import org.json.JSONObject

class ReviewTiket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_tiket)
        val actionbar = supportActionBar
        actionbar!!.title = "Detail Tiket"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val asl = bundle?.get("id_kereta").toString()
        val id_penumpang = bundle?.get("ktp").toString()
        val hari = bundle?.get("hari").toString()

        txt_hari.setText(hari)

        bt_pesan.setOnClickListener{
            intent = Intent(this, DataPenumpang::class.java)
            intent.putExtra("id_kereta", asl)
            intent.putExtra("ktp", id_penumpang)
            intent.putExtra("hari", hari)
            startActivity(intent)
        }
    }
//==================================================================================================
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val bundle = intent.extras
        val id_penumpang = bundle?.get("ktp").toString()
        val asl = bundle?.get("asal").toString()
        val tuj = bundle?.get("tuju").toString()
        val hari = bundle?.get("hari").toString()

        intent = Intent(this, TampilTiket::class.java)
        intent.putExtra("ktp",id_penumpang)
        intent.putExtra("asal",asl)
        intent.putExtra("tuju",tuj)
        intent.putExtra("hari", hari)
        startActivity(intent)
        return true
    }
//==================================================================================================
    override fun onResume(){
        super.onResume()
        TampilReview()
    }
//==================================================================================================
    private fun TampilReview(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val bundle = intent.extras
        val asl = bundle?.get("id_kereta").toString()

        AndroidNetworking.post(ApiKoneksi.READ2)
            .addBodyParameter("id",asl)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)
                        val nama = jsonObject.getString("namakrt").toString()
                        val asal = jsonObject.getString("asal").toString()
                        val tujuan =jsonObject.getString("tujuan").toString()
                        val jam_brk =jsonObject.getString("jam_brk").toString()
                        val jam_smp =jsonObject.getString("jam_smp").toString()
                        val harga = jsonObject.getString("harga").toString()

                        if (jsonArray?.length()-1 == i){
                            loading.dismiss()
                            txt_nmkrt.text = nama
                            txt_asal.text = asal
                            txt_tujuan.text = tujuan
                            txt_jb.text = jam_brk
                            txt_harga.text = "Rp.$harga"
                            txt_js.text = jam_smp
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }
//==================================================================================================
}
