package farrell.test.bs.section3;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests soft 20 (A,9) vs dealer Ace which should STAY.
 */
public class Test_A9_A_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(1, Card.Suit.CLUBS)); // Ace
        myHand.hit(new Card(9, Card.Suit.HEARTS));

        Card upCard = new Card(1, Card.Suit.SPADES); // Ace

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.STAY;
    }
}
