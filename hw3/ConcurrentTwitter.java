import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Concurrent Twitter
 *
 * Class to get a lot of tweets back in a concurrent fashion.
 *
 * @author David Parke
 * @version 0.1
 */
public class ConcurrentTwitter extends AbstractTwitter {

    /**
     * Constructor
     *
     * @param String filename (required) - filename to get handles from
     * @param String format (required) - format of statuses (json|atom)
     * @param int count (required) - number of tweets per page
     * @param int pages (required) - number of pages
     */
    public ConcurrentTwitter(final String filename, final String format, final int count, final int pages) {
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
	throws InterruptedException, ExecutionException {

	final List<Callable<Void>> partitions = new ArrayList<Callable<Void>>();
	final int numberOfCores = Runtime.getRuntime().availableProcessors();
	final double blockingCoefficient = 0.9;
	final int poolSize = (int)(numberOfCores / (1 - blockingCoefficient));
	final List<Integer> listOfPages = new ArrayList<Integer>();
	for (int i = 1; i < pages+1; i++) { listOfPages.add(i); }

	System.out.println("Number of Cores available is " + numberOfCores);
	System.out.println("Pool size is " + poolSize);
	for(final String handle : handles) {
	    for (final int i : listOfPages) {	   
		partitions.add(new Callable<Void>() {
			public Void call() throws Exception {
			    System.out.println(String.format("Retrieving page %s for user \"%s\".",String.valueOf(i),handle));
			    writeStatusesToFile(handle,TwitterStatuses.get(handle,format,String.valueOf(count),String.valueOf(i)));
			    return null;
			}
		    });
	    }
	}
        
	final ExecutorService executorPool = Executors.newFixedThreadPool(poolSize);
	executorPool.invokeAll(partitions, 100000, TimeUnit.SECONDS);

	// Don't need to do anything after, so making Callable return void
	//	final List<Future<String>> statuses = executorPool.invokeAll(partitions, 100000, TimeUnit.SECONDS);
	//	for(final Future<String> status : statuses)
	//	    writeStatusesToFile(handle,status,format));

	executorPool.shutdown();
    } 

    /**
     * main - do it!
     */    
    public static void main(final String[] args)
	throws ExecutionException, InterruptedException, IOException { 
	new ConcurrentTwitter("twitterHandles.txt","json",200,16).timeAndGetStatuses();
    }
}
