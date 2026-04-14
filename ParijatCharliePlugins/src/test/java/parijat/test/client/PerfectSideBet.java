package parijat.test.client;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IUi;
import charlie.test.framework.Perfect;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * End-to-end test for the Lab 8/9 side-bet shoe (10 scripted games).
 * Sequences main and side bets and play actions to match {@code charlie.sidebet.test.Shoe}.
 * @author Ron.Coleman
 */
public class PerfectSideBet extends Perfect implements IUi {

    private static final int MAIN_BET = 25;
    private static final int SIDE_BET_AFTER_FIRST = 10;
    private static final int NUM_GAMES = 10;

    /**
     * Expected bankroll after each game completes (Lab 9 appendix, Table 1).
     */
    private static final double[] EXPECTED_BANKROLL_AFTER_GAME = {
        1000.0, 1055.0, 1070.0, 1075.0, 1040.0,
        1315.0, 1330.0, 1365.0, 1420.0, 1435.0
    };

    static {
        System.setProperty("charlie.props", "../Charlie/charlie.props");
    }

    private Hid you;
    private Hand myHand;
    private int gameNumber = -1;
    private double bankroll = 1000.0;
    private volatile CountDownLatch gameEndLatch;

    /**
     * After a hit, the stock {@code Dealer} does not always send a second {@code play()};
     * we stand from {@link #deal} once the draw card is applied, and use this flag so
     * a second {@code stay} is not sent if a second {@code play} does arrive.
     */
    private boolean hitSequenceStandIssued;

    /**
     * Runs the test.
     */
    public void test() throws Exception {
        go(this);

        for (int g = 0; g < NUM_GAMES; g++) {
            gameEndLatch = new CountDownLatch(1);
            int sideAmt = (g == 0) ? 0 : SIDE_BET_AFTER_FIRST;
            bet(MAIN_BET, sideAmt);
            info("game loop " + g + " bet main=" + MAIN_BET + " side=" + sideAmt);
            assertTrue("timed out waiting for game " + g,
                    gameEndLatch.await(20, TimeUnit.SECONDS));
        }

        assertEquals(1435.0, bankroll, 1e-6);
        info("DONE");
    }

    @Override
    public void deal(Hid hid, Card card, int[] handValues) {
        if (hid.getSeat() == Seat.YOU && card != null) {
            myHand.hit(card);
            if (gameNumber >= 0 && gameNumber <= 4
                    && myHand.size() == 3
                    && !myHand.isBroke()
                    && !hitSequenceStandIssued) {
                hitSequenceStandIssued = true;
                stay(you);
            }
        }
        info("DEAL: " + hid + " card: " + card + " hand values: "
                + handValues[0] + ", " + handValues[1]);
    }

    @Override
    public void play(Hid hid) {
        if (hid.getSeat() != Seat.YOU) {
            return;
        }
        if (gameNumber >= 0 && gameNumber <= 4) {
            if (myHand.size() == 2) {
                hit(hid);
            } else if (!hitSequenceStandIssued) {
                stay(hid);
            }
        } else {
            stay(hid);
        }
    }

    @Override
    public void bust(Hid hid) {
        info("BREAK: " + hid);
        if (hid.getSeat() == Seat.YOU) {
            fail("unexpected player bust");
        }
    }

    @Override
    public void win(Hid hid) {
        info("WIN: " + hid);
        if (hid.getSeat() != Seat.YOU) {
            return;
        }
        assertEquals(Seat.YOU, hid.getSeat());
        assertEquals(you, hid);
        switch (gameNumber) {
            case 1:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(30.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 2:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(-10.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 5:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(250.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 6:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(-10.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 7:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(10.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 8:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(30.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 9:
                assertEquals(25.0, hid.getAmt(), 1e-9);
                assertEquals(-10.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            default:
                fail("unexpected win in game " + gameNumber);
        }
    }

    @Override
    public void lose(Hid hid) {
        info("LOSE: " + hid);
        if (hid.getSeat() != Seat.YOU) {
            return;
        }
        assertEquals(Seat.YOU, hid.getSeat());
        assertEquals(you, hid);
        switch (gameNumber) {
            case 3:
                assertEquals(-25.0, hid.getAmt(), 1e-9);
                assertEquals(30.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            case 4:
                assertEquals(-25.0, hid.getAmt(), 1e-9);
                assertEquals(-10.0, hid.getSideAmt(), 1e-9);
                bankroll += hid.getAmt() + hid.getSideAmt();
                break;
            default:
                fail("unexpected lose in game " + gameNumber);
        }
    }

    @Override
    public void push(Hid hid) {
        info("PUSH: " + hid);
        if (hid.getSeat() != Seat.YOU) {
            return;
        }
        assertEquals(Seat.YOU, hid.getSeat());
        assertEquals(you, hid);
        if (gameNumber == 0) {
            assertEquals(0.0, hid.getAmt(), 1e-9);
            assertEquals(0.0, hid.getSideAmt(), 1e-9);
            bankroll += hid.getAmt() + hid.getSideAmt();
        } else {
            fail("unexpected push in game " + gameNumber);
        }
    }

    @Override
    public void blackjack(Hid hid) {
        info("BLACKJACK: " + hid);
        if (hid.getSeat() != Seat.YOU) {
            return;
        }
        fail("unexpected blackjack");
    }

    @Override
    public void charlie(Hid hid) {
        info("CHARLIE: " + hid);
        if (hid.getSeat() != Seat.YOU) {
            return;
        }
        fail("unexpected charlie");
    }

    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("game STARTING: ");
        gameNumber++;
        myHand = null;
        hitSequenceStandIssued = false;
        for (Hid hid : hids) {
            buffer.append(hid).append(", ");
            if (hid.getSeat() == Seat.YOU) {
                this.you = hid;
                this.myHand = new Hand(hid);
            }
        }
        buffer.append(" shoe size: ").append(shoeSize);
        info(buffer.toString());
    }

    @Override
    public void endGame(int shoeSize) {
        assertEquals("bankroll after game " + gameNumber,
                EXPECTED_BANKROLL_AFTER_GAME[gameNumber], bankroll, 1e-6);
        gameEndLatch.countDown();
        info("ENDING game " + gameNumber + " shoe size: " + shoeSize);
    }

    @Override
    public void shuffling() {
        info("SHUFFLING");
    }

    @Override
    public void setCourier(Courier courier) {
    }

    @Override
    public void split(Hid newHid, Hid origHid) {
        fail("split not expected");
    }

    @Override
    public void insure() {
        fail("insurance not supported");
    }
}
