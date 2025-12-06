package com.bank.allo.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.function.Function;

@Service
public class UseCaseExecutorImpl implements UseCaseExecutor {
    private static final Logger log = LoggerFactory.getLogger(UseCaseExecutorImpl.class);

    @Override
    public <RX, I extends UseCase.InputValues, O extends UseCase.OutputValues> RX execute(
            UseCase<I, O> useCase, I input, Function<O, RX> outputMapper) {
        log.info("Executing use case: {}", useCase.getClass().getSimpleName());
        O output = useCase.execute(input);
        return outputMapper.apply(output);
    }
}
