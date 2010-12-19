/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crawlhal;

import org.jibble.jmegahal.JMegaHal;

/**
 *
 * @author vigge
 */
public class Hal {
    private static JMegaHal hal = new JMegaHal();
    
    public String getSentence(String input) {
        return hal.getSentence(input);
    }

    public void add(String input) {
        hal.add(input);
    }
}
