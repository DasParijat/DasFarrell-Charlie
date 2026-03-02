/*
 * Copyright (c) 2026 Hexant, LLC
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package farrell.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;

/**
 * This class is an incomplete starter implementation of the Basic Strategy.
 * <p>It is table-driven, missing most of the rules and all validation.
 * @author Ron.Coleman
 */
public class BasicStrategy {
    // These help make table formatting compact to look like the pocket card.
    public final static Play P = Play.SPLIT;
    public final static Play H = Play.HIT;
    public final static Play S = Play.STAY;
    public final static Play D = Play.DOUBLE_DOWN;

    /** Rules for section 1; see Instructional Services (2000) pocket card */
    Play[][] section1Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* 21 */ { S, S, S, S, S, S, S, S, S, S },
            /* 20 */ { S, S, S, S, S, S, S, S, S, S },
            /* 19 */ { S, S, S, S, S, S, S, S, S, S },
            /* 18 */ { S, S, S, S, S, S, S, S, S, S },
            /* 17 */ { S, S, S, S, S, S, S, S, S, S },
            /* 16 */ { S, S, S, S, S, H, H, H, H, H },
            /* 15 */ { S, S, S, S, S, H, H, H, H, H },
            /* 14 */ { S, S, S, S, S, H, H, H, H, H },
            /* 13 */ { S, S, S, S, S, H, H, H, H, H },
            /* 12 */ { H, H, S, S, S, H, H, H, H, H }
    };

    /** Rules for section 2; see Instructional Services (2000) pocket card */
    Play[][] section2Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* 11 */ { D, D, D, D, D, D, D, D, D, H },
            /* 10 */ { D, D, D, D, D, D, D, D, H, H },
            /* 9  */ { H, D, D, D, D, H, H, H, H, H },
            /* 8  */ { H, H, H, H, H, H, H, H, H, H },
            /* 7  */ { H, H, H, H, H, H, H, H, H, H },
            /* 6  */ { H, H, H, H, H, H, H, H, H, H },
            /* 5  */ { H, H, H, H, H, H, H, H, H, H }
    };

    /** Rules for section 3; see Instructional Services (2000) pocket card */
    Play[][] section3Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* A,9 */ { S, S, S, S, S, S, S, S, S, S },
            /* A,8 */ { S, S, S, S, S, S, S, S, S, S },
            /* A,7 */ { S, D, D, D, D, S, S, H, H, H },
            /* A,6 */ { H, D, D, D, D, H, H, H, H, H },
            /* A,5 */ { H, H, D, D, D, H, H, H, H, H },
            /* A,4 */ { H, H, D, D, D, H, H, H, H, H },
            /* A,3 */ { H, H, D, D, D, H, H, H, H, H },
            /* A,2 */ { H, H, D, D, D, H, H, H, H, H }
    };

    /** Rules for section 4; see Instructional Services (2000) pocket card */
    Play[][] section4Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */

            /* A,A */ { P,P,P,P,P,P,P,P,P,P },
            /* T,T */ { S,S,S,S,S,S,S,S,S,S },
            /* 9,9 */ { P,P,P,P,P,S,P,P,S,S },
            /* 8,8 */ { P,P,P,P,P,P,P,P,P,P },
            /* 7,7 */ { P,P,P,P,P,P,H,H,H,H },
            /* 6,6 */ { P,P,P,P,P,H,H,H,H,H },
            /* 5,5 */ { D,D,D,D,D,D,D,D,H,H },
            /* 4,4 */ { H,H,H,P,P,H,H,H,H,H },
            /* 3,3 */ { P,P,P,P,P,P,H,H,H,H },
            /* 2,2 */ { P,P,P,P,P,P,H,H,H,H }
    };

    /**
     * Gets the play for player's hand vs. dealer up-card.
     * @param hand Hand player hand
     * @param upCard Dealer up-card
     * @return Play based on basic strategy
     */
    public Play getPlay(Hand hand, Card upCard) {
        if(!isValid(hand, upCard))
            throw new IllegalArgumentException("Invalid hand or up-card");

        if(hand.isBlackjack())
            return Play.STAY;

        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        if(hand.isPair()) {
            return doSection4(hand,upCard);
        }
        else if(hand.size() == 2 && (card1.getRank() == Card.ACE || card2.getRank() == Card.ACE)) {
            return doSection3(hand,upCard);
        }
        else if(hand.getValue() >=5 && hand.getValue() < 12) {
            return doSection2(hand,upCard);
        }
        else if(hand.getValue() >= 12)
            return doSection1(hand,upCard);

        return Play.NONE;
    }

    /**
     * Does section 1 processing of the basic strategy, 12-21 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection1(Hand hand, Card upCard) {
        int value = hand.getValue();
        int colIndex = -1;

        // Section 1 is only for hard 12–21
        if(value < 12 || value > 21)
            return Play.NONE;

        // Row index:
        // 21 is row 0
        // 20 is row 1
        // ...
        // 12 is row 9
        int rowIndex = 21 - value;
        Play[] row = section1Rules[rowIndex];

        // Determine column index
        if(upCard.isAce()) {
            colIndex = 9; // Ace column
        }
        else if(upCard.isFace()) {
            colIndex = 8; // Ten column (T)
        }
        else {
            colIndex = upCard.getRank() - 2; // 2–9
        }

        return row[colIndex];
    }

    /**
     * Does section 2 processing of the basic strategy, 5-11 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection2(Hand hand, Card upCard) {
        int value = hand.getValue();
        int colIndex;

        if(value < 5 || value > 11)
            return Play.NONE;

        // 11 is row 0
        // 10 is row 1
        // ...
        // 5 is row 6
        int rowIndex = 11 - value;
        Play[] row = section2Rules[rowIndex];

        if(upCard.isAce()) {
            colIndex = 9;
        }
        else if(upCard.isFace()) {
            colIndex = 8;
        }
        else {
            colIndex = upCard.getRank() - 2;
        }

        Play play = row[colIndex];

        // Double only allowed on first two cards
        if(play == D && hand.size() != 2)
            return H;

        return play;
    }

    /**
     * Does section 3 processing of the basic strategy, soft 13-20 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection3(Hand hand, Card upCard) {

        int value = hand.getValue();
        int colIndex;

        // Soft totals range 13–20
        if(value < 13 || value > 20)
            return Play.NONE;

        // 20 (A,9) is row 0
        // 19 (A,8) is row 1
        // ...
        // 13 (A,2) is row 7
        int rowIndex = 20 - value;
        Play[] row = section3Rules[rowIndex];

        if(upCard.isAce()) {
            colIndex = 9;
        }
        else if(upCard.isFace()) {
            colIndex = 8;
        }
        else {
            colIndex = upCard.getRank() - 2;
        }

        Play play = row[colIndex];

        if(play == D && hand.size() != 2)
            return H;

        return play;
    }

    /**
     * Does section 4 processing of the basic strategy, pairs (2,2 through A,A) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection4(Hand hand, Card upCard) {

        int colIndex;

        if(!hand.isPair())
            return Play.NONE;

        int rank = hand.getCard(0).getRank();
        int rowIndex;

        // Determine row
        if(rank == Card.ACE)
            rowIndex = 0;
        else if(rank >= 10 && rank <= 13)
            rowIndex = 1;
        else if(rank >= 2 && rank <= 9)
            rowIndex = 10 - rank;
        else
            return Play.NONE;

        Play[] row = section4Rules[rowIndex];

        // Determine column
        if(upCard.isAce()) {
            colIndex = 9;
        }
        else if(upCard.isFace()) {
            colIndex = 8;
        }
        else {
            colIndex = upCard.getRank() - 2;
        }

        // 5,5 plays like hard 10
        if(rank == 5)
            return doSection2(hand, upCard);

        return row[colIndex];
    }

    /**
     * Validates a hand and up-card.
     * @param hand Hand
     * @param upCard Up-card
     * @return True if both are valid, false otherwise
     */
    boolean isValid(Hand hand, Card upCard) {
        return isValid(hand) && isValid(upCard);
    }

    /**
     * Validates a hand.
     * @param hand Hand
     * @return True if valid, false otherwise
     */
    boolean isValid(Hand hand) {
        // Hand is NOT null
        if(hand == null)
            return false;

        // Hand size is not less than 2
        if(hand.size() < 2)
            return false;

        // Hand value is not outside bounds
        int value = hand.getValue();
        if(value < 4 || value > 21)
            return false;

        // Ensure no null cards
        for(int i = 0; i < hand.size(); i++) {
            if(hand.getCard(i) == null)
                return false;
        }

        return true;
    }

    /**
     * Validates a card
     * @param card Card
     * @return True if valid, false otherwise
     */
    boolean isValid(Card card) {
        // Ensure card is NOT null
        if(card == null)
            return false;

        // Ensure card is valid rank
        int rank = card.getRank();
        return rank >= 1 && rank <= 13;
    }
}