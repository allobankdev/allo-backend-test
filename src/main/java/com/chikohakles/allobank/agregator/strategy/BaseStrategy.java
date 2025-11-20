package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;

public interface BaseStrategy {
    ResourceType getResourceType();
    Object getData();
}
