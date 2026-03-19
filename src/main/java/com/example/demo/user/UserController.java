package com.example.demo.user;

import com.example.demo._core.handler.ex.Exception401;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.example.demo.user.UserRequest.JoinDTO;
import com.example.demo.user.UserRequest.LoginDTO;
import com.example.demo.user.UserRequest.UpdateDTO;

@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    public UserController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(@Valid UserRequest.JoinDTO joinDTO, BindingResult bindingResult) {
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(@Valid UserRequest.LoginDTO loginDTO, BindingResult bindingResult) {
        User sessionUser = userService.login(loginDTO);
        session.setAttribute("sessionUser", sessionUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/update-form")
    public String updateForm(org.springframework.ui.Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("로그인이 필요합니다.");
        }
        model.addAttribute("user", new UserResponse.UpdateFormDTO(sessionUser));
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(@Valid UserRequest.UpdateDTO updateDTO, BindingResult bindingResult) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("로그인이 필요합니다.");
        }
        User user = userService.회원정보수정(sessionUser.getId(), updateDTO);
        session.setAttribute("sessionUser", user);
        return "redirect:/";
    }

    @PostMapping("/user/withdraw")
    public String withdraw() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("로그인이 필요합니다.");
        }
        userService.회원탈퇴(sessionUser.getId());
        session.invalidate();
        return "redirect:/";
    }
}
