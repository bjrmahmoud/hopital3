package ma.emsi.hopital3.repository;


import ma.emsi.hopital3.entities.Patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


//spring data
public interface PatientRepository extends JpaRepository<Patient,Long> {
//pageable pour la pagination on peut transmettre le num et size
    Page<Patient>findByNomContains(String keyword, Pageable pageable);

    @Query("select p from Patient p where p.nom like :x")
    Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);
}


