package com.example.demo.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class Join {
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class Login {
        private String username;
        private String password;
    }

}
