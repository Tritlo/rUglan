package is.mpg.ruglan.test;

import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.TestCase;

import is.mpg.ruglan.Utils;

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
}
