package com.mmc.datareportzuulserver.entity;


/**
 * @author csy
 * @version 1.0
 * @date 2020/7/11 15:01
 */

public class DataObj {
    private int status = 1 ;
    private String message = "ok";
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"status\"=\"" + status +
                "\", \"message\"=\"" + message   +
                "\", \"data\"=\"" + data +
                "\"}";
    }
}
