package simple4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
  private static final Logger logger = LogManager.getLogger(Window.class);
  private static int count = 0;

  public static int getCount() {
    return count;
  }
  public static void decrementCount() {
    count--;
  }

  private long windowHandle;
  private int width, height;
  private String title;

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
    logger.info("Initializing window: {} ({}x{})", title, width, height);
    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
    if (windowHandle == NULL){
      logger.fatal("Failed to create the GLFW window!");
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup a key callback
    glfwSetKeyCallback(windowHandle, (windowHandle, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        logger.debug("Escape key pressed, closing window");
        glfwSetWindowShouldClose(windowHandle, true);
    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1);
      IntBuffer pHeight = stack.mallocInt(1);

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(windowHandle, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
          windowHandle,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2);
    } // the stack frame is popped automatically

    glfwShowWindow(windowHandle);
    count++;
    logger.info("Window initialized successfully");

    logger.info("Starting RederingThread for window {}", title);
    new RenderingThread(this).start();
  }

  public void render() {
    // logger.debug("Rendering window: {}", title);
  }

  public void cleanup() {
    glfwFreeCallbacks(windowHandle);
    glfwDestroyWindow(windowHandle);
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
}
