package org.example;

import io.vertx.sqlclient.Tuple;

import java.util.Arrays;

public class MainTest {
    public static void main(String[] args) {
        Tuple tuple = Tuple.tuple(Arrays.asList(null, null, null));
        System.out.println(tuple.getValue(3));
        System.out.println(tuple.size());
    }
}
