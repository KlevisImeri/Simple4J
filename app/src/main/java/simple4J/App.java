package simple4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
  private static final Logger log = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    new Window("Window 1");
    new Thread(() -> new Window("Window 2")).start();
    new Window("Window 3");
  }
}