package achlaq.co.allo_backend_test.finance.strategy;

public interface IdrDataFetcher {

    void load();

    Object getCachedData();
}
