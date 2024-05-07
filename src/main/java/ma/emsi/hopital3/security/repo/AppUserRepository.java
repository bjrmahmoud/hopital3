package ma.emsi.hopital3.security.repo;

import ma.emsi.hopital3.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,String> {
    AppUser findByUsername(String username);
}

