package farrell.test.bs.invalid;

import farrell.client.BasicStrategy;
import junit.framework.TestCase;
import charlie.card.Card;

/**
 * Tests behavior when the player hand is null.
 * BasicStrategy should throw an exception
 * when a null hand is provided.
 * This validates defensive programming.
 *
 * @author Christian Farrell
 */
public class TestNullHand extends TestCase {

    /**
     * Ensures getPlay throws an exception
     * when hand is null.
     */
    public void test() {

        BasicStrategy strategy = new BasicStrategy();

        try {
            strategy.getPlay(null, new Card(5, Card.Suit.CLUBS));
            fail("Expected exception was not thrown.");
        } catch (Exception e) {
            assert true;
        }
    }
}