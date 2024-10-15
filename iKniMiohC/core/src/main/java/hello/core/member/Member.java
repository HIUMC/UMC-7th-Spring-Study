package hello.core.member;

public class Member {

    private long id;
    private String name;
    private Grade grade;

    public Member(long id, String name, Grade grade) {
        this.name = name;
        this.id = id;
        this.grade = grade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
