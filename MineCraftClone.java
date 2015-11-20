/*
File: FPCameraController.java, MineCraftClone.java, Vector3Float.java
Block.java, Chunk.java, SimplexNoise.java, SimplexNoise_octave.java
Author: Sharat V. , Maheen Iqbal, Fahad Ahmed
Class: CS 445

Assignment: Final Project Checkpoint 2
Date Last Mod: 11/19/15
*/
package minecraftclone;

import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import java.util.Scanner;
import java.io.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

public class MineCraftClone {

  private FPCameraController fp;
  private DisplayMode displayMode;

  public void start() {
    try {
      createWindow();
      initGL();
      fp = new FPCameraController(0f, 0f, 0f);
      fp.gameLoop();//render();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createWindow() throws Exception {
    Display.setFullscreen(false);
    DisplayMode d[] = Display.getAvailableDisplayModes();

    for (int i = 0; i < d.length; i++) {
      if (d[i].getWidth() == 1280  && d[i].getHeight() == 720 && d[i].getBitsPerPixel() == 32) {
        displayMode = d[i];
        break;
      }
    }

    Display.setDisplayMode(displayMode);
    Display.setTitle("MineCraftClone");
    Display.create();
  }

  private void initGL() {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    GLU.gluPerspective(100.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 100.0f);
    glEnable(GL_DEPTH_TEST);
    glMatrixMode(GL_MODELVIEW);
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_TEXTURE_2D);
    glEnableClientState (GL_TEXTURE_COORD_ARRAY);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
  }

  public static void main(String[] args) {
    MineCraftClone mcc = new MineCraftClone();
    mcc.start();
  }

}
