package parijat.server;

import charlie.actor.House;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.plugin.IPlayer;
import charlie.util.Play;
import org.apache.log4j.Logger;

public class MyDealer extends Dealer {
    private final Logger LOG = Logger.getLogger(MyDealer.class);

    public MyDealer(House house) {
        super(house);
    }

    @Override
    public void hit(IPlayer iplayer, Hid hid) {
        Hand hand = validate(hid);
        if (hand == null) {
            LOG.error("got invalid HIT player = " + iplayer);
            return;
        }

        Card card = deal();
        hand.hit(card);

        hid.request(Play.HIT);

        LOG.info("hit hid = " + hid + " with " + card);

        for (IPlayer player : playerSequence) {
            player.deal(hid, card, hand.getValues());
        }

        if (hand.isBroke()) {
            updateBankroll(hid, LOSS);

            for (IPlayer _player : playerSequence) {
                _player.bust(hid);
            }

            LOG.info("going to next hand");
            goNextHand();
        } else if (hand.isCharlie()) {
            hid.request(Play.STAY);

            updateBankroll(hid, CHARLIE_PAYS);

            for (IPlayer _player : playerSequence) {
                _player.charlie(hid);
            }

            goNextHand();
        } else if (hand.isBlackjack()) {
            hid.request(Play.STAY);

            updateBankroll(hid, BLACKJACK_PAYS);

            for (IPlayer _player : playerSequence) {
                _player.blackjack(hid);
            }

            goNextHand();
        } else if (hand.getValue() == 21) {
            goNextHand();
        }
    }

    @Override
    protected void goNextHand() {
        LOG.info("hand sequence index = " + nextHandIndex + " hand sequence size = " + handSequence.size());

        if (nextHandIndex < handSequence.size()) {
            boolean firstSplitHit = false;

            Hid hid = handSequence.get(nextHandIndex++);

            active = players.get(hid);
            LOG.info("active player = " + active);

            Hand hand = this.hands.get(hid);

            if (hand.isBlackjack()) {
                goNextHand();
                return;
            }

            if (hid.isSplit() && hand.size() == 1) {
                try {
                    Thread.sleep(DEAL_DELAY);

                    Card card = deal();
                    hand.hit(card);
                    firstSplitHit = true;
                } catch (InterruptedException ex) {
                    LOG.error(ex.getMessage());
                }
            }

            for (IPlayer player : playerSequence) {
                if (firstSplitHit) {
                    player.deal(hid, hand.getCard(1), hand.getValues());
                }
            }

            // A split hand can become blackjack on the mandatory first hit.
            if (hand.isBlackjack()) {
                hid.request(Play.STAY);
                updateBankroll(hid, BLACKJACK_PAYS);

                for (IPlayer player : playerSequence) {
                    player.blackjack(hid);
                }

                goNextHand();
                return;
            }

            for (IPlayer player : playerSequence) {
                LOG.info("sending turn " + hid + " to " + player);
                player.play(hid);
            }
        } else {
            closeGame();
        }
    }
}
