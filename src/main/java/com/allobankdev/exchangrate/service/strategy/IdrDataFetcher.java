package com.allobankdev.exchangrate.service.strategy;

import com.allobankdev.exchangrate.constant.ResourceType;

public interface IdrDataFetcher {
    ResourceType getType();
    Object fetch();
}
