package toyproject.hongikhospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.hongikhospital.domain.Doctor;
import toyproject.hongikhospital.service.DoctorService;

@Controller
@RequiredArgsConstructor
public class DoctorController {
    public final DoctorService doctorService;

    //의사등록 폼 보여줌
    @GetMapping(value = "/doctors/new")
    public String newDoctor(Model model) {
        // 빈 DoctorForm 객체를 만들어서 "doctor"라는 이름으로 모델에 추가
        model.addAttribute("doctor", new DoctorForm());
        // "doctors/new" 뷰를 반환하여 폼을 렌더링
        return "doctors/new";
    }

    //의사등록
    @PostMapping(value = "/doctors/new")
    public String saveDoctor(@Valid DoctorForm form, BindingResult result) {
        // 폼 데이터가 유효하지 않으면 다시 폼 페이지로 돌아감
        if (result.hasErrors()) {
            return "doctors/new";
        }
        // 유효한 경우, 폼 데이터를 기반으로 새로운 의사 객체 생성
        Doctor doctor=new Doctor();
        doctor.setName(form.getName());
        doctor.setExperience(form.getExperience());
        doctor.setDepartment(form.getDepartment());

        doctorService.join(doctor);

        return "redirect:/";
    }

    //id로 의사 조회
    @GetMapping("/doctors/{id}")
    public String getDoctor(@PathVariable Long id, Model model) {
        Doctor doctor=doctorService.find(id);
        if(doctor==null){
            return "error/404";
        }

        model.addAttribute("doctor", doctor);
        return "doctors/view";
    }

}
