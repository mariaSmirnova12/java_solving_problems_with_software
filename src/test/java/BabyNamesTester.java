import babynames.BabyNames;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BabyNamesTester {
    BabyNames babyNames;

    @Before
    public void setUp()
    {
        babyNames = new BabyNames();
    }

    @Test
    public void getTotalBirthsRankedHigher() {

        int res = babyNames.getTotalBirthsRankedHigher(2012, "Ethan", "M");
        assertEquals(37973, res);
        res = babyNames.getTotalBirthsRankedHigher(1990, "Drew", "M");
        assertEquals(1498074, res);
    }

    @Test
    public void getRank() {

        int rank = babyNames.getRank(1960, "Emily", "F");
        assertEquals(251, rank);
        rank = babyNames.getRank(1971, "Frank", "M");
        assertEquals(54, rank);
        rank = babyNames.getRank(2012, "Mason", "M");
        assertEquals(2, rank);
        rank = babyNames.getRank(2012, "Emma", "F");
        assertEquals(2, rank);
        rank = babyNames.getRank(2012, "Isabella", "F");
        assertEquals(3, rank);
    }

    @Test
    public void getNameInYear() {

        String name = babyNames.whatIsNameInYear("Susan", 1972, 2014, "F");
        assertEquals(name, "Addison");
        name = babyNames.whatIsNameInYear("Owen", 1974, 2014, "M");
        assertEquals(name, "Leonel");
    }


}
