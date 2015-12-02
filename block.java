package minecraftclone;

public class block {
  private boolean isActive;
  private BlockType type;
  private float x, y, z;

  public enum BlockType {

    BlockType_Grass(0),
    BlockType_Sand(1),
    BlockType_Water(2),
    BlockType_Dirt(3),
    BlockType_Stone(4),
    BlockType_Bedrock(5);

    private int BlockID;

    BlockType(int i) {
      BlockID = i;
    }

    public int GetID() {
      return BlockID;
    }

    public void SetID(int i) {
      BlockID = i;
    }

  }

  public block(BlockType type) {
    this.type = type;
  }

  public void setCoords(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void setType(BlockType newType)
  {
    type = newType;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public int getID() {
    return type.GetID();
  }

}
