package com.proyekoprec.monsterpanicbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Anotasi ini menandakan bahwa ini adalah aplikasi Spring Boot
// dan akan secara otomatis mengkonfigurasi banyak hal untuk Anda.
@SpringBootApplication
public class MonsterPanicBackendApplication {

    // Ini adalah metode main standar Java, yang menjadi titik awal
    // eksekusi program. SpringApplication.run() akan memulai server
    // dan semua komponen backend Anda.
    public static void main(String[] args) {
        SpringApplication.run(MonsterPanicBackendApplication.class, args);
    }

}
