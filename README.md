# Monster Panic
```
Fadhlureza Sebastian
2306161971
```
## Deskripsi
Monster Panic adalah sebuah game reaksi berbasis web yang dirancang untuk menguji kecepatan dan akurasi pemain. Proyek ini dibangun sebagai aplikasi full-stack menggunakan frontend berbasis JavaScript dan backend RESTful API menggunakan Java Spring Boot. Game ini dilengkapi dengan sistem akun, leaderboard, pencapaian, dan fitur-fitur lainnya.

## Fitur Utama
Dua Mode Permainan:
- Aim Trainer (PC): Fokus pada akurasi dan kecepatan flick shot dengan target yang bergerak acak.
- Reaction Test (Mobile): Fokus pada kecepatan sentuhan sesaat setelah target muncul.
- Sistem Akun & Sesi:
    - Registrasi dan Login pemain.
    - Pengelolaan sesi menggunakan JSON Web Token (JWT).
    - Fitur "Bermain sebagai Tamu" tanpa perlu mendaftar.
- Kustomisasi & Pengaturan:
    - Pengaturan profil untuk mengubah username dan password.
    - Pilihan tema antarmuka (Light Mode & Dark Mode).
- Fitur Kompetitif:
    - Sistem skor dan kombo yang dinamis.
    - Leaderboard Global dengan sistem filter berdasarkan mode permainan dan tingkat kesulitan.
    - Sistem Pencapaian (Achievements) yang bisa dibuka oleh pemain.
- Gameplay Dinamis:
    - Tiga tingkat kesulitan (Mudah, Normal, Sulit).
    - Power-ups dan item jebakan yang muncul secara acak.

## Teknologi yang Digunakan
- Frontend:
    - HTML5, CSS3, JavaScript (ES6)
    - Tailwind CSS untuk styling
- Backend:
    - Java 17
    - Spring Boot 3
    - Spring Security
    - JSON Web Token (JWT) untuk autentikasi
- Database:
    - PostgreSQL
    - Neon (Serverless Postgres)

## Struktur Proyek
Proyek ini dibagi menjadi dua folder utama di dalam repository:
- /frontend: Berisi semua kode sumber untuk aplikasi frontend (file index.html, style.css, script.js).
- /backend: Berisi semua kode sumber untuk aplikasi backend Spring Boot.

## Cara Menjalankan Proyek
### Prasyarat
- Java JDK 17 atau yang lebih baru.
- Maven.
- PostgreSQL Server.
- VS Code.

### Konfigurasi Backend
- Buka file /Backend/src/main/resources/application.properties.
- Sesuaikan konfigurasi spring.datasource dengan detail koneksi database PostgreSQL Anda (lokal atau Neon).
```
spring.datasource.url=jdbc:postgresql://<host>/<database>
spring.datasource.username=<user>
spring.datasource.password=<password>
```
- Pastikan jwt.secret memiliki nilai string yang panjang (minimal 64 karakter) untuk keamanan.

### Menjalankan Backend
- Buka terminal dan navigasi ke direktori root /Backend.
- Jalankan perintah berikut:
```
mvn spring-boot:run
```
- Server backend akan berjalan di http://localhost:8080.

### Menjalankan Frontend
- Buka folder /frontend di VS Code.
- Pastikan sudah terinstall ekstensi Live Server di VS Code anda.
- Klik kanan pada file index.html.
- Pilih opsi "Open with Live Server".
- Browser akan otomatis terbuka, dan game siap dimainkan.