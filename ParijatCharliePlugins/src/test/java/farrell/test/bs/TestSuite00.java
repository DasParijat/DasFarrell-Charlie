package farrell.test.bs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import farrell.test.bs.invalid.TestHandTooSmall;
import farrell.test.bs.invalid.TestHandValueTooHigh;
import farrell.test.bs.invalid.TestInvalidUpCardRank;
import farrell.test.bs.invalid.TestNullHand;
import farrell.test.bs.invalid.TestNullUpCard;
import farrell.test.bs.section1.Test_12_2_00;
import farrell.test.bs.section1.Test_12_4_00;
import farrell.test.bs.section1.Test_13_6_00;
import farrell.test.bs.section1.Test_16_7_00;
import farrell.test.bs.section2.Test_10_T_00;
import farrell.test.bs.section2.Test_11_6_00;
import farrell.test.bs.section2.Test_11_A_00;
import farrell.test.bs.section2.Test_9_3_00;
import farrell.test.bs.section3.Test_A6_7_00;
import farrell.test.bs.section3.Test_A7_3_00;
import farrell.test.bs.section3.Test_A7_9_00;
import farrell.test.bs.section3.Test_A9_A_00;
import farrell.test.bs.section4.Test_55_6_00;
import farrell.test.bs.section4.Test_88_6_00;
import farrell.test.bs.section4.Test_99_7_00;
import farrell.test.bs.section4.Test_AA_9_00;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        Test_12_2_00.class,
        Test_12_4_00.class,
        Test_13_6_00.class,
        Test_16_7_00.class,

        Test_10_T_00.class,
        Test_11_6_00.class,
        Test_11_A_00.class,
        Test_9_3_00.class,

        Test_A7_9_00.class,
        Test_A7_3_00.class,
        Test_A6_7_00.class,
        Test_A9_A_00.class,

        Test_88_6_00.class,
        Test_AA_9_00.class,
        Test_99_7_00.class,
        Test_55_6_00.class,

        TestNullHand.class,
        TestNullUpCard.class,
        TestHandTooSmall.class,
        TestInvalidUpCardRank.class,
        TestHandValueTooHigh.class
})
public class TestSuite00 {
}