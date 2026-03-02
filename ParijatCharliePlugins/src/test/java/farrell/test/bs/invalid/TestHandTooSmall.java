package farrell.test.bs.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests behavior when the player hand has fewer than 2 cards.
 */
public class TestHandTooSmall extends TestCase {
    public void test() {
        BasicStrategy strategy = new BasicStrategy();

        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(10, Card.Suit.CLUBS)); // only one card

        Card upCard = new Card(6, Card.Suit.HEARTS);

        try {
            strategy.getPlay(myHand, upCard);
            fail("Expected exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }
}
