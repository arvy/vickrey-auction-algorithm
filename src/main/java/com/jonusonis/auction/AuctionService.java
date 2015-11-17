package com.jonusonis.auction;

import java.math.BigDecimal;

/**
 * Created by Arvydas on 11/16/15.
 */
public class AuctionService {

    private BigDecimal reserve;

    private Winner current;

    private volatile boolean isClosed = false;

    public AuctionService(BigDecimal reserve) {
        this.reserve = reserve;
    }

    /**
     * Submits a bid
     * @param bidder Name of bidder
     * @param newBid bid
     */
    public synchronized void bid(String bidder, BigDecimal newBid){
        if( isClosed ) throw new AuctionClosedException();

        if( current == null && newBid.compareTo(reserve) >= 0 ){
            current = new Winner(newBid, reserve, bidder); //first bidder should win auction @ reserve price
        }else if( current != null && newBid.compareTo(current.getBid()) > 0 ){

            current = new Winner(
                    newBid,
                    //if winning bidder submits another bid, price should remain the same
                    (current.getName().equals(bidder) ? current.getPrice() : current.getBid()),
                    bidder
            );
        }
    }

    /**
     * Closes this auction and produces a result.
     * @return winner or null if reserve not met
     */
    public Winner close(){

        if( !isClosed ){
            synchronized (AuctionService.class){
                if( !isClosed ){
                    isClosed = true;
                }
            }
        }


        return current;
    }
}
