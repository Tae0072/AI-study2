package com.example.demo.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo._core.utils.Resp;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
public class UserApiController {

    private final UserService userService;
    private final HttpSession session;

    public UserApiController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/api/username-same-check")
    public ResponseEntity<?> usernameSameCheck(@RequestParam("username") String username) {
        boolean isDuplicated = userService.usernameSameCheck(username);
        if (isDuplicated) {
            return Resp.fail(HttpStatus.BAD_REQUEST, "중복된 아이디입니다.");
        }
        return Resp.ok(true);
    }

}
