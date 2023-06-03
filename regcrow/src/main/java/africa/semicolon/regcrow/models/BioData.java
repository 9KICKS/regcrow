package africa.semicolon.regcrow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BioData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;

//    @Override
//    public boolean equals(Object object) {
//        if (object == null) return false;
//        if (this.getClass() != object.getClass()) return false;
//        BioData other = ((BioData) object);
//        if (this.email != null && getEmail() == null) return false;
//        assert this.email != null;
//        return this.email.equals(other.getEmail());
//    }
//
//    @Override
//    public int hashcode() {
//        return Objects.hash(this.email);
//    }
}
