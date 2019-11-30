package id.kuliah.prameksapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_caritiket.*
import kotlinx.android.synthetic.main.activity_data_penumpang.*
import kotlinx.android.synthetic.main.activity_pembayaran.*
import org.json.JSONObject

class Pembayaran : AppCompatActivity() {

    private val kode = (0..10000000).random().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        sp_bank.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                tv_bankk.text = getResources().getStringArray(R.array.bayar)[i]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>){}
        }

        bt_selesai.setOnClickListener{
            create()

            val bundle = intent.extras
            val id_penum = bundle?.get("id_penumpang").toString()

            intent = Intent(this, DetailPembayaran::class.java)
            intent.putExtra("id_penumpang", id_penum)
            startActivity(intent)
        }

        kd_byr.setText(kode)
    }

    private fun create(){
        val loading = ProgressDialog(this)
        loading.setMessage("Menambahkan data...")
        loading.show()
        val bundle = intent.extras
        val id_kereta = bundle?.get("id_kereta").toString()
        val id_penum = bundle?.get("id_penumpang").toString()
        val metode = tv_bankk.text.toString()

        println(kode+" "+id_kereta+" "+id_penum+" "+metode)

        AndroidNetworking.post(ApiKoneksi.CREATE2)
            .addBodyParameter("id_jadwal",id_kereta)
            .addBodyParameter("id_akun",id_penum)
            .addBodyParameter("kode_bayar",kode)
            .addBodyParameter("metode_bayar",metode)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()
                    Toast.makeText(applicationContext,response?.getString("message"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("message")?.contains("successfully")!!){}
                }
                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR",anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
