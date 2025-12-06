package com.bank.allo.usecase.idr;

public interface IdrDataFetcher {
    String resourceType();
    Object fetch();
}
