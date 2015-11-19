/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minecraftclone;

/**
 *
 * @author sharatv
 */
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
