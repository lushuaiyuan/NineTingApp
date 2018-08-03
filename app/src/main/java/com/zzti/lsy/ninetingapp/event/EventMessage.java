package com.zzti.lsy.ninetingapp.event;

/**
 * author：anxin on 2018/8/3 16:32
 */
public class EventMessage<T> {
    private T data;
    private int eventCode = -1;

    public EventMessage(int eventCode) {
        this.eventCode = eventCode;
    }

    public EventMessage(int eventCode, T paramT) {
        this.eventCode = eventCode;
        this.data = paramT;
    }

    public T getData() {
        return (T) this.data;
    }

    public int getEventCode() {
        return this.eventCode;
    }
}
