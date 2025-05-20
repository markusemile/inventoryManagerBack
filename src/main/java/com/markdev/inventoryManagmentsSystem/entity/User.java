package com.markdev.inventoryManagmentsSystem.entity;

import com.markdev.inventoryManagmentsSystem.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Mail is required")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Phone number is required")
    @Column(name="phone_number")
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToMany(mappedBy = "id")
    private List<Transaction> transactions;

    @Column(name="create_at")
    private final LocalDateTime createAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", createAt=" + createAt +
                '}';
    }
}
