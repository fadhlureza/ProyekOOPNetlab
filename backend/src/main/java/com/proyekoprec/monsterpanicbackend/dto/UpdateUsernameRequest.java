package com.proyekoprec.monsterpanicbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUsernameRequest {
    @NotBlank(message = "Username baru tidak boleh kosong")
    @Size(min = 3, max = 10, message = "Username baru harus antara 3 dan 10 karakter")
    private String newUsername;
    
    // Getter & Setter
    public String getNewUsername() { return newUsername; }
    public void setNewUsername(String newUsername) { this.newUsername = newUsername; }
}