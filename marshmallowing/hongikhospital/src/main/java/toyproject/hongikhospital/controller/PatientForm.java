package toyproject.hongikhospital.controller;

import lombok.Getter;
import lombok.Setter;
import toyproject.hongikhospital.domain.enums.Gender;

@Getter @Setter
public class PatientForm {
    private Integer id;

    private String name;
    private Gender gender;
    private Integer age;
}
