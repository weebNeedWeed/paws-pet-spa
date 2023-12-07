package com.paws.payloads.response;

import jakarta.persistence.Column;

public class PetTypeDto {
    private long id;
    private String name;

    public PetTypeDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
