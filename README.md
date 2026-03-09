<details>
<Summary><b>Refleksi 1</b></Summary>

### Clean code yang sudah diterapkan:
1. Meaningful Names: Memastikan semua variabel dan method sudah jelas nama dan fungsinya untuk apa
2. Function Size: Memastikan setiap fungsi mengerjakan 1 hal saja
3. DRY (Don't Repeat Yourself): Memastikan tidak ada logika yang berulang, kalau berulang dipindahkan ke bagian service

### Secure coding practices yang diterapkan:
1. Penggunaan UUID: Menggunakan UUID sebagai tipe data productId
2. Validation Ready: Menggunakan `````@ModelAttribute````` yang mempermudah jika ingin menambahkan anotasi validasi
</details>

<details>
<summary><b>Refleksi 2</b></summary>

1. Dengan adanya unit test, kode yang kita kerjakan bisa memastikan bahwa logika-logika yang terdapat dalam program berjalan sesuai ekspektasi. Selain itu, unit test juga dapat memudahkan proses debugging. Jika terjadi kesalahan di masa depan setelah mengganti kode, maka di dalam test akan langsung tahu bagian mana yang error.
Banyaknya unit test yang diperlukan untuk setiap projek biasanya 1 test untuk 1 function. Cara untuk memastikan unit test sudah cukup adalah ketika semua bagian kode sudah di test. Code coverage 100% artinya setiap bagian kode sudah dieksekusi tetapi bisa saja ada kemungkinan error karena kesalahan logika dalam kode
2. Jika saya membuat kelas test baru dengan menyalin prosedur setup dan variabel instance yang sama persis dari kelas sebelumnya, maka akan ada duplikasi kode yang melanggar principle clean code. Saran perbaikan
yang bisa dilakukan salah satunya adalah inheritance, yaitu membuat sebuah parent class (misalnya BaseFunctionalTest.java) yang menampung semua variabel umum seperti serverPort, testBaseUrl, dan metode setupTest. Kelas test lainnya cukup melakukan extends ke kelas ini
</details>


<details>
<Summary><b>Refleksi 3</b></Summary>

1. Code Quality Issues
    - Field Injection (menggunakan ```@Autowired``` pada field)
        - Issue: SonarCloud menandai penggunaan ```@Autowired``` langsung pada field (seperti ```private ProductService service;```) sebagai praktik yang kurang baik. Hal ini karena field injection membuat unit testing menjadi sulit, menyembunyikan dependensi kelas, dan membuat field tidak bisa bersifat immutable.
        - Strategi Perbaikan: Saya melakukan refactoring menjadi Constructor Injection. Lalu saya menghapus anotasi ```@Autowired``` dari field dan mengubah modifier field menjadi private final untuk menjamin immutability. Terakhir, saya membuat constructor yang menerima dependensi tersebut sebagai parameter. Spring secara otomatis akan menyuntikkan dependensi melalui constructor ini tanpa perlu anotasi ```@Autowired```.

    - Code Coverage yang Rendah (Uncovered Code)
        - Issue: SonarCloud melaporkan 0% coverage pada baris constructor baru karena laporan JaCoCo XML yang belum tergenerate.
        - Strategi Perbaikan: Pada konfigurasi CI, saya menambahkan plugin jacoco di build.gradle.kts, mengaktifkan laporan XML, dan memastikan workflow GitHub Actions menjalankan task jacocoTestReport sebelum analisis SonarCloud.
2. Menurut saya, implementasi saya saat ini sudah memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD).

    - Untuk Continuous Integration (CI): Workflow GitHub Actions saya telah dikonfigurasi agar berjalan secara otomatis pada setiap push dan pull request ke dalam repositori. Pipeline CI ini secara konsisten menyiapkan Java environment, mengompilasi kode, dan mengeksekusi seluruh unit test secara otomatis untuk memverifikasi fungsionalitas aplikasi. Selain itu, pipeline ini juga terintegrasi dengan SonarCloud menggunakan laporan XML dari JaCoCo untuk menganalisis kualitas kode, mendeteksi code smells, dan mengukur test coverage secara terus-menerus. Hal ini memastikan bahwa hanya kode yang berkualitas dan bugnya sedikit yang dapat dimerge ke branch utama.
    - Untuk Continuous Deployment (CD): Saya telah menerapkan mekanisme auto-deploy menggunakan layanan PaaS (Koyeb) yang terhubung langsung dengan repositori GitHub. Dengan konfigurasi pull based, setiap perubahan yang berhasil lolos tahap CI dan dimerge ke branch utama akan secara otomatis dideteksi oleh Koyeb, dibangun ulang menggunakan ```Dockerfile```, dan di-deploy ke lingkungan produksi tanpa intervensi manual, sehingga aplikasi selalu dalam kondisi terbaru.

</details>

<details>
<Summary><b>Refleksi 4</b></Summary>

