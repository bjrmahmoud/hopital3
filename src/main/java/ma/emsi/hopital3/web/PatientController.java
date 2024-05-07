package ma.emsi.hopital3.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.emsi.hopital3.entities.Patient;
import ma.emsi.hopital3.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

//    public PatientController(PatientRepository patientRepository) {
//        this.patientRepository = patientRepository;
//    }
//methode qui retourne une vue patients.html
@GetMapping( "/user/index")
public String index(Model model,
                    @RequestParam(name = "page",defaultValue = "0") int page,
                    @RequestParam(name = "size",defaultValue = "4") int size,
                    @RequestParam(name = "keyword",defaultValue = "") String keyword){
//    Page<Patient> pagePatients = patientRepository.findAll(PageRequest.of(page,size));
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
    model.addAttribute("listPatient", pagePatients.getContent());
    model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
    model.addAttribute("currentPage", page);
    model.addAttribute("keyword", keyword);
    return "patients";
   }

    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam(name="id") Long id,@RequestParam(name = "keyword",defaultValue = "")String keyword,
                         @RequestParam(name = "page",defaultValue = "0")int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/displayDetails")
    public String displayDetails(Long id, Model model){
        Patient patient = patientRepository.findById(id).orElse(null);
        model.addAttribute("patient", patient);
        return "patientDetails";
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }
    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatients(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/Save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String Save(Model model, @Valid Patient patient, BindingResult bindingResult ,
                       @RequestParam(defaultValue = "0") int page ,
                       @RequestParam(defaultValue = "") String keyword){
    if(bindingResult.hasErrors()) return "formPatients";
    patientRepository.save(patient);
//        return "formPatients";
        return  "redirect:/user/index?page" + page+ "&keyword="+keyword;
        //rendu cote serveur si on veut garder etat de app il faut transmettre des parametres dans des url etc
        //single page app
    }
    @GetMapping("/admin/editPatients")
    public String editPatients(Model model, Long id, String keyword, int page){
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient", patient);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "editPatients";
    }
//ajout validation recherche la suppression on gardant etat de app

}