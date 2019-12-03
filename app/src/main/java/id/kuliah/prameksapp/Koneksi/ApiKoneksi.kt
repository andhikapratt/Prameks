package id.kuliah.prameksapp.Koneksi

class ApiKoneksi {
    companion object {
        private val SERVER = "http://192.168.43.138/prameks/"
        val READ  = SERVER + "read.php"
        val READ2 = SERVER + "read2.php"
        val READ3 = SERVER + "read_penumpang.php"
        val READ4 = SERVER + "show_penumpang.php"
        val READ5 = SERVER + "show_mytrips.php"
        val READ6 = SERVER + "read_akun.php"

        val CREATE2 = SERVER + "create_detail_pesan.php"
        val CREATE3 = SERVER + "create_penumpang.php"

        val DELETE = SERVER + "batalkan_tiket.php"

        val UPDATE = SERVER + "update_akun.php"
    }
}