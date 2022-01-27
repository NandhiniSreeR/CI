package com.tw.bootcamp.bookshop.user;

public enum Role {
    USER,
    ADMIN;

    String authority() {
        return "ROLE_" + this.name();
    }
}
