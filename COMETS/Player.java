package comets;

import java.awt.*;
import java.util.Random;

public class Player extends GameObject {

    Random r = new Random();
    Handler handler;

    public Player(int x, int y, ID id, Handler handler){
        super(x, y , id);
        this.handler = handler;

    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    public void tick(){
        x += velX;
        y += velY;

        x = Game.clamp((int) x, 0, Game.WIDTH - 33);
        y = Game.clamp((int) y, 0, Game.WIDTH -219);

        handler.addObject(new Trail((int) x, (int) y, ID.Trail, Color.white , 32, 32, 0.15f, handler));

        collision();

    }

    private void collision(){
        for (int i = 0; i < handler.object.size(); i++){

            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy ){ //tempObject is BasicEnemy

                if (getBounds().intersects(tempObject.getBounds())){
                    //collison code
                    HUD.HEALTH -= 1;
                }
            }

        }
    }

    public void render(Graphics g){
        if(id == ID.Player) {
            g.setColor(Color.WHITE);
        }
        /* if(id == ID.Player2) {
            g.setColor(Color.blue);
        } */

        Graphics2D g2d = (Graphics2D) g;

        //g.setColor(Color.green);
        //g2d.draw(getBounds());

        g.setColor(Color.WHITE);
        g.fillRect((int) x, (int) y,32,32);
    }


}
