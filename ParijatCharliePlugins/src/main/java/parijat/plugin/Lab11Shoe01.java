package parijat.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;

public class Lab11Shoe01 extends Shoe {
    @Override
    public void init() {
        cards.clear();
        index = 0;

        // Initial deal (no bots): You A, Dealer hole 7, You A, Dealer up 10
        add(Card.ACE);
        add(7);
        add(Card.ACE);
        add(10);

        // After splitting aces:
        // first split hand gets 10 (A+10), second split hand gets 9
        add(10);
        add(9);
    }

    private void add(int rank) {
        cards.add(new Card(rank, Card.Suit.SPADES));
    }
}
