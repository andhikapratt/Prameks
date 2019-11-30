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
import id.kuliah.prameksapp.Adapter.LvAdapterTampilTiket
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_data_penumpang.*
import kotlinx.android.synthetic.main.activity_tampil_tiket.*
import org.json.JSONObject

class DataPenumpang : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_penumpang)
        val actionbar = supportActionBar
        actionbar!!.title = "Pemesanan"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val id_kereta = bundle?.get("id_kereta").toString()
        val id_penum = bundle?.get("id_penumpang").toString()

        bt_go.setOnClickListener{
            intent = Intent(this, Pembayaran::class.java)
            intent.putExtra("id_kereta", id_kereta)
            intent.putExtra("id_penumpang", id_penum)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        val bundle = intent.extras
        val asl = bundle?.get("id_kereta").toString()
        val id_penumpang = bundle?.get("id_penumpang").toString()

        intent = Intent(this, ReviewTiket::class.java)
        intent.putExtra("id_kereta", asl)
        intent.putExtra("id_penumpang",id_penumpang)
        startActivity(intent)
        return true
    }

    override fun onResume(){
        super.onResume()
        TampilPenumpang()
    }

    private fun TampilPenumpang(){

        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val bundle = intent.extras
        val id_penum = bundle?.get("id_penumpang").toString()

        AndroidNetworking.post(ApiKoneksi.READ4)
            .addBodyParameter("id_penumpang",id_penum)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)
                        val ktpp = jsonObject.getString("ktp").toString()
                        val namaa = jsonObject.getString("nama").toString()
                        val notelpp = jsonObject.getString("notelp").toString()
                        val emaill = jsonObject.getString("email").toString()

                        if (jsonArray?.length()-1 == i) {
                            loading.dismiss()
                            et_ktp.setText(ktpp)
                            et_nama.setText(namaa)
                            et_notelp.setText(notelpp)
                            et_email.setText(emaill)
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
}