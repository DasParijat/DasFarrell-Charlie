package farrell.test.bs.section4;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import farrell.client.BasicStrategy;
import junit.framework.TestCase;

/**
 * Tests A,A vs dealer 9 which should SPLIT.
 */
public class Test_AA_9_00 extends TestCase {
    public void test() {
        Hand myHand = new Hand(new Hid(Seat.YOU));

        myHand.hit(new Card(1, Card.Suit.CLUBS)); // Ace
        myHand.hit(new Card(1, Card.Suit.HEARTS)); // Ace

        Card upCard = new Card(9, Card.Suit.SPADES);

        BasicStrategy strategy = new BasicStrategy();
        Play play = strategy.getPlay(myHand, upCard);

        assert play == Play.SPLIT;
    }
}
