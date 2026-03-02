package farrell.test.bs.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests behavior when the player hand value exceeds 21.
 */
public class TestHandValueTooHigh extends TestCase {
    public void test() {
        BasicStrategy strategy = new BasicStrategy();

        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(10, Card.Suit.CLUBS));
        myHand.hit(new Card(10, Card.Suit.HEARTS));
        myHand.hit(new Card(2, Card.Suit.DIAMONDS)); // 22 total

        Card upCard = new Card(6, Card.Suit.SPADES);

        try {
            strategy.getPlay(myHand, upCard);
            fail("Expected exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }
}

