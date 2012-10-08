import java.util.ArrayList;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Sequential Twitter
 *
 * Class to get all of the statuses for a given list of handles in a sequential fashion
 *
 * @author David Parker
 * @version 0.1
 */
public class SequentialTwitter extends AbstractTwitter {

    /**
     * Constructor
     *
     * @param String filename (required) - filename to get handles from
     * @param String format (required) - format of statuses (json|atom)
     * @param int count (required) - number of tweets per page
     * @param int pages (required) - number of pages
     */
    public SequentialTwitter(final String filename, final String format, final int count, final int pages) {
	this.filename = filename;
	this.format = format;
	this.count = count;
	this.pages = pages;
    }

    /**
     * getAndWriteStatusesToFile(final ArrayList<String> handles)
     *
     * @param ArrayList<String> handles - list of Twitter handles to get statuses from
     */
    public void getAndWriteStatusesToFile(final ArrayList<String> handles) 
	throws IOException {

	for(final String handle : handles) {
	    getAndWriteStatusesToFile(handle);
	}
    }

    /**
     * getAndWriteStatusesToFile(final String handle)
     *
     * @param String handle - Twitter handle to get statuses from
     */
    private void getAndWriteStatusesToFile(final String handle)
	throws IOException {

	for (int i = 1; i < this.pages+1; i++) {
	    System.out.println(String.format("Retrieving page %s for user \"%s\".",String.valueOf(i),handle));
	    writeStatusesToFile(handle,TwitterStatuses.get(handle,this.format,String.valueOf(this.count),String.valueOf(i)));
	}
    }

    /**
     * main - do it!
     */
    public static void main(final String[] args) 
	throws ExecutionException, IOException, InterruptedException { 
	new SequentialTwitter("twitterHandles.txt","json",200,16).timeAndGetStatuses();
    }
}
