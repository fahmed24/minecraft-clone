/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package minecraftclone;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

//need SlickUtil library from BlackBoard to use these imports:
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Chunk {
  static final int CHUNK_SIZE = 30;
  static final int CUBE_LENGTH = 2;
  private Block[][][] blocks;
  private int VBOVertexHandle;
  private int VBOColorHandle;
  private int VBOTextureHandle;
  private Texture texture;
  private int startX, startY, startZ;

  private Random r;

  public void render() {
    glPushMatrix();
    glPushMatrix();
    glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
    glVertexPointer(3, GL_FLOAT, 0, 0L);
    glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
    glColorPointer(3, GL_FLOAT, 0, 0L);
    glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
    glBindTexture(GL_TEXTURE_2D, 1);
    glTexCoordPointer(2,GL_FLOAT,0,0L);
    glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
    glPopMatrix();
  }

  public void rebuildMesh(float startX, float startY, float startZ) {
    VBOColorHandle = glGenBuffers();
    VBOVertexHandle = glGenBuffers();
    VBOTextureHandle = glGenBuffers();

    FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
    FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
    FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE*CHUNK_SIZE *CHUNK_SIZE)* 6 * 12);

    //SimplexNoise Object declaration
    SimplexNoise noise = new SimplexNoise(900, .45, 0);
    int height = 0;
    for(int x = 0; x < CHUNK_SIZE; x++) {
      for(int z = 0; z < CHUNK_SIZE; z++) {
        height = (int)(startY + (int)(100 * Math.abs(noise.getNoise(x,z))) * CUBE_LENGTH / 2) + 10;
        for(int y = 0; y < CHUNK_SIZE; y++) {
          if (y <= height) {
            VertexPositionData.put(createCube((float)(startX + x * CUBE_LENGTH),
            (float)(y * CUBE_LENGTH + (int)(CHUNK_SIZE * 0.6)),
            (float)(startZ + z * CUBE_LENGTH)));
            VertexColorData.put(createCubeVertexCol(getCubeColor(blocks[(int)x]
              [(int)y][(int)z])));
            VertexTextureData.put(createTexCube((float) 0, (float)0,
              blocks[(int)(x)][(int) (y)][(int) (z)]));
          }
        }
      }
    }

    VertexColorData.flip();
    VertexPositionData.flip();
    VertexTextureData.flip();
    glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
    glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
    glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
    glBufferData(GL_ARRAY_BUFFER, VertexTextureData,
    GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
  }

  private float[] createCubeVertexCol(float[] CubeColorArray) {
    float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
    for (int i = 0; i < cubeColors.length; i++) {
      cubeColors[i] = CubeColorArray[i %
      CubeColorArray.length];
    }
    return cubeColors;
  }

  public static float[] createCube(float x, float y, float z) {
    int offset = CUBE_LENGTH / 2;
    return new float[] {
      // TOP QUAD
      x + offset, y + offset, z,
      x - offset, y + offset, z,
      x - offset, y + offset, z - CUBE_LENGTH,
      x + offset, y + offset, z - CUBE_LENGTH,
      // BOTTOM QUAD
      x + offset, y - offset, z - CUBE_LENGTH,
      x - offset, y - offset, z - CUBE_LENGTH,
      x - offset, y - offset, z,
      x + offset, y - offset, z,
      // FRONT QUAD
      x + offset, y + offset, z - CUBE_LENGTH,
      x - offset, y + offset, z - CUBE_LENGTH,
      x - offset, y - offset, z - CUBE_LENGTH,
      x + offset, y - offset, z - CUBE_LENGTH,
      // BACK QUAD
      x + offset, y - offset, z,
      x - offset, y - offset, z,
      x - offset, y + offset, z,
      x + offset, y + offset, z,
      // LEFT QUAD
      x - offset, y + offset, z - CUBE_LENGTH,
      x - offset, y + offset, z,
      x - offset, y - offset, z,
      x - offset, y - offset, z - CUBE_LENGTH,
      // RIGHT QUAD
      x + offset, y + offset, z,
      x + offset, y + offset, z - CUBE_LENGTH,
      x + offset, y - offset, z - CUBE_LENGTH,
      x + offset, y - offset, z };
    }

    private float[] getCubeColor(Block block) {
      return new float[] { 1, 1, 1 };
    }


    public Chunk(int startX, int startY, int startZ) {

      try {
        texture = TextureLoader.getTexture("PNG",
        ResourceLoader.getResourceAsStream("terrain.png"));
      }
      catch(Exception e) {
        System.out.print("ER-ROAR!");
      }

      r= new Random(System.currentTimeMillis());
      blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
      for (int x = 0; x < CHUNK_SIZE; x++) {
        for (int y = 0; y < CHUNK_SIZE; y++) {
          for (int z = 0; z < CHUNK_SIZE; z++) {
            float f = r.nextFloat();
            if (f > 0.85f)
            blocks[x][y][z] = new Block (Block.BlockType.BlockType_Grass);
            else if (f > 0.7f)
            blocks[x][y][z] = new Block (Block.BlockType.BlockType_Wood);
            else if (f > 0.55f)
            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
            else if (f > 0.4f)
            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
            else if (f > 0.2f)
            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
            else
            blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
          }
        }
      }
      VBOColorHandle = glGenBuffers();
      VBOVertexHandle = glGenBuffers();
      VBOTextureHandle = glGenBuffers();
      this.startX = startX;
      this.startY = startY;
      this.startZ = startZ;
      rebuildMesh(startX, startY, startZ);
    }

    public static float[] createTexCube(float x, float y, Block block) {
      float offset = (1024f/16)/1024f;

      switch(block.getID()) {
        case 0: //grass
        return new float[] {

          // TOP!
          x + offset*3, y + offset*10,
          x + offset*2, y + offset*10,
          x + offset*2, y + offset*9,
          x + offset*3, y + offset*9,

          // BOTTOM QUAD(DOWN=+Y)
          x + offset*3, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*0,
          x + offset*3, y + offset*0,
          // FRONT QUAD
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1,
          // BACK QUAD
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          // LEFT QUAD
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1,
          // RIGHT QUAD
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1
        };

        case 1: //sand
        return new float[] {

          // TOP!
          x + offset*2, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*2,
          x + offset*2, y + offset*2,

          // BOTTOM QUAD(DOWN=+Y)
          x + offset*2, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*2,
          x + offset*2, y + offset*2,
          // FRONT QUAD
          x + offset*2, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*2,
          x + offset*2, y + offset*2,
          // BACK QUAD
          x + offset*2, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*2,
          x + offset*2, y + offset*2,
          // LEFT QUAD
          x + offset*2, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*2,
          x + offset*2, y + offset*2,
          // RIGHT QUAD
          x + offset*2, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*2,
          x + offset*2, y + offset*2
        };
        case 2: //water
        return new float[] {

          // TOP!
          x + offset*14, y + offset*0,
          x + offset*15, y + offset*0,
          x + offset*15, y + offset*1,
          x + offset*14, y + offset*1,

          // BOTTOM QUAD(DOWN=+Y
          x + offset*14, y + offset*0,
          x + offset*15, y + offset*0,
          x + offset*15, y + offset*1,
          x + offset*14, y + offset*1,
          // FRONT QUAD
          x + offset*14, y + offset*0,
          x + offset*15, y + offset*0,
          x + offset*15, y + offset*1,
          x + offset*14, y + offset*1,
          // BACK QUAD
          x + offset*14, y + offset*0,
          x + offset*15, y + offset*0,
          x + offset*15, y + offset*1,
          x + offset*14, y + offset*1,
          // LEFT QUAD
          x + offset*14, y + offset*0,
          x + offset*15, y + offset*0,
          x + offset*15, y + offset*1,
          x + offset*14, y + offset*1,
          // RIGHT QUAD
          x + offset*14, y + offset*0,
          x + offset*15, y + offset*0,
          x + offset*15, y + offset*1,
          x + offset*14, y + offset*1,
        };
        case 3: //wood
        return new float[] {

          // TOP!
          x + offset*5, y + offset*1,
          x + offset*6, y + offset*1,
          x + offset*6, y + offset*2,
          x + offset*5, y + offset*2,

          // BOTTOM QUAD(DOWN=+Y)
          x + offset*5, y + offset*1,
          x + offset*6, y + offset*1,
          x + offset*6, y + offset*2,
          x + offset*5, y + offset*2,
          // FRONT QUAD
          x + offset*4, y + offset*1,
          x + offset*5, y + offset*1,
          x + offset*5, y + offset*2,
          x + offset*4, y + offset*2,
          // BACK QUAD
          x + offset*4, y + offset*1,
          x + offset*5, y + offset*1,
          x + offset*5, y + offset*2,
          x + offset*4, y + offset*2,
          // LEFT QUAD
          x + offset*4, y + offset*1,
          x + offset*5, y + offset*1,
          x + offset*5, y + offset*2,
          x + offset*4, y + offset*2,
          // RIGHT QUAD
          x + offset*4, y + offset*1,
          x + offset*5, y + offset*1,
          x + offset*5, y + offset*2,
          x + offset*4, y + offset*2,
        };
        case 4: //stone
        return new float[] {

          // TOP!
          x + offset*1, y + offset*0,
          x + offset*2, y + offset*0,
          x + offset*2, y + offset*1,
          x + offset*1, y + offset*1,

          // BOTTOM QUAD(DOWN=+Y)
          x + offset*1, y + offset*0,
          x + offset*2, y + offset*0,
          x + offset*2, y + offset*1,
          x + offset*1, y + offset*1,
          // FRONT QUAD
          x + offset*1, y + offset*0,
          x + offset*2, y + offset*0,
          x + offset*2, y + offset*1,
          x + offset*1, y + offset*1,
          // BACK QUAD
          x + offset*1, y + offset*0,
          x + offset*2, y + offset*0,
          x + offset*2, y + offset*1,
          x + offset*1, y + offset*1,
          // LEFT QUAD
          x + offset*1, y + offset*0,
          x + offset*2, y + offset*0,
          x + offset*2, y + offset*1,
          x + offset*1, y + offset*1,
          // RIGHT QUAD
          x + offset*1, y + offset*0,
          x + offset*2, y + offset*0,
          x + offset*2, y + offset*1,
          x + offset*1, y + offset*1,
        };
        case 5: //bedrock
        return new float[] {

          // TOP!
          x + offset*1, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*2,
          x + offset*1, y + offset*2,

          // BOTTOM QUAD(DOWN=+Y)
          x + offset*1, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*2,
          x + offset*1, y + offset*2,
          // FRONT QUAD
          x + offset*1, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*2,
          x + offset*1, y + offset*2,
          // BACK QUAD
          x + offset*1, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*2,
          x + offset*1, y + offset*2,
          // LEFT QUAD
          x + offset*1, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*2,
          x + offset*1, y + offset*2,
          // RIGHT QUAD
          x + offset*1, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*2,
          x + offset*1, y + offset*2,
        };
        default:    //grass is default
        return new float[] {

          // TOP!
          x + offset*3, y + offset*10,
          x + offset*2, y + offset*10,
          x + offset*2, y + offset*9,
          x + offset*3, y + offset*9,

          // BOTTOM QUAD(DOWN=+Y)
          x + offset*3, y + offset*1,
          x + offset*2, y + offset*1,
          x + offset*2, y + offset*0,
          x + offset*3, y + offset*0,
          // FRONT QUAD
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1,
          // BACK QUAD
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1,
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          // LEFT QUAD
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1,
          // RIGHT QUAD
          x + offset*3, y + offset*0,
          x + offset*4, y + offset*0,
          x + offset*4, y + offset*1,
          x + offset*3, y + offset*1
        };
      }
    }
  }
