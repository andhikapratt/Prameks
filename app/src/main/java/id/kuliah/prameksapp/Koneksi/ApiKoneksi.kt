package id.kuliah.prameksapp.Koneksi

class ApiKoneksi {
    companion object {
        private val SERVER = "http://192.168.43.138/prameks/"
        val READ = SERVER + "read.php"
        val READ2 = SERVER + "read2.php"
        val READ3 = SERVER + "read_penumpang.php"
        val READ4 = SERVER + "show_penumpang.php"
        val READ5 = SERVER + "show_mytrips.php"

        val CREATE2 = SERVER + "create_detail_pesan.php"
    }
}