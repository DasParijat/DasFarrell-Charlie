package farrell.test.bs.section1;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests 13 vs dealer 6 which should STAY.
 */
public class Test_13_6_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(3, Card.Suit.CLUBS));
        myHand.hit(new Card(10, Card.Suit.HEARTS));

        Card upCard = new Card(6, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.STAY;
    }
}
