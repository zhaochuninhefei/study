package com.czhao.test.jdk15.exprs;

/**
 * JEP 360: Sealed Classes (Preview)
 */
public sealed interface Expr permits ConstantExpr, NewExpr, PlusExpr {
}
