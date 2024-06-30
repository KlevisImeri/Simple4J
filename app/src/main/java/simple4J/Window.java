package simple4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.List;

public class Window {
  private static final Logger log = LogManager.getLogger(Window.class);

  public static List<Window> windows = new ArrayList<Window>();

  private long windowHandle;
  private int width, height;
  private String title;

  private RenderingThread renderingThread;

  public Window() {
    this(800, 600, "Simple Window");
  }

  public Window(String title) {
    this(800, 600, title);
  }

  public Window(int width, int height, String title) {
    this.width = width;
    this.height = height;
    this.title = title;
    init();
  }

  private void init() {
    log.info("Initializing window: {} ({}x{})", title, width, height);
    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
    if (windowHandle == NULL) {
      log.fatal("Failed to create the GLFW window!");
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup a key callback
    glfwSetKeyCallback(windowHandle, (windowHandle, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        log.debug("Escape key pressed, closing window");
        glfwSetWindowShouldClose(windowHandle, true);
      }
    });

    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    // Center the window
    glfwSetWindowPos(
        windowHandle,
        (vidmode.width() - width) / 2,
        (vidmode.height() - height) / 2);

    glfwShowWindow(windowHandle);
    windows.add(this);
    log.info("Window initialized successfully");
    
    renderingThread = new RenderingThread(this);
    renderingThread.setName("Thread-"+getTitle());
    renderingThread.start();
  }

  public void render() {
    // log.debug("Rendering window: {}", title);
  }

  public void remove() {
    glfwFreeCallbacks(windowHandle);
    windows.remove(this);
    Simple.removeQueue.add(this);
  }

  public long getWindowHandle() {
    return windowHandle;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public String getTitle() {
    return title;
  }

  public RenderingThread getRenderingThread(){
    return renderingThread;
  }
}
