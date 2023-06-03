package africa.semicolon.regcrow.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Payment payment;
    private Long sellerId;
    private Long buyerId;
    private String description;
    private LocalDateTime createdAt;
    private Status status;
}
