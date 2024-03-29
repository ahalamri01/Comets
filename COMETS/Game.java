package comets;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = -4703569367039006555L;

    public static final int WIDTH = 640, HEIGHT = (WIDTH / 12 * 9);
    private Thread thread;
    private boolean running = false;

    private Random r;
    private Handler handler;
    private HUD hud;
    private Spawn spawner;


    public Game(){
        handler = new Handler();

        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "Let's Build a GAME!", this);

        hud = new HUD();

        spawner = new Spawn(handler, hud);

        r = new Random();

        handler.addObject(new Player(WIDTH/2-32, HEIGHT/2-32, ID.Player, handler));
        //handler.addObject(new Player(WIDTH/2, HEIGHT/2, ID.Player2));
        handler.addObject(new BasicEnemy(WIDTH/2+64, HEIGHT/2-32, ID.BasicEnemy,handler));

        /* for (int i = 0; i < 5; i++) {
            handler.addObject(new BasicEnemy(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.BasicEnemy, handler));
        } */



    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try{
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                tick();
                delta --;
            }
            if (running)
                render();
             frames++;

             if (System.currentTimeMillis() - timer > 1000){
                 timer += 1000;
                 System.out.println("FPS: " + frames);
                 frames = 0;
             }
        }
        stop();
    }

    private void tick(){
        handler.tick();
        hud.tick();
        spawner.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0,0, WIDTH, HEIGHT);


        handler.render(g);
        hud.render(g);


        g.dispose();
        bs.show();
    }

    public static float clamp(float var, float min, float max){
        if (var >= max){
            return var = max;
        } else if (var <= min){
            return var = min;
        } else {
            return var;
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}
