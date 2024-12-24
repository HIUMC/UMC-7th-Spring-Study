package toyproject.hongikhospital.controller;

import lombok.Getter;
import lombok.Setter;
import toyproject.hongikhospital.domain.Department;

@Getter @Setter
public class DoctorForm {

    private Integer id;

    private String name;
    private Integer experience;
    private Department department;
}
