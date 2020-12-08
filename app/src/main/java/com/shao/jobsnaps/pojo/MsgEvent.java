package com.shao.jobsnaps.pojo;

/**
 * 事件包装pojo
 * Created by shaoduo on 2017-07-20.
 */

public class MsgEvent {
    private byte code ;
    private String msg ;
    private Object data ;

    public MsgEvent() {

    }
    public MsgEvent(byte code, String msg,Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
