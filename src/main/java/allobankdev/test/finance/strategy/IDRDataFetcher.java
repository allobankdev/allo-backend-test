package allobankdev.test.finance.strategy;

public interface IDRDataFetcher {
    String resourceType();

    Object fetch();
}
