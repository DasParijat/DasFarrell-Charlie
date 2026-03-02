package farrell.test.bs.section4;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests 9,9 vs dealer 7 which should STAY.
 */
public class Test_99_7_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(9, Card.Suit.CLUBS));
        myHand.hit(new Card(9, Card.Suit.HEARTS));

        Card upCard = new Card(7, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.STAY;
    }
}
