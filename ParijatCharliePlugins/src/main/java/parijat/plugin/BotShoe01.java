package parijat.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;

/**
 * This class implements a deterministic shoe for bot playtesting.
 * It encodes the six games from Lab 07 Appendix 2.
 * @author Parijat
 */
public class BotShoe01 extends Shoe {
    /**
     * Initializes the shoe with a fixed card sequence.
     */
    @Override
    public void init() {
        cards.clear();
        index = 0;

        // Game 1
        add(13); add(10); add(10); add(13); add(6); add(5); add(10); add(7); add(2); add(4);
        // Game 2
        add(6); add(10); add(10); add(1); add(5); add(7); add(8); add(5); add(9); add(5);
        // Game 3
        add(9); add(10); add(5); add(7); add(7); add(8); add(6); add(7); add(4); add(10); add(3);
        // Game 4
        add(10); add(10); add(5); add(6); add(6); add(7); add(9); add(8); add(10); add(5); add(4);
        // Game 5
        add(10); add(10); add(5); add(6); add(9); add(10); add(6); add(4); add(7); add(8);
        // Game 6
        add(2); add(10); add(10); add(10); add(2); add(9); add(6); add(7); add(3); add(10); add(8);
    }

    /**
     * Adds a card rank with a fixed suit.
     * @param rank Card rank
     */
    private void add(int rank) {
        cards.add(new Card(rank, Card.Suit.SPADES));
    }
}
