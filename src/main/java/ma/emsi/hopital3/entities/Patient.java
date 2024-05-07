package ma.emsi.hopital3.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity // anotation jpa
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //anotation de validation
    @NotEmpty
    @Size(min = 3, max = 40)
    private String nom;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "YYY-MM-dd")
    private Date dateNaissance;
    private boolean malade;
    @DecimalMin("1")
    private int score;
    /*pour faire la validation on a besoin de 3 chose
      premieres ajouter la dependance  spring boot validation
             2eme chose ajouter les anotations des validations
              la 3eme au niveau du controleur */
}
