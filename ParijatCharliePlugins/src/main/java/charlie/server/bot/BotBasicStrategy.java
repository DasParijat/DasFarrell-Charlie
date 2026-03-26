package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import farrell.client.BasicStrategy;

/**
 * This class implements a bot-safe basic strategy.
 * It prevents IBot from requesting split plays.
 * @author Christian
 */
public class BotBasicStrategy extends BasicStrategy {
    /**
     * Gets the play for bot hand and dealer up-card.
     * If the base strategy suggests split, this method remaps to
     * a non-split play so Dealer does not throw an exception.
     * @param hand Bot hand
     * @param upCard Dealer up-card
     * @return A valid non-split play
     */
    @Override
    public Play getPlay(Hand hand, Card upCard) {
        Play play = super.getPlay(hand, upCard);
        if (play != Play.SPLIT) {
            return play;
        }

        // Charlie's dealer rejects bot splits, so fall back to hard-total logic.
        int value = hand.getValue();
        Play fallback = value >= 12 ? doSection1(hand, upCard) : doSection2(hand, upCard);

        return fallback == Play.NONE ? Play.HIT : fallback;
    }
}
