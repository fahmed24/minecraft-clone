/*
File: FPCameraController.java, MineCraftClone.java, Vector3Float.java
Block.java, Chunk.java, SimplexNoise.java, SimplexNoise_octave.java
Author: Sharat V. , Maheen Iqbal, Fahad Ahmed
Class: CS 445

Assignment: Final Project Checkpoint 2
Date Last Mod: 11/19/15
*/
package minecraftclone;
public class Block {

  private boolean isActive;
  private BlockType type;
  private float x, y, z;

  public enum BlockType {
    BlockType_Grass(0),
    BlockType_Sand(1),
    BlockType_Water(2),
    BlockType_Wood(3),
    BlockType_Stone(4),
    BlockType_Bedrock(5);

    private int BlockID;
    BlockType(int i) { BlockID = i; }
    public int getID() { return BlockID; }
    public void setID(int i) { BlockID = i; }
  }

  public Block (BlockType type) { this.type = type; }

  public void setCoordinates(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public boolean isActive() { return isActive; }
  public void setActive() { this.isActive = true; }
  public int getID() { return this.type.getID(); }
}
