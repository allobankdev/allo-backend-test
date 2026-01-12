package id.co.evan.project.aggregator.fault;

public class GeneralException extends RuntimeException {
    public GeneralException() {
        super("A general error occurred in the system.");
    }
}