package com.jonusonis.auction;

import java.math.BigDecimal;

/**
 * Created by Arvydas on 11/16/15.
 */
public class Winner {
    private final BigDecimal price;
    private final BigDecimal bid;
    private final String name;

    public Winner(BigDecimal bid, BigDecimal price, String name) {
        this.bid = bid;
        this.price = price;
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public String getName() {
        return name;
    }
}
