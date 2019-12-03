package id.kuliah.prameksapp

import android.app.AlertDialog
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
import id.kuliah.prameksapp.Adapter.LvAdapterMyTrips
import id.kuliah.prameksapp.Adapter.LvAdapterTampilTiket
import id.kuliah.prameksapp.Koneksi.ApiKoneksi
import kotlinx.android.synthetic.main.activity_tampil_tiket.*
import org.json.JSONObject

class MyTrips : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trips)
        val actionbar = supportActionBar
        actionbar!!.title = "My Trips"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val penumm = bundle?.get("id_penumpang").toString()

        lvnya.setOnItemClickListener{adapterView, view, position, id ->
            val loading = ProgressDialog(this)
            val builder = AlertDialog.Builder(this@MyTrips)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Hapus data?")
            builder.setPositiveButton("Ya"){dialog, which ->

            val itemAtPos = adapterView.getItemAtPosition(position)

            AndroidNetworking.post(ApiKoneksi.DELETE)
                .addBodyParameter("kode_bayar",itemAtPos.toString())
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

                intent = Intent(this, MyTrips::class.java)
                intent.putExtra("id_penumpang",penumm)
                startActivity(intent)
            }

            builder.setNegativeButton("Batal"){dialog,which ->}

            builder.setNeutralButton(""){_,_ ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val bundle = intent.extras
        val penumm = bundle?.get("id_penumpang").toString()
        intent = Intent(this, CariTiket::class.java)
        intent.putExtra("id_penumpang",penumm)
        startActivity(intent)
        onBackPressed()
        return true
    }

    override fun onResume(){
        super.onResume()
        TampilSepur()
    }

    private fun TampilSepur(){
        val loading = ProgressDialog(this)
        loading.setMessage("Memuat data...")
        loading.show()

        val bundle = intent.extras
        val penumm = bundle?.get("id_penumpang").toString()

        AndroidNetworking.post(ApiKoneksi.READ5)
            .addBodyParameter("id_akun",penumm)
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
                    val list_f = arrayListOf<String>()
                    val list_g = arrayListOf<String>()

                    for(i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)

                        list_a.add((jsonObject.getString("namakrt")).toString())
                        list_b.add((jsonObject.getString("kode_bayar")).toString())
                        list_c.add((jsonObject.getString("asal")).toString())
                        list_d.add((jsonObject.getString("tujuan")).toString())
                        list_e.add((jsonObject.getString("jam_brk")).toString())
                        list_f.add((jsonObject.getString("jam_smp")).toString())
                        list_g.add((jsonObject.getString("harga")).toString())

                        if (jsonArray?.length()-1 == i) {
                            loading.dismiss()
                            val adapter = LvAdapterMyTrips(this@MyTrips, list_a, list_b, list_c, list_d, list_e, list_f, list_g)
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
