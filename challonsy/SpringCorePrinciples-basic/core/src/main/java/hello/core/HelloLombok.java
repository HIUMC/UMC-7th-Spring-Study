package hello.core;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok hello = new HelloLombok();
        hello.setName("asdasd");

        String name = hello.getName();
        System.out.println(name);
    }
}
