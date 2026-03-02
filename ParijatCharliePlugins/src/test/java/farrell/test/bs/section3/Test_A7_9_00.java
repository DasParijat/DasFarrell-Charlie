package farrell.test.bs.section3;

import farrell.client.BasicStrategy;
import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;

/**
 * Tests a soft 18 (A,7) versus dealer 9.
 * According to basic strategy,
 * soft 18 vs 9 should HIT.
 * This ensures that soft-hand logic
 * is properly implemented in BasicStrategy.
 *
 * @author Christian Farrell
 */
public class Test_A7_9_00 extends TestCase {

    /**
     * Verifies that soft 18 against 9 returns HIT.
     */
    public void test() {

        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(1, Card.Suit.CLUBS));  // Ace
        myHand.hit(new Card(7, Card.Suit.HEARTS));

        Card upCard = new Card(9, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();

        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.HIT;
    }
}