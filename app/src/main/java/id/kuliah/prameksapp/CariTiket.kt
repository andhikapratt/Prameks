package id.kuliah.prameksapp

import android.app.AlertDialog
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
import org.json.JSONObject

class CariTiket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caritiket)
        val loading = ProgressDialog(this)
        loading.dismiss()
        val actionbar = supportActionBar
        actionbar!!.title = "Prameks"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val id_penumpang = bundle?.get("id_penumpang").toString()

        sp_asal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                tv_asal.text = getResources().getStringArray(R.array.asal_dd)[i]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>){}
        }

        sp_tuj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                tv_tuju.text = getResources().getStringArray(R.array.tuju_dd)[i]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>){}
        }

        bt_cari.setOnClickListener{
            intent = Intent(this, TampilTiket::class.java)
            var a = tv_asal.text.toString()
            var b = tv_tuju.text.toString()
            intent.putExtra("asal",a)
            intent.putExtra("tuju",b)
            intent.putExtra("id_penumpang",id_penumpang)
            startActivity(intent)
        }

        bt_mytrips.setOnClickListener{
            intent = Intent(this, MyTrips::class.java)
            intent.putExtra("id_penumpang",id_penumpang)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val builder = AlertDialog.Builder(this@CariTiket)
        builder.setTitle("Logout")
        builder.setMessage("Apakah anda yakin?")

        builder.setPositiveButton("Ya"){dialog, which ->
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        builder.setNegativeButton("Tidak"){dialog,which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        onBackPressed()
        finish()
        return true
    }

}
