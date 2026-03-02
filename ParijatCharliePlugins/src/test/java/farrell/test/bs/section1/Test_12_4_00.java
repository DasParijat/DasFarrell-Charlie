package farrell.test.bs.section1;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests 12 vs dealer 4 which should STAY.
 */
public class Test_12_4_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(2, Card.Suit.CLUBS));
        myHand.hit(new Card(10, Card.Suit.DIAMONDS));

        Card upCard = new Card(4, Card.Suit.HEARTS);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.STAY;
    }
}
