package com.bank.allo.usecase;

import java.util.function.Function;

public interface UseCaseExecutor {
    <RX, I extends UseCase.InputValues, O extends UseCase.OutputValues> RX execute(
            UseCase<I, O> useCase,
            I input,
            Function<O, RX> outputMapper);
}
