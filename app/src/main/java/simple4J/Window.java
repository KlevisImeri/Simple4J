package simple4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
  private static final Logger log = LogManager.getLogger(Window.class);

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
    SimpleRuntime.getInstance().addWindow(this);
    SimpleRuntime.getInstance().run();
  }

  /**
   * The initialization of a window should only 
   * be done from the main SimpleRuntime
   */
  public void init() {
    createWindowHandle();
    setCallbacks();
    setUpWindow();
    setUpRenderingThread();
  }

  private void createWindowHandle() {
    log.info("Initializing window: {} ({}x{})", title, width, height);
    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
    if (windowHandle == NULL) {
      log.fatal("Failed to create the GLFW window!");
      throw new RuntimeException("Failed to create the GLFW window");
    }
  }

  private void setCallbacks() {
    glfwSetKeyCallback(windowHandle, (windowHandle, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        log.debug("Escape key pressed, closing window");
        glfwSetWindowShouldClose(windowHandle, true);
      }
    });
  }

  private void setUpWindow() {
    GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    // Center the window
    glfwSetWindowPos(
        windowHandle,
        (vidmode.width() - width) / 2,
        (vidmode.height() - height) / 2);

    glfwShowWindow(windowHandle);
    log.info("Window initialized successfully");
  }

  private void setUpRenderingThread() {
    renderingThread = new RenderingThread(this);
    renderingThread.setName("Thread-" + getTitle());
    renderingThread.start();
  }

  // rendering is done in another thread;
  public void render() { // here comes what you render in everyframe
    // log.debug("Rendering window: {}", title);
  }

  public void remove() {
    glfwFreeCallbacks(windowHandle);
    SimpleRuntime.getInstance().destroyWindow(this);
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

  public RenderingThread getRenderingThread() {
    return renderingThread;
  }
}
