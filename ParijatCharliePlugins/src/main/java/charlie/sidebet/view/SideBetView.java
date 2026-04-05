/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.sidebet.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.view.AMoneyManager;
import charlie.view.sprite.Chip;
import charlie.view.sprite.ChipButton;

/**
 * This class implements the side bet view
 * @author Ron Coleman, Ph.D.
 */
public class SideBetView implements ISideBetView {
    private enum Outcome { None, Win, Lose }

    private final Logger LOG = Logger.getLogger(SideBetView.class);
    
    public static final int X = 400;
    public static final int Y = 200;
    public static final int DIAMETER = 50;
    private static final int PLACE_X = X + DIAMETER / 2 + 10;
    private static final int PLACE_Y = Y - DIAMETER / 4;
    private static final int TEXT_X = PLACE_X + 90;
    private static final int TEXT_Y = Y - 20;
    private static final int OUTCOME_X = X + DIAMETER / 2 + 15;
    private static final int OUTCOME_Y = Y + 8;
    
    private final Font font = new Font("Arial", Font.BOLD, 18);
    private final Font infoFont = new Font("Arial", Font.BOLD, 16);
    private final Font outcomeFont = new Font("Arial", Font.BOLD, 18);
    private final Color infoColor = Color.YELLOW;
    private final Color loseColorBg = new Color(250,58,5);
    private final Color loseColorFg = Color.WHITE;
    private final Color winColorFg = Color.BLACK;
    private final Color winColorBg = new Color(116,255,4);
    
    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    private final float dash1[] = {10.0f};
    private final BasicStroke dashed
            = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);   

    private List<ChipButton> buttons;
    private final List<Chip> chips = new ArrayList<>();
    private int amt = 0;
    private AMoneyManager moneyManager;
    private Outcome outcome = Outcome.None;
    private final Random ran = new Random();

    public SideBetView() {
        LOG.info("side bet view constructed");
    }
    
    /**
     * Sets the money manager.
     * @param moneyManager 
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }
    
    /**
     * Registers a click for the side bet.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        if(buttons == null)
            return;

        // Test if any chip button has been pressed.
        for(ChipButton button: buttons) {
            if(button.isReady() && button.isPressed(x, y)) {
                amt += button.getAmt();

                int n = chips.size();
                int placeX = PLACE_X + n * button.getImage().getWidth(null) / 3 + ran.nextInt(21) - 10;
                int placeY = PLACE_Y + ran.nextInt(11) - 5;

                chips.add(new Chip(button.getImage(), placeX, placeY, button.getAmt()));
                outcome = Outcome.None;
                SoundFactory.play(Effect.CHIPS_IN);

                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
                return;
            }
        }

        if(isAtStakePressed(x, y)) {
            amt = 0;
            chips.clear();
            outcome = Outcome.None;
            SoundFactory.play(Effect.CHIPS_OUT);
            LOG.info("B. side bet amount cleared");
        }
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();
        
        if(bet == 0)
            return;

        outcome = bet > 0 ? Outcome.Win : Outcome.Lose;

        LOG.info("side bet outcome = "+bet);
        
        // Update the bankroll
        moneyManager.update(bet);
        
        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting
     */
    @Override
    public void starting() {
        outcome = Outcome.None;
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view
     */
    @Override
    public void update() {
    }

    /**
     * Renders the view
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        if(g == null) {
            return;
        }

        // Draw the at-stake place on the table
        g.setColor(Color.RED); 
        g.setStroke(dashed);
        g.drawOval(X-DIAMETER/2, Y-DIAMETER/2, DIAMETER, DIAMETER);
        
        // Draw the at-stake amount
        g.setFont(font);
        g.setColor(Color.WHITE);
        String text = "" + amt;
        FontMetrics fm = g.getFontMetrics(font);
        int textX = X - fm.charsWidth(text.toCharArray(), 0, text.length()) / 2;
        int textY = Y + fm.getHeight() / 4;
        g.drawString(text, textX, textY);

        g.setFont(infoFont);
        g.setColor(infoColor);
        g.drawString("SUPER 7 pays 3:1", TEXT_X, TEXT_Y);
        g.drawString("ROYAL MATCH pays 25:1", TEXT_X, TEXT_Y + 22);
        g.drawString("EXACTLY 13 pays 1:1", TEXT_X, TEXT_Y + 44);

        for(Chip chip: chips) {
            chip.render(g);
        }

        renderOutcome(g);
    }

    private boolean isAtStakePressed(int x, int y) {
        int left = X - DIAMETER / 2;
        int top = Y - DIAMETER / 2;
        return x > left && x < left + DIAMETER && y > top && y < top + DIAMETER;
    }

    private void renderOutcome(Graphics2D g) {
        if(outcome == Outcome.None || chips.isEmpty())
            return;

        String text = outcome == Outcome.Win ? " WIN " : " LOSE ";
        FontMetrics fm = g.getFontMetrics(outcomeFont);
        int w = fm.charsWidth(text.toCharArray(), 0, text.length());
        int h = fm.getHeight();

        int x = OUTCOME_X;
        int y = OUTCOME_Y;

        if(outcome == Outcome.Win)
            g.setColor(winColorBg);
        else
            g.setColor(loseColorBg);

        g.fillRoundRect(x, y - h + 5, w, h, 5, 5);

        if(outcome == Outcome.Win)
            g.setColor(winColorFg);
        else
            g.setColor(loseColorFg);

        g.setFont(outcomeFont);
        g.drawString(text, x, y);
    }
}
