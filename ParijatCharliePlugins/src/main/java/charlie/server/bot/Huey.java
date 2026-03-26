package charlie.server.bot;

import java.util.List;
import java.util.Random;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.card.HoleCard;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Constant;
import charlie.util.Play;

/**
 * This class implements a server-side blackjack bot.
 * The bot responds on its turn only and sends one play at a time.
 * @author Christian
 */
public class Huey implements IBot, Runnable {
    private final BotBasicStrategy strategy = new BotBasicStrategy();
    private final Random random = new Random();

    private Dealer dealer;
    private Seat seat = Seat.RIGHT;
    private Hid hid = new Hid(seat, Constant.BOT_MIN_BET, 0.0);
    private Hand hand = new Hand(hid);
    private Card dealerUpCard;
    private boolean myTurn;
    private boolean handClosed;
    private boolean requestPending;

    /**
     * Gets the bot's hand.
     * @return Bot hand
     */
    @Override
    public synchronized Hand getHand() {
        return hand;
    }

    /**
     * Sets the dealer callback for this bot.
     * @param dealer Dealer
     */
    @Override
    public synchronized void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    /**
     * Seats the bot and initializes its starting hand.
     * @param seat Seat
     */
    @Override
    public synchronized void sit(Seat seat) {
        this.seat = seat;
        this.hid = new Hid(seat, Constant.BOT_MIN_BET, 0.0);
        this.hand = new Hand(this.hid);
    }

    /**
     * Resets hand-level state at the start of a game.
     * @param hids Hand ids at table
     * @param shoeSize Current shoe size
     */
    @Override
    public synchronized void startGame(List<Hid> hids, int shoeSize) {
        this.hand = new Hand(this.hid);
        this.dealerUpCard = null;
        this.myTurn = false;
        this.handClosed = false;
        this.requestPending = false;
    }

    /**
     * Closes the bot hand state at game end.
     * @param shoeSize Current shoe size
     */
    @Override
    public synchronized void endGame(int shoeSize) {
        this.myTurn = false;
        this.handClosed = true;
        this.requestPending = false;
    }

    /**
     * Receives card updates for all seats and tracks only relevant cards.
     * @param hid Hand id
     * @param card Card dealt
     * @param values Hand values
     */
    @Override
    public synchronized void deal(Hid hid, Card card, int[] values) {
        if (hid == null) {
            return;
        }

        if (hid.getSeat() == Seat.DEALER) {
            if (card != null && !(card instanceof HoleCard)) {
                this.dealerUpCard = card;
            }
            return;
        }

        if (!hid.equals(this.hid) || card == null) {
            return;
        }

        hand.hit(card);

        if (!myTurn || handClosed || requestPending || hand.isBroke() || hand.isBlackjack() || hand.isCharlie()) {
            return;
        }

        spawnWorker();
    }

    /**
     * Receives insurance prompt.
     */
    @Override
    public synchronized void insure() {
        // Bot does not buy insurance.
    }

    /**
     * Receives bust notification.
     * @param hid Hand id
     */
    @Override
    public synchronized void bust(Hid hid) {
        closeIfMine(hid);
    }

    /**
     * Receives win notification.
     * @param hid Hand id
     */
    @Override
    public synchronized void win(Hid hid) {
        closeIfMine(hid);
    }

    /**
     * Receives blackjack notification.
     * @param hid Hand id
     */
    @Override
    public synchronized void blackjack(Hid hid) {
        closeIfMine(hid);
    }

    /**
     * Receives charlie notification.
     * @param hid Hand id
     */
    @Override
    public synchronized void charlie(Hid hid) {
        closeIfMine(hid);
    }

    /**
     * Receives lose notification.
     * @param hid Hand id
     */
    @Override
    public synchronized void lose(Hid hid) {
        closeIfMine(hid);
    }

    /**
     * Receives push notification.
     * @param hid Hand id
     */
    @Override
    public synchronized void push(Hid hid) {
        closeIfMine(hid);
    }

    /**
     * Receives shuffle warning notification.
     */
    @Override
    public synchronized void shuffling() {
        // No-op.
    }

    /**
     * Receives turn updates and acts only when this bot's hand is active.
     * @param hid Hand id whose turn changed
     */
    @Override
    public synchronized void play(Hid hid) {
        if (hid == null) {
            return;
        }

        if (hid.equals(this.hid)) {
            myTurn = true;
            if (!handClosed && !requestPending) {
                spawnWorker();
            }
            return;
        }

        myTurn = false;
    }

    /**
     * Receives split notification.
     * @param newHid New hand id
     * @param origHid Original hand id
     */
    @Override
    public synchronized void split(Hid newHid, Hid origHid) {
        // Split is not used by IBot in Charlie.
    }

    /**
     * Worker thread entry for one play request.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(1500 + random.nextInt(2001));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        Play play;
        synchronized (this) {
            if (dealer == null
                    || !myTurn
                    || handClosed
                    || dealerUpCard == null
                    || hand.isBroke()
                    || hand.isBlackjack()
                    || hand.isCharlie()) {
                requestPending = false;
                return;
            }
            play = strategy.getPlay(hand, dealerUpCard);
            if (play == Play.SPLIT || play == Play.NONE) {
                play = Play.HIT;
            }
            if (play == Play.DOUBLE_DOWN && hand.size() != 2) {
                play = Play.HIT;
            }
        }

        switch (play) {
            case STAY:
                synchronized (this) {
                    handClosed = true;
                    myTurn = false;
                    requestPending = false;
                }
                dealer.stay(this, hid);
                break;
            case DOUBLE_DOWN:
                synchronized (this) {
                    handClosed = true;
                    myTurn = false;
                    requestPending = false;
                }
                dealer.doubleDown(this, hid);
                break;
            case HIT:
            default:
                synchronized (this) {
                    requestPending = false;
                }
                dealer.hit(this, hid);
                break;
        }
    }

    /**
     * Spawns a worker thread to send one play.
     */
    private synchronized void spawnWorker() {
        requestPending = true;
        Thread worker = new Thread(this, getClass().getSimpleName() + "-worker");
        worker.start();
    }

    /**
     * Closes hand state if notification belongs to this bot hand.
     * @param hid Hand id
     */
    private void closeIfMine(Hid hid) {
        if (hid != null && hid.equals(this.hid)) {
            this.handClosed = true;
            this.myTurn = false;
            this.requestPending = false;
        }
    }
}
