package com.tw.bootcamp.bookshop.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class AddressErrorResponse {
    private final String message;
    private final Map<String, String > errors;

    public AddressErrorResponse(String message) {
        this.message = message;
        this.errors = new HashMap<>();
    }

    public boolean hasAnyErrors(){
        return errors.size() > 0;
    }

    public int countOfErrors(){
        return errors.size();
    }

    public void addError(String field , String errorMessage){
        this.errors.put(field,errorMessage);
    }
}

