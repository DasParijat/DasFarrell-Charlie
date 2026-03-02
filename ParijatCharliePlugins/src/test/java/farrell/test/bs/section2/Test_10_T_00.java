package farrell.test.bs.section2;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests 10 vs dealer 10 which should HIT.
 */
public class Test_10_T_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(4, Card.Suit.CLUBS));
        myHand.hit(new Card(6, Card.Suit.HEARTS));

        Card upCard = new Card(10, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.HIT;
    }
}
