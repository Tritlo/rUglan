package is.mpg.ruglan.test;
import is.mpg.ruglan.iCalParser;
import android.test.AndroidTestCase;

/**
 * Created by tritlo on 10/8/13.
 */
public class iCalParserTest extends AndroidTestCase{

    //@Test
    public void testUrlToString() throws Exception {
        String hws = "hello, world\n";
        String urlHWS = iCalParser.urlToString("https://notendur.hi.is/mpg3/helloworld.html");
        assertEquals(hws,urlHWS);
    }
}
