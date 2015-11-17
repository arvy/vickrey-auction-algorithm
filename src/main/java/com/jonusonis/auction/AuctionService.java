package com.jonusonis.auction;

import java.math.BigDecimal;
import java.util.Stack;

/**
 * Created by Arvydas on 11/16/15.
 */
public class AuctionService {

    private BigDecimal reserve;

    private Stack<Winner> bidderStack;

    private Winner current;

    private volatile boolean isClosed = false;

    public AuctionService(BigDecimal reserve) {
        this.reserve = reserve;
        this.bidderStack = new Stack<>();
    }

    /**
     * Submits a bid
     * @param bidder Name of bidder
     * @param newBid bid
     */
    public synchronized void bid(String bidder, BigDecimal newBid){
        if( isClosed ) throw new AuctionClosedException();

        if( bidderStack.isEmpty() && newBid.compareTo(reserve) >= 0 ){
            bidderStack.push(new Winner(newBid, reserve, bidder)); //first bidder should win auction @ reserve price
        }else if( !bidderStack.isEmpty() && newBid.compareTo(bidderStack.peek().getBid()) > 0 ){
            bidderStack.push(new Winner(newBid, bidderStack.peek().getBid(), bidder));
        }
    }

    /**
     * Closes this auction and produces a result.
     * @return winner or null if reserve not met
     */
    public Winner close(){

        if( isClosed ) throw new AuctionClosedException();

        synchronized (AuctionService.class){
            if( !isClosed ){
                isClosed = true;
            }
        }


        if( bidderStack.isEmpty() ){
            return null;
        }else{
            Winner winner = bidderStack.pop();
            //if same bidder has multiple winning positions, make sure to get the lowest
            while( !bidderStack.isEmpty() && winner.getName().equals(bidderStack.peek().getName()) ){
                winner = bidderStack.pop();
            }
            return winner;
        }
    }
}
