package simple4j;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL;

public class RenderingThread extends Thread {
  private static final Logger log = LogManager.getLogger(RenderingThread.class);

  Window window;

  RenderingThread(Window window) {
    this.window = window;
  }

  @Override
  public void run() {
    log.info("Starting RederingThread for window {}", window.getTitle());
    glfwMakeContextCurrent(window.getWindowHandle());
    GL.createCapabilities();
    glfwSwapInterval(1);
    while (!glfwWindowShouldClose(window.getWindowHandle())) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      glfwSwapBuffers(window.getWindowHandle());
      window.render();
    }
    glfwMakeContextCurrent(NULL);
    window.remove();
  }
}
