package farrell.test.bs.section3;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests soft 17 (A,6) vs dealer 7 which should HIT.
 */
public class Test_A6_7_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(1, Card.Suit.CLUBS)); // Ace
        myHand.hit(new Card(6, Card.Suit.HEARTS));

        Card upCard = new Card(7, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.HIT;
    }
}
