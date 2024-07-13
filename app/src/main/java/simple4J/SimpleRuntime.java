package simple4j;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFWErrorCallback;

public class SimpleRuntime {
  private static final Logger log = LogManager.getLogger(SimpleRuntime.class);

  /* -------------------------------- singleton ------------------------------- */
  private static volatile SimpleRuntime instance = new SimpleRuntime();

  public static SimpleRuntime getInstance() {
    return instance;
  }

  private SimpleRuntime() { }
  /* -------------------------------------------------------------------------- */

  private final Queue<Window> addQueue = new ConcurrentLinkedQueue<>();
  private final List<Window> windows = new ArrayList<>();
  private final Queue<Window> removeQueue = new ConcurrentLinkedQueue<>();
  private boolean running = false;

  public void addWindow(Window window) {
    addQueue.add(window);
  }

  public void destroyWindow(Window window) {
    removeQueue.add(window);
  }


  /**
   * Make sure there is a window before running the Runtime
   * else the Runtime will see there are no windows and it 
   * will just terminate.
   * You only run the runtime once but you dont have to 
   * check cuase it does by itself.
   */
  public synchronized void run() {
    if(running) return;
    new Thread(() -> {
      log.info("Initializing SimpleRuntime...");
      initializeGlfw();
      setUpWindowHints();
      initializeNewWindows();
      log.info("SimpleRuntime initialized successfully");

      while (!windows.isEmpty()) {
        initializeNewWindows();

        glfwWaitEvents();

        removeDestroiedWindows();
      }

      terminate();

    }).start();
    running = true;
  }

  private void initializeGlfw() {
    log.info("Initializing GLFW...");
    GLFWErrorCallback.createPrint(System.err).set();
    if (!glfwInit()) {
      log.fatal("Unable to initialize GLFW");
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    log.info("GLFW initialized successfully");
  }

  private void setUpWindowHints() {
    log.info("Setting up window hints...");
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    log.info("Window hints set up successfully.");
  }

  private void initializeNewWindows() {
    while (!addQueue.isEmpty()) {
      Window window = addQueue.poll();
      window.init();
      windows.add(window);
    }
  }

  private void removeDestroiedWindows() {
    while (!removeQueue.isEmpty()) {
      Window window = removeQueue.poll();
      if (window != null) {
        log.debug("Destroying window {}", window.getTitle());
        try {
          window.getRenderingThread().join();
        } catch (InterruptedException e) {
          log.error("Interrupted while waiting for rendering thread to finish", e);
        }
        glfwDestroyWindow(window.getWindowHandle());
        windows.remove(window);
      }
    }
  }

  private void terminate() {
    log.info("Terminating GLFW...");
    glfwTerminate();
    glfwSetErrorCallback(null).free();
    log.info("GLFW terminated");
  }

}