1. Implementasi SOLID Principle
   - Single Responsibility Principle (SRP): Saya memisahkan CarController dari ProductController. Sebelumnya, CarController memiliki tanggung jawab double karena mengextend ProductController. Sekarang, CarController hanya menangani routing dan request HTTP yang berkaitan dengan Car, sedangkan logika ditangani oleh CarServiceImpl, dan penyimpanan data ditangani oleh repositori.
   - Open/Closed Principle (OCP): Saya menerapkan prinsip ini dengan membuat interface untuk Service dan Repository (seperti CarReadService dan CarReadRepository). Kode sekarang terbuka untuk ekstensi, namun tertutup untuk modifikasi atau tidak perlu mengubah isi CarServiceImpl jika isinya mau diubah.
   - Liskov Substitution Principle (LSP): Saya menghapus extend di bagian ```class CarController extends ProductController```. Selain itu, implementasi class seperti CarServiceImpl dan CarRepository sekarang dapat menggantikan interface mereka tanpa merusak program.
   - Interface Segregation Principle (ISP): Saya membagi interface yang besar menjadi bagian-bagian yang lebih kecil dan spesifik. CarService dipecah menjadi CarReadService dan CarWriteService dan repositori dipecah menjadi CarReadRepository dan CarWriteRepository.
   - Dependency Inversion Principle (DIP): Saya mengubah cara injeksi dependensi dari Field Injection (@Autowired pada concrete class) menjadi Constructor Injection yang bergantung pada interface. Sekarang, modul tingkat tinggi (CarController dan CarServiceImpl) bergantung pada abstraksi, bukan pada implementasi detail (modul tingkat rendah).

2. Keuntungan penerapan SOLID Principle dalam projek
   - Skalabilitas dan Ekstensibilitas yang Mudah (OCP & DIP)
      - Karena CarServiceImpl sekarang bergantung pada CarReadRepository dan CarWriteRepository, saya dapat dengan mudah mengganti dari repositori yang memakai ArrayList ke penyimpanan database di masa depan. Saya hanya perlu membuat class baru yang mengimplementasikan antarmuka tersebut tanpa memodifikasi kode di dalam CarServiceImpl.
   - Lebih mudah untuk dilakukan Unit Testing:
      - Dengan menggunakan Constructor Injection (DIP), saya bisa melakukan Unit Test pada CarController dengan sangat mudah. Saya cukup memasukkan mock object dari CarReadService ke dalam konstruktor CarController tanpa harus menyalakan seluruh framework Spring Boot.

3. Kekurangan tidak menerapkan SOLID Principle dalam projek
   - Perilaku app yang sulit diprediksi 
     - Sebelum diubah, CarController melakukan extends terhadap ProductController. Akibat extends ini, CarController secara tidak sengaja mewarisi semua endpoint URL milik produk. Hal ini bisa menyebabkan konflik routing yang aneh, di mana mengakses URL terkait mobil malah bisa memicu logika penghapusan atau pembuatan produk.
   - Ketergantungan yang kaku 
     - Saat CarController menggunakan @Autowired private CarServiceImpl carService;, kode menjadi sangat kaku. Jika sewaktu-waktu tim memutuskan untuk membuat versi layanan mobil khusus, saya harus memodifikasi langsung source code di dalam CarController. Ini melanggar prinsip Open-Closed Principle bagian tertutup untuk modifikasi dan adanya risiko untuk merusak kode yang sudah ada.
   - Ketergantungan yang berlebihan 
     - Jika kita mempunyai satu CarRepository besar, class lain yang hanya perlu mengambil data tetap harus menanggung beban dari metode write dan delete. Ini membuat pemahaman terhadap kode menjadi sulit dan setiap ada perubahan pada proses write, class tersebut yang hanya melakukan read terpaksa harus ikut dikompilasi ulang.
   
</details>

<details>
<Summary><b>Refleksi 5</b></Summary>

1. Secara keseluruhan, TDD flow yang saya ikuti cukup berguna karena memaksa saya untuk memikirkan perilaku yang diharapkan dari kode sebelum menulisnya. 
    - Berikut ini adalah hal yang telah saya lakukan:
      - Saya menulis test terlebih dahulu sebelum implementasi, sehingga tujuan kode lebih jelas. Siklus Red-Green-Refactor membantu saya fokus pada satu fitur kecil di satu waktu 
      - Test yang ada memberikan rasa aman saat melakukan refactoring karena saya bisa langsung tahu jika ada yang rusak.
    - Yang mungkin perlu diperbaiki:
      - Terkadang saya masih ingin menulis implementasi dulu baru test menyesuaikan, yang berarti test tidak benar-benar menguji perilaku melainkan hanya mengkonfirmasi kode yang sudah ada.
      - Saya juga perlu lebih fokus pada "Testing Behavior, not Implementation". Terkadang, tes yang terlalu terikat pada detail internal membuat perubahan kecil pada kode memerlukan perubahan besar pada tes.
   
2. Unit test yang udah dibuat secara umum sudah mencoba mengikuti prinsip F.I.R.S.T:
    - Fast: Tes berjalan sangat cepat karena menggunakan JUnit dan Mockito tanpa perlu menjalankan server atau database asli.
    - Independent: Setiap metode tes memiliki setUp() yang menginisialisasi ulang data (seperti products atau orders), sehingga hasil satu tes tidak memengaruhi tes lainnya.
    - Repeatable: Tes memberikan hasil yang sama di lingkungan manapun karena tidak bergantung pada faktor eksternal seperti koneksi internet.
    - Self-Validating: Tes menggunakan assertions yang secara otomatis menentukan lulus atau gagal tanpa perlu pengecekan manual.
    - Timely: Karena menggunakan alur TDD, tes dibuat sebelum kode implementasi, yang merupakan inti dari prinsip ini.


</details>