package org.example.specification;


public interface Expression {
    String expression();

    static String equal() {
        return " = ";
    }

    static String notEqual() {
        return " <> ";
    }

    static String gt() {
        return " > ";
    }

    static String gte() {
        return " >= ";
    }

    static String like() {
        return " LIKE ";
    }

    static String lt() {
        return " < ";
    }

    static String lte() {
        return " <= ";
    }

    static String between() {
        return " BETWEEN ";
    }

    static String in() {
        return " in ";
    }

    static String isNULL() {
        return " is null";
    }

    static String isNotNull() {
        return " is not null";
    }

    static String join() {
        return " JOIN ";
    }

    static String rightJoin() {
        return " RIGHT JOIN ";
    }

    static String leftJoin() {
        return " LEFT JOIN ";
    }

    static String on() {
        return " ON ";
    }

    static String and() {
        return " AND ";
    }

    static String or() {
        return " OR ";
    }
}
