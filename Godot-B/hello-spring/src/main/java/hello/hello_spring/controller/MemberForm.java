package hello.hello_spring.controller;

// ❓ 스프링이 MemberForm의 세터에 접근하는 원리가 뭐지
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    //spring이 접근하는 세터(set~)
    public void setName(String name) {
        this.name = name;
    }
}
