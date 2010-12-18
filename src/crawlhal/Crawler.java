/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crawlhal;

import java.net.MalformedURLException;
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
            // Get the first URL in the pending Set
            String url = Main.getPending().iterator().next().toString();
            Main.setCurrentlyCrawling(url);
            // Connect and get the links from the HTML
            Document doc = Jsoup.connect(url).userAgent("crawlHal").get();
            Elements links = doc.select("a[href]");
            int size = links.size();

            for (Element link : links) {
                
                String linkURL = link.attr("abs:href");

                // Make sure we have an URL, otherwise we are stuck here forever
                if(linkURL.isEmpty()) continue;

                // Filter out some known filetypes, we do not wish to crawl these
                if(linkURL.endsWith("jpg")  ||
                   linkURL.endsWith("jpeg") ||
                   linkURL.endsWith("png")  ||
                   linkURL.endsWith("bmp")  ||
                   linkURL.endsWith("zip")  ||
                   linkURL.endsWith("rar")  ||
                   linkURL.endsWith("tar")  ||
                   linkURL.endsWith("exe")  ||
                   linkURL.endsWith("jar")  ||
                   linkURL.endsWith("msi")  ||
                   linkURL.endsWith("css")  ||
                   linkURL.endsWith("txt")  ||
                   linkURL.endsWith("#")) {
                    Main.linksFound++;
                    Main.linksTotal++;
                    continue;
                }

                URL url1;
                try {
                    url1 = new URL(linkURL);
                } catch(MalformedURLException e){
                    System.out.println("Malformed URL");
                    continue;
                }

                // Appends the URL to the output. This needs changing
                Main.appendToOutputArea(url1.toString());
                // Add the URL to the pending list
                Main.addToPending(url1);
                // Update the status
                Main.updateStatus();
                Main.linksTotal++;
                
                
            }
            
            // This URL is crawled and does not need to be crawled again
            Main.addToCrawled(new URL(url));
            // Remove the first URL from the pending list
            Main.removeFromPending(new URL(url));

            // Debug stuff
            System.out.println("on "+ url+" - next: "+ url);
            System.out.println("Links added: "+Main.linksAddedToPending);
            System.out.println("Links already found or invalid: "+Main.linksFound);
            System.out.println("Total "+Main.linksAddedToPending+" of "+Main.linksTotal+" added to pending");
            System.out.println("---");
            Main.linksAddedToPending = 0;
            Main.linksFound = 0;
            Main.linksTotal = 0;
        } while((Main.getPending().size() >0) && (Main.toggled != false));
        return null;
    }

}
