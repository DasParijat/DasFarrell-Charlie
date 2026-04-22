package parijat.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;

public class Lab11Shoe02 extends Shoe {
    @Override
    public void init() {
        cards.clear();
        index = 0;

        // Ray spec:
        // You 10+10+P!{A,A} | Dealer 10+6
        add(10); // You first
        add(6);  // Dealer hole
        add(10); // You second
        add(10); // Dealer up
        add(Card.ACE); // Split hand 1 hit
        add(Card.ACE); // Split hand 2 hit
    }

    private void add(int rank) {
        cards.add(new Card(rank, Card.Suit.SPADES));
    }
}
