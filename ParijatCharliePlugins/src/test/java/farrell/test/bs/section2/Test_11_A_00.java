package farrell.test.bs.section2;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests 11 vs dealer Ace which should HIT.
 */
public class Test_11_A_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(5, Card.Suit.CLUBS));
        myHand.hit(new Card(6, Card.Suit.DIAMONDS));

        Card upCard = new Card(1, Card.Suit.SPADES); // Ace

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.HIT;
    }
}
