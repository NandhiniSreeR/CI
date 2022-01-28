package com.tw.bootcamp.bookshop.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class AddressErrorResponse {
    private final String errorMessage;
    private final Map<String, String > errors;

    public AddressErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
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

