package farrell.test.bs.section4;

import farrell.client.BasicStrategy;
import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;

/**
 * Tests a pair of 8s versus dealer 6.
 * Basic strategy dictates that 8,8 vs 6 should SPLIT.
 * This ensures pair-hand logic is handled correctly.
 *
 * @author Christian Farrell
 */
public class Test_88_6_00 extends TestCase {

    /**
     * Verifies that a pair of 8s against 6 results in SPLIT.
     */
    public void test() {

        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(8, Card.Suit.CLUBS));
        myHand.hit(new Card(8, Card.Suit.HEARTS));

        Card upCard = new Card(6, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();

        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.SPLIT;
    }
}