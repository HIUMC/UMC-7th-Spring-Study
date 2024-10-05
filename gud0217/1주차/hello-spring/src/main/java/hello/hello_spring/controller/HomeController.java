package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // 8080
    public String home() {
        return "home"; // 템플릿에서 home.html이 호출됨
    }
}
