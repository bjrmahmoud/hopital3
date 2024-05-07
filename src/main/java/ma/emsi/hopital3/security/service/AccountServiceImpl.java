package ma.emsi.hopital3.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.emsi.hopital3.security.entities.AppRole;
import ma.emsi.hopital3.security.entities.AppUser;
import ma.emsi.hopital3.security.repo.AppRoleRepository;
import ma.emsi.hopital3.security.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser=appUserRepository.findByUsername(username);
        if(appUser!=null) throw new RuntimeException("this user already exist");
        if (!password.equals(confirmPassword)) throw new RuntimeException("Please confirm your password");
        AppUser user = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedAppUser = appUserRepository.save(user);
        return savedAppUser;

    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appRole != null) {
            throw new RuntimeException("Role already exists");
        }

        appRole = AppRole.builder()
                .role(role)
                .build();
        return appRoleRepository.save(appRole);
    }




    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().add(appRole);
       // appUserRepository.save(appUser); car @transactional
    }

    @Override
    public void removeRoleToUser(String username, String role) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().remove(appRole);

    }
    public  AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

}
