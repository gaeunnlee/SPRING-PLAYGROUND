package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 이 클래스를 스프링 MVC의 컨트롤러로 등록한다.
// (스프링 빈으로 등록되며, 웹 요청을 처리할 수 있다)
@Controller
public class HelloController {
    // /hello 경로로 GET 요청이 들어오면 이 메서드가 실행된다.
    @GetMapping("hello")
    public String hello(Model model) {
        // Model 객체에 데이터를 담아 뷰(template)로 전달할 수 있다.
        model.addAttribute("data", "hello!!!");
        // 뷰 리졸버(ViewResolver)가 resources/templates/hello.html을 찾아서 렌더링한다.
        return "hello";
    }
}
