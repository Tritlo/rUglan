package is.mpg.ruglan.test;

import android.test.AndroidTestCase;

import is.mpg.ruglan.utils.Utils;

/**
 * Created by jon on 1.11.2013.
 */
public class UtilsTest extends AndroidTestCase {
    public void testFillColorsArray() throws Exception {

        Utils.fillColorsArray();
        assertTrue(Utils.colors.length > 10);
        assertEquals(7, Utils.colors[10].length());
        assertEquals("#",Utils.colors[10].substring(0, 1));
        Long.decode(Utils.colors[10]);
    }
    
    /**
     * Checks if the stripCourseNumberFromName works as expected.
     * @throws Exception
     */
    public void testStripCourseNumberFromName() throws Exception {
    	String[] input = new String[]{
    			"EDL102G-20136 Edlisfraedi 1 V",
    			"IDN503G-20136 Verkefnastjornun",
    			"TOL301G-20136 Formleg mal og reiknanleiki",
    			"HBV501G-20136 Hugbunadarverkefni 1"
    	};
    	String[] output = new String[]{
    			"Edlisfraedi 1 V",
    			"Verkefnastjornun",
    			"Formleg mal og reiknanleiki",
    			"Hugbunadarverkefni 1"
    	};
    	for(int i=0; i<input.length; i++) {
    		assertTrue("Result did not match expected result.",
    			Utils.stripCourseNumberFromName(input[i]).equals(output[i]));
    	}
    }
}
