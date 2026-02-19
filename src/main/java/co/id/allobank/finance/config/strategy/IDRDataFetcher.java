package co.id.allobank.finance.config.strategy;

public interface IDRDataFetcher {

    String getResourceType();

    Object fetchData();
}
