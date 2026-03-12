package farrell.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;

public class Advisor implements IAdvisor {
    public Play advise(Hand var1, Card var2) {
        return Play.NONE;
    }
}
