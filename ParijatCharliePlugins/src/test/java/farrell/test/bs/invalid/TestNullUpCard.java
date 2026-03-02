package farrell.test.bs.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests behavior when the dealer up-card is null.
 */
public class TestNullUpCard extends TestCase {
    public void test() {
        BasicStrategy strategy = new BasicStrategy();

        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(2, Card.Suit.CLUBS));
        myHand.hit(new Card(10, Card.Suit.HEARTS));

        try {
            strategy.getPlay(myHand, null);
            fail("Expected exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }
}
