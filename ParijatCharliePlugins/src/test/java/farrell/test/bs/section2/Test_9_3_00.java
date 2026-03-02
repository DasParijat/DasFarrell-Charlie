package farrell.test.bs.section2;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests 9 vs dealer 3 which should DOUBLE_DOWN.
 */
public class Test_9_3_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(4, Card.Suit.CLUBS));
        myHand.hit(new Card(5, Card.Suit.HEARTS));

        Card upCard = new Card(3, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.DOUBLE_DOWN;
    }
}
