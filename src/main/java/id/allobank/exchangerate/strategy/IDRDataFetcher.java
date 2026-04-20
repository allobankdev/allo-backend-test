package id.allobank.exchangerate.strategy;

public interface IDRDataFetcher<T> {
    String getType();
    T fetch();
}