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
import kotlinx.android.synthetic.main.activity_tampil_tiket.*
import org.json.JSONObject

class TampilTiket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampil_tiket)

        val actionbar = supportActionBar
        actionbar!!.title = "Tiket Kereta"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val hari = bundle?.get("hari").toString()
        tv_hari.setText("Keberangkatan : "+hari)

        kliksepur()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val bundle = intent.extras
        val id_penumpang = bundle?.get("ktp").toString()

        intent = Intent(this, CariTiket::class.java)
        intent.putExtra("ktp",id_penumpang)
        startActivity(intent)
        return true
    }

    override fun onResume(){
        super.onResume()
        TampilSepur()
    }

    private fun kliksepur(){
        lvnya.setOnItemClickListener{adapterView, view, position, id ->

            val idnya = adapterView.getItemAtPosition(position)
            val a = idnya.toString()
            val bundle = intent.extras
            val asl = bundle?.get("asal").toString()
            val tuj = bundle?.get("tuju").toString()
            val id_penumpang = bundle?.get("ktp").toString()
            val hari = bundle?.get("hari").toString()

            intent = Intent(this, ReviewTiket::class.java)
            intent.putExtra("asal",asl)
            intent.putExtra("tuju",tuj)
            intent.putExtra("id_kereta",a)
            intent.putExtra("ktp",id_penumpang)
            intent.putExtra("hari",hari)
            startActivity(intent)
        }
    }

    private fun TampilSepur(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val bundle = intent.extras
        val asl = bundle?.get("asal").toString()
        val tuj = bundle?.get("tuju").toString()

        AndroidNetworking.post(ApiKoneksi.READ)
            .addBodyParameter("asal",asl)
            .addBodyParameter("tujuan",tuj)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    val jsonArray = response?.optJSONArray("result")
                    loading.dismiss()

                    val list_a = arrayListOf<String>()
                    val list_b = arrayListOf<String>()
                    val list_c = arrayListOf<String>()
                    val list_d = arrayListOf<String>()
                    val list_e = arrayListOf<String>()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)
                        list_a.add((jsonObject.getString("namakrt")).toString())
                        list_b.add((jsonObject.getString("asal")).toString())
                        list_c.add((jsonObject.getString("tujuan")).toString())
                        list_d.add((jsonObject.getString("jam_brk")).toString())
                        list_e.add((jsonObject.getString("id")).toString())

                        if (jsonArray?.length()-1 == i) {
                            loading.dismiss()
                            val adapter = LvAdapterTampilTiket(this@TampilTiket, list_e, list_a, list_b, list_c, list_d)
                            lvnya.adapter = adapter
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
