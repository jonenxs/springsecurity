package com.nxs.Exception;

public class UserNotExistException extends RuntimeException {

    private Integer id;

    public UserNotExistException(Integer id) {
        super("user not exists");
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
