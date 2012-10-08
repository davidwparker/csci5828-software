import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Abstract Twitter
 * 
 * Override this abstract class. Used to get the handles 
 *
 * @author David Parker
 * @version 0.1
 */
public abstract class AbstractTwitter {
    
    /** filename, format, count, and pages to be set in constructor */
    public static String filename;
    public static String format;
    public static int count;
    public static int pages;

    /**
     * timeAndGetStatuses
     */
    public void timeAndGetStatuses()
	throws ExecutionException, InterruptedException, IOException {

	final long start = System.nanoTime();
	getAndWriteStatusesToFile(getHandles());
    	final long end = System.nanoTime();

	System.out.println("Finished retrieving and writing tweets to file in " + (end - start)/1.0e9 + " seconds.");
    }

    /**
     * getHandles
     */
    private static ArrayList<String> getHandles()
	throws IOException {
	
	final BufferedReader reader = new BufferedReader(new FileReader(filename));
	final ArrayList<String> handles = new ArrayList<String>();
    
	String handle = null;
	// add every handle in the file to handles
	while((handle = reader.readLine()) != null) {
	    handles.add(handle);
	}
    
	return handles;
    }

    /**
     * writeStatusesToFile
     *
     * @param String handle - Twitter handle to write file to
     * @param String statuses - Statuses to add to file
     */
    protected void writeStatusesToFile(final String handle, final String statuses) 
	throws IOException {
	try {
	    // Append to file named: "handle".txt
	    FileWriter file = new FileWriter(String.format("%s.txt",handle),true);
	    BufferedWriter out = new BufferedWriter(file);
	    out.write(statuses);
	    out.close();
	}
	catch (IOException e) {
	    System.out.println(String.format("Failed to write statuses for user \"%s\" to file.",handle));
	}
    }

    /**
     * getAndWriteStatusesToFile(final ArrayList handles)
     * 
     * This is where the magic happens (getting the statuses)
     *
     * @param ArrayList<String> handles - list of handles to get statuses for
     */    
    public abstract void getAndWriteStatusesToFile(final ArrayList<String> handles) 
	throws ExecutionException, InterruptedException, IOException;
}
