package toyproject.hongikhospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.hongikhospital.domain.Doctor;
import toyproject.hongikhospital.domain.Patient;
import toyproject.hongikhospital.repository.PatientRepository;
import toyproject.hongikhospital.service.PatientService;

@Controller
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    //환자 생성
    @GetMapping(value = "/patients/new")
    public String newPatient(Model model) {
        model.addAttribute("patient", new PatientForm());
        return "patients/new";
    }

    @PostMapping(value = "/patients/new")
    public String savePatient(@Valid PatientForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "patient/new";
        }

        Patient patient=new Patient();
        patient.setName(form.getName());
        patient.setAge(form.getAge());
        patient.setGender(form.getGender());

        patientService.join(patient);

        return "redirect:/";
    }
}
