package simple4j;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

public class RenderingThread extends Thread {
  Window window;

  RenderingThread(Window window) {
    this.window = window;
  }

  @Override
  public void run() {
    glfwMakeContextCurrent(window.getWindowHandle());
    GL.createCapabilities();
    glfwSwapInterval(1);
    while (!glfwWindowShouldClose(window.getWindowHandle())) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      glfwSwapBuffers(window.getWindowHandle());
      window.render();
      glfwWaitEvents();
      // try{
      //   sleep(1000);
      // }catch(InterruptedException e){}
    }
    window.cleanup();
    Window.decrementCount();
  }
}
