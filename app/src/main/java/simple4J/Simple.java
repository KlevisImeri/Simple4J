package simple4j;

import static org.lwjgl.glfw.GLFW.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Simple {

  private static final Logger log = LogManager.getLogger(Simple.class);

  private static boolean glfwInitialized = false;

  public synchronized static void init() {
    log.info("Initializing simple4j...");
    log.info("Initializing GLFW...");
    if (glfwInitialized)
      return;
    GLFWErrorCallback.createPrint(System.err).set();

    if (!glfwInit()){
      log.fatal("Unable to initialize GLFW");
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    log.info("GLFW initialized successfully");
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    glfwInitialized = true;
    log.info("simple4j initialized successfully");
  }

  public static void render() {
    // log.info("Started Simple rendering...");
    // while (Window.getCount() != 0) {
    //   glfwWaitEvents();{}
    // }
    // terminate();
  }

  private static void terminate() {
    log.info("Terminating GLFW...");
    glfwTerminate();
    glfwSetErrorCallback(null).free();
    log.info("GLFW terminated");
  }
}
