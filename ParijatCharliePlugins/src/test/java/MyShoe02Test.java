import charlie.card.Card;
import charlie.plugin.IShoe;
import junit.framework.TestCase;
import parijat.plugin.MyShoe02;


public class MyShoe02Test extends TestCase {
    public void test() {
        // Instantiate a shoe
        IShoe shoe = new MyShoe02();

        // Assert shoe has 10 cards
        assert shoe.size() == 10;

        // Get first card
        Card firstCard = shoe.next();

        // Assert it's the card we expect
        assert firstCard.getRank() == 2;

        // Get second card
        Card secondCard = shoe.next();

        // Assert it's the card we expect
        assert secondCard.getRank() == Card.QUEEN;
    }
}
