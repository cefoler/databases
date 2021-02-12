package com.celeste.sql.function;

import java.sql.SQLException;
import java.util.function.Function;

public interface SqlFunction<I, O> extends Function<I, O> {

    O applyThrowing(I input) throws SQLException;

    @Override
    default O apply(I input) {
        try {
            return applyThrowing(input);
        } catch (Exception e) {
            return null;
        }
    }

}
