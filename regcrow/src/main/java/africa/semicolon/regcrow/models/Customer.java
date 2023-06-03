package africa.semicolon.regcrow.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BioData bioData;
    private String firstName;
    private String lastName;
    @OneToOne
    private BankAccount bankAccount;
    private String profileImage;
    private LocalDateTime timeCreated;

    @PrePersist
    public void setTimeCreated() {
        timeCreated = LocalDateTime.now();
    }
}
