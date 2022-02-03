package com.tom.redis.service;

public interface IPubService {

    public void publish(String topic, String message);
}
