/*
File: FPCameraController.java, MineCraftClone.java, Vector3Float.java
Block.java, Chunk.java, SimplexNoise.java, SimplexNoise_octave.java
Author: Sharat V. , Maheen Iqbal, Fahad Ahmed
Class: CS 445

Assignment: Final Project Checkpoint 2
Date Last Mod: 11/19/15
*/
package minecraftclone;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import org.lwjgl.util.glu.GLU;


public class FPCameraController {
  private Vector3f position = null;
  private Vector3f lPosition = null;
  private float yaw = 0.0f;
  private float pitch = 0.0f;
  private Chunk chunk = new Chunk(0, 0 ,0);

  public FPCameraController(float x, float y, float z) {
    position = new Vector3f(x, y, z);
    lPosition = new Vector3f(x, y, z);
    lPosition.x = 0f;
    lPosition.y = 0f;
    lPosition.z = 0f;
  }
  public void yaw(float amount) {
    this.yaw += amount;
  }
  public void pitch(float amount) {
    this.pitch -= amount;
  }
  public void walkForward(float distance) {
    float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
    float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
    position.x -= xOffset;
    position.z += zOffset;
  }
  public void walkBackwards(float distance) {
    float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
    float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
    position.x += xOffset;
    position.z -= zOffset;
  }
  public void strafeLeft(float distance) {
    float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
    float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
    position.x -= xOffset;
    position.z += zOffset;
  }
  public void strafeRight(float distance) {
    float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
    float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
    position.x -= xOffset;
    position.z += zOffset;
  }
  public void moveUp(float distance) {
    position.y -= distance;
  }
  public void moveDown(float distance) {
    position.y += distance;
  }
  public void lookThrough() {
    glRotatef(pitch,1.0f,0.0f,0.0f);
    glRotatef(yaw,0.0f,1.0f,0.0f);
    glTranslatef(position.x, position.y, position.z);
  }
  public void gameLoop() {
    FPCameraController camera = new FPCameraController(-30,-55,-95);
    float dx = 0.0f;
    float dy = 0.0f;
    float dt = 0.0f;
    float lastTime = 0.0f;
    long time = 0;
    float mouseSensitivity = 0.09f;
    float movementSpeed = .60f;

    Mouse.setGrabbed(true);
    while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      time = Sys.getTime();
      lastTime = time;

      //Camera view controlled here via mouse.
      dx = Mouse.getDX();
      dy = Mouse.getDY();
      camera.yaw(dx * mouseSensitivity);
      camera.pitch(dy * mouseSensitivity);

      if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
        camera.walkForward(movementSpeed);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
        camera.walkBackwards(movementSpeed);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
        camera.strafeLeft(movementSpeed);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
        camera.strafeRight(movementSpeed);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
        camera.moveUp(movementSpeed);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
        camera.moveDown(movementSpeed);
      }
      glLoadIdentity();
      camera.lookThrough();
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      chunk.render();
      Display.update();
      Display.sync(60);
    }
    Display.destroy();
  }
  /*
  private void render() {
  try {
  glBegin(GL_QUADS);
  //Top
  glColor3f(0.0f,1.0f,0.5f);
  glVertex3f(2.0f, 2.0f, -2.0f);
  glVertex3f(-2.0f, 2.0f, -2.0f);
  glVertex3f(-2.0f, 2.0f, 2.0f);
  glVertex3f(2.0f, 2.0f, 2.0f);

  //Bottom
  glColor3f(0.0f,0.0f,1.0f);
  glVertex3f(2.0f, -2.0f, 2.0f);
  glVertex3f(-2.0f, -2.0f, 2.0f);
  glVertex3f(-2.0f, -2.0f, -2.0f);
  glVertex3f(2.0f, -2.0f, -2.0f);

  //Front
  glColor3f(1.0f,1.0f,1.0f);
  glVertex3f(2.0f, 2.0f, 2.0f);
  glVertex3f(-2.0f, 2.0f, 2.0f);
  glVertex3f(-2.0f, -2.0f, 2.0f);
  glVertex3f(2.0f, -2.0f, 2.0f);

  //Back
  glColor3f(0.0f,1.0f,1.0f);
  glVertex3f(2.0f, -2.0f, -2.0f);
  glVertex3f(-2.0f, -2.0f, -2.0f);
  glVertex3f(-2.0f, 2.0f, -2.0f);
  glVertex3f(2.0f, 2.0f, -2.0f);

  //Left
  glColor3f(1.0f,0.0f,0.0f);
  glVertex3f(-2.0f, 2.0f, 2.0f);
  glVertex3f(-2.0f, 2.0f, -2.0f);
  glVertex3f(-2.0f, -2.0f, -2.0f);
  glVertex3f(-2.0f, -2.0f, 2.0f);

  //Right
  glColor3f(1.0f,0.0f,1.0f);
  glVertex3f(2.0f, 2.0f, -2.0f);
  glVertex3f(2.0f, 2.0f, 2.0f);
  glVertex3f(2.0f, -2.0f, 2.0f);
  glVertex3f(2.0f, -2.0f, -2.0f);
  glEnd();

} catch(Exception e) {
//do nothing
}
}
*/
}
