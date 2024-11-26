package umc.spring.web.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class classJoinDto {
    String name;
    Integer gender;
    Integer birthYear;
    Integer birthMonth;
    Integer birthDay;
    String address;
    String specAddress;
    List<Long> preferCategory;
}
