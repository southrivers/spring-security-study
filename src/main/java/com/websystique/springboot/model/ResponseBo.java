package com.websystique.springboot.model;

import java.util.LinkedList;
import java.util.List;

public class ResponseBo {

    String msg;
    int code;
    List<BigDemInterfaceRate> res = new LinkedList<>();


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<BigDemInterfaceRate> getRes() {
        return res;
    }

    public void setRes(List<BigDemInterfaceRate> res) {
        this.res = res;
    }
}
