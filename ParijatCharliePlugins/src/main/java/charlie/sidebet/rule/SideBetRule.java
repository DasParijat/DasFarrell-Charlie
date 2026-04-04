/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.sidebet.rule;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.ISideBetRule;
import org.apache.log4j.Logger;

/**
 * This class implements side bet rules: Super 7, suited Royal Match, and Exactly 13.
 * @author Ron Coleman
 */
public class SideBetRule implements ISideBetRule {
    private final Logger LOG = Logger.getLogger(SideBetRule.class);
    
    private final double PAYOFF_SUPER7 = 3.0;
    private final double PAYOFF_SUITED_ROYAL_MATCH = 25.0;
    private final double PAYOFF_EXACTLY_13 = 1.0;

    /**
     * Apply rule to the hand and return the payout if the rule matches
     * and the negative bet if the rule does not match.
     * @param hand Hand to analyze.
     * @return 
     */
    @Override
    public double apply(Hand hand) {

        
        Double bet = hand.getHid().getSideAmt();
        LOG.info("side bet amount = "+bet);
        
        if(bet == 0)
            return 0.0;
        
        LOG.info("side bet rule applying hand = "+hand);
        
        double best = 0.0;
        
        Card c0 = hand.getCard(0);
        if(c0 != null && c0.getRank() == 7) {
            LOG.info("side bet SUPER 7 matches");
            best = Math.max(best, bet * PAYOFF_SUPER7);
        }
        
        if(hand.size() >= 2) {
            Card c1 = hand.getCard(1);
            if(suitedRoyalMatch(c0, c1)) {
                LOG.info("side bet suited ROYAL MATCH matches");
                best = Math.max(best, bet * PAYOFF_SUITED_ROYAL_MATCH);
            }
            if(exactly13(c0, c1)) {
                LOG.info("side bet EXACTLY 13 matches");
                best = Math.max(best, bet * PAYOFF_EXACTLY_13);
            }
        }
        
        if(best > 0.0)
            return best;
        
        LOG.info("side bet rule no match");
        
        return -bet;
    }
    
    /** King and Queen of the same suit on the first two cards. */
    private static boolean suitedRoyalMatch(Card c0, Card c1) {
        if(c0 == null || c1 == null)
            return false;
        if(c0.getSuit() != c1.getSuit())
            return false;
        int r0 = c0.getRank();
        int r1 = c1.getRank();
        return (r0 == Card.KING && r1 == Card.QUEEN)
                || (r0 == Card.QUEEN && r1 == Card.KING);
    }
    
    /** First two cards total 13 in blackjack value (ace may count as 1 or 11). */
    private static boolean exactly13(Card c0, Card c1) {
        int lit = c0.value() + c1.value();
        if(lit == 13)
            return true;
        int soft = lit;
        if(c0.isAce())
            soft += 10;
        else if(c1.isAce())
            soft += 10;
        return soft == 13;
    }
}
