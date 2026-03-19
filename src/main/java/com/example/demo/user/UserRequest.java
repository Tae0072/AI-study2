package com.example.demo.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    public static class JoinDTO {
        @NotBlank
        @Size(min = 4, max = 20)
        private String username;

        @NotBlank
        @Size(min = 4, max = 20)
        private String password;

        @NotBlank
        @Email
        private String email;

        private String postcode;
        private String address;
        private String detailAddress;
        private String extraAddress;

        public JoinDTO() {}

        public JoinDTO(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getExtraAddress() {
            return extraAddress;
        }

        public void setExtraAddress(String extraAddress) {
            this.extraAddress = extraAddress;
        }

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .postcode(postcode)
                    .address(address)
                    .detailAddress(detailAddress)
                    .extraAddress(extraAddress)
                    .build();
        }
    }

    public static class UpdateDTO {
        @NotBlank
        @Size(min = 4, max = 20)
        private String password;

        @NotBlank
        @Email
        private String email;

        private String postcode;
        private String address;
        private String detailAddress;
        private String extraAddress;

        public UpdateDTO() {}

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getExtraAddress() {
            return extraAddress;
        }

        public void setExtraAddress(String extraAddress) {
            this.extraAddress = extraAddress;
        }
    }

    public static class LoginDTO {
        @NotBlank
        private String username;
        @NotBlank
        private String password;

        public LoginDTO() {}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
