package ma.emsi.hopital3;

import ma.emsi.hopital3.entities.Patient;
import ma.emsi.hopital3.repository.PatientRepository;
import ma.emsi.hopital3.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class Hopital3Application {
    //utiliser interface patienrepo avec injection des dependance en utilisant auto
//    @Autowired
//    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(Hopital3Application.class, args);
    }
    /*la notation bean sa veut dire au demm execute cette methode ne sera jamais executer sa veut dire au demmarage
 execute moi cette methode quand il execute  il va recevoir un obj de type cmd line runer */
//    @Bean
    CommandLineRunner start(PatientRepository patientRepository) {
        return args -> {
            patientRepository.save(
                    new Patient(null, "mahmoud", new Date(), false, 34));
            patientRepository.save(
                    new Patient(null, "merouane", new Date(), false, 4321));
            patientRepository.save(
                    new Patient(null, "ziad", new Date(), true, 344));
            patientRepository.findAll().forEach(p -> {
                System.out.println(p.getNom());
            });
        };
    }
//    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
        PasswordEncoder passwordEncoder = passwordEncoder();
        return args -> {
            UserDetails user1 = jdbcUserDetailsManager.loadUserByUsername("merouane");
            if(user1 == null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("mahmoud")
                                .password(passwordEncoder.encode("1234"))
                                .roles("USER")
                                .build()
                );
            UserDetails user2 = jdbcUserDetailsManager.loadUserByUsername("Hamza");
            if(user2 == null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("merouane")
                                .password(passwordEncoder.encode("1234"))
                                .roles("USER")
                                .build()
                );
            UserDetails user3 = jdbcUserDetailsManager.loadUserByUsername("admin2");
            if(user3 == null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("admin2")
                                .password(passwordEncoder.encode("1234"))
                                .roles("ADMIN", "USER")
                                .build()
                );
        };
    }
    @Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService){
        return args -> {
           accountService.addNewRole("USER");
           accountService.addNewRole("ADMIN");
           accountService.addNewUser("user1","1234","user1@gmail.com","1234");
            accountService.addNewUser("user2","1234","user2@gmail.com","1234");
            accountService.addNewUser("admin","1234","admin@gmail.com","1234");

            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("user2","USER");
            accountService.addRoleToUser("admin","ADMIN");


        };
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

