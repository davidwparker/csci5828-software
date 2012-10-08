import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Twitter Statuses
 *
 * Class uses to get statuses for a given Twitter handle and page
 *
 * @author David Parker
 * @version 0.1
 */
public class TwitterStatuses {

    /** URL of the twitter status */
    private static final String URLSTRING = "https://twitter.com/statuses/user_timeline/%s.%s?count=%s&page=%s";

    /**
     * get(final String handle, String page)
     * 
     * @param String handle (required) - Twitter handle for a given user
     * @param String page (required) - current page to get statuses on
     * @param String format (required) - format of the returned statuses (json|atom)
     */
    public static String get(final String handle, final String format, final String count, final String page)
	throws IOException {

	// Build the correct URL
	final URL url = new URL(String.format(URLSTRING,handle,format,count,page));
	// Output String to concat to
	String outputString = "";
	try {
	    final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	    String line;

	    // For every line that we get back, concat them to the output
	    while ((line = reader.readLine()) != null) {
		outputString += line;
	    }
	}
	catch (IOException e) {
	    // Twitter likes to give 502 and other errors... we ignore those pages and move on
	    System.out.println(String.format("Failed to retrieve page %s for user \"%s\".",page,handle));
	    System.out.println(e.getMessage());
	}

	return outputString;
    }
}
