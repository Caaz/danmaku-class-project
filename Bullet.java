import java.awt.*;
public class Bullet {
  public double size = 120;
  //public boolean rotates = false;
  //public float roation = 0;
  // Pattern pattern = new Pattern();
  int pattern[] = {0,0};
  //Color color = new Color(255,255,255);
  public long life = 0;
  public long birth = 0;
  public float[] position = {0,0}; // Angle, Distance
  public float[] offset = {0,0}; // Angle, Distance
  public float[] originO = {0,0}; // Angle, Distance
  public float[] origin = {0,0}; // X, Y
  public double hitbox = 2;
  public boolean living = false;
  public boolean friendly = false;
  public Bullet() { } // This probably won't do anything.
  public Bullet(boolean friendly, long birth, long life, float origin[], float offset[], int pattern[]) {
    this.friendly = friendly;
    this.birth = birth;
    this.life = life;
    for(int i = 0; i < 2; i++) { 
      this.origin[i] = origin[i];
      this.originO[i] = offset[i];
      this.pattern[i] = (int)(pattern[i]); 
    }
    // Bring it to life!
    living = true;
    update(birth+1);
  }
  public void update(long sysTime) {
    // Every in game tick, the bullet gets updated here
    // Let's calculate how long the bullet has been alive here (We can later use this in calculations)
    long lifeTime = sysTime - birth;
    if(life > lifeTime) {
      // Here we calculate the x and y coordinates of the bullet by getting the angle and distance and doing magic.
      float oldPos[] = position;
      position[0] = (float)(origin[0] + getDistance(lifeTime) * Math.cos(getAngle(lifeTime)+originO[0]));
      position[1] = (float)(origin[1] + getDistance(lifeTime) * Math.sin(getAngle(lifeTime)+originO[0]));
    }
    // If we've exceeded the lifetime, then die.
    else { die(); }
    
    
    // This checks if it's offscreen and kills it if it is, but I don't know if that's a good idea.
    // for(int i = 0; i<2; i++) { if((position[i] < 0) || position[i] > 500) { die(); break; } }
  }
  public float getAngle(long lifeTime) {
    // North
    if(pattern[0] == 0) { return (float)(Math.toRadians(offset[0]+270)); }
    // Spiral, three times.
    else if(pattern[0] == 1) { return (float)Math.toRadians(offset[0]+(float)(lifeTime)/(float)(life)*360*3); }
    // Moves downward, stops for a while, then continues on downward
    if(pattern[0] == 2) { 
      float percent = (float)(lifeTime/life);
      if(percent < .3) {
        return (float)(-(Math.pow(percent*100.0-30.0,2)/5.0)+150+offset[1]);
      }
      else if(percent < .6) {
        return (float)(150+offset[1]);
      }
      else {
        return (float)(Math.pow(percent*100.0-60.0,2)/2.0+150+offset[1]);
      }
    }
    // This last one is essentially the else.
    return (float)0;
  }
  public float getDistance(long lifeTime) {
    // Linear, speed depends on the lifetime of the bullet.
    if(pattern[1] == 0) { return offset[1]+(float)((float)(lifeTime)/(float)(life)*500.0); }
    
    // Else, static.
    return (float)0;
  }
  public void die() {
    // Very simplistic.
    living = false;
  }
  public void draw(Graphics2D g2d, int[] screen, View helper) {
    float scale = (float)(screen[1]/500.0*size/32.0);                               // Scale the bullet shape
    g2d.translate(position[0]/500*screen[1],position[1]/500*screen[1]);             // Translate to the position of the bullet.
    g2d.rotate(offset[0]);                                                          // Rotate!
    int bullet[][] = {{0,1,0,-1,0},{-1,0,1,0,-1}};                                  // Bullet shape.
    helper.drawPolygon(g2d,bullet,scale,new Color(0,0,0), new Color((friendly)?0:255,0,(friendly)?255:0));  // Draw it.
    g2d.rotate(-offset[0]);                                                         // Rotate!
    g2d.translate(-(position[0]/500*screen[1]),-(position[1]/500*screen[1]));       // Translate back to original position.
  }
}















