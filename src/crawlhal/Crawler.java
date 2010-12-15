/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crawlhal;

import java.net.URL;
import javax.swing.SwingWorker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author vigge
 */
public class Crawler extends SwingWorker<Void, Void> {
    
    @Override
    protected Void doInBackground() throws Exception {
        
        // Crawl and parse for links until there is no more pending URL's
        do {
            // Get the first URL in the pending list
            String url = Main.getPending().getFirst().toString();

            // Connect and get the links from the HTML
            Document doc = Jsoup.connect(url).userAgent("crawlHal").get();
            Elements links = doc.select("a[href]");


            for (Element link : links) {
                // Make an URL of the attribute
                URL url1 = new URL(link.attr("abs:href"));

                if(url1 != null){
                    // Appends the URL to the output. This needs changing
                    Main.appendToOutputArea(url1.toString());
                    // Add the URL to the pending list
                    Main.addToPending(url1);
                    // Update the status
                    Main.updateStatus();
                }
            }
            // This URL is crawled and does not need to be crawled again
            Main.addToCrawled(Main.getPending().getFirst());
            // Remove the first URL from the pending list
            Main.removeFromPending();
        } while((Main.getPending().size() >0) && (Main.toggled != false));
        return null;
    }

}
