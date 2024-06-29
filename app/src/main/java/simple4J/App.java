package simple4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
  private static final Logger log = LogManager.getLogger(App.class);
  public static void main(String[] args) {
    Simple.init();  
    
    Window win1 = new Window("Window 1");
    Window win2 = new Window("Window 2");

    Simple.render();
  }
}