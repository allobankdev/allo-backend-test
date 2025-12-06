package com.bank.allo.usecase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UseCaseExecutorImplTest {

    private final UseCaseExecutorImpl executor = new UseCaseExecutorImpl();

    static class DummyUseCase extends UseCase<DummyUseCase.In, DummyUseCase.Out> {
        @lombok.Value @lombok.Builder public static class In implements UseCase.InputValues { String value; }
        @lombok.Value @lombok.Builder public static class Out implements UseCase.OutputValues { String result; }

        @Override
        public Out execute(In input) {
            return Out.builder().result("hello:" + input.getValue()).build();
        }
    }

    @Test
    void execute_appliesMapper() {
        DummyUseCase uc = new DummyUseCase();
        DummyUseCase.In in = DummyUseCase.In.builder().value("x").build();

        String out = executor.execute(uc, in, (o) -> "mapped:" + o.getResult());

        assertEquals("mapped:hello:x", out);
    }
}
