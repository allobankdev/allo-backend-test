package com.allobankdev.exchangrate.service.strategy;

public interface IdrDataFetcher {
    String getType();
    Object fetch();
}
