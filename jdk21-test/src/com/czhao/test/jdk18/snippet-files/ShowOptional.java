package com.czhao.test.jdk18;

import java.util.Optional;

/**
 * @author zhaochun
 */
public class ShowOptional {
    void show(Optional<String> v) {
        // @start region="example"
        if (v.isPresent()) {
            System.out.println("v: " + v.get());
        }
        // @end
    }
}
