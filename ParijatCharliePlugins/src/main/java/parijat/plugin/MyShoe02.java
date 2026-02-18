package parijat.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;

/**
 * Create an instance of MyShoe02.
 * A unique shoe to result in a 5 card Charlie if following standard play
 */
public class MyShoe02 extends Shoe {
    public MyShoe02() {
        // Constructor calls init()
        super();
        init();
    }

    @Override
    public void init() {
        // Clear cards
        cards.clear();

        // Add specified cards
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(Card.QUEEN, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.HEARTS));
        cards.add(new Card(7, Card.Suit.CLUBS));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(5, Card.Suit.CLUBS));
        cards.add(new Card(6, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.KING, Card.Suit.CLUBS));
        cards.add(new Card(9, Card.Suit.DIAMONDS));
        cards.add(new Card(Card.JACK, Card.Suit.CLUBS));
    }
}
