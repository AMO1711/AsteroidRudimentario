package balls.view;

import balls.model.Asteroid;
import balls.model.Player;
import balls.model.ZonaCritica;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {
    private final Thread thread;
    private final View view;
    private BufferStrategy bufferStrategy;

    public Viewer(View view) {
        this.view = view;

        setBackground(Color.WHITE);
        setFocusable(true);
        setIgnoreRepaint(true);

        for (Player p : view.getAllPlayers()) {
            addKeyListener(p);
        }

        thread = new Thread(this);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
    }

    @Override
    public void run() {
        while (true) {
            if (bufferStrategy == null) return;

            long start = System.nanoTime(), end, renderTime;

            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            ArrayList<Asteroid> asteroids = view.getAllBalls();
            ArrayList<ZonaCritica> rooms = view.getAllRooms();
            ArrayList<Player> players = view.getAllPlayers();

            paintRooms(rooms, g);
            paintPlayers(players, g);
            paintBall(asteroids, g);

            g.dispose();
            bufferStrategy.show();

            end = System.nanoTime();
            renderTime = end - start;

            view.getDataPanel().actualizarTabla(renderTime);
        }
    }

    public Thread getThread() {
        return this.thread;
    }

    public void paintBall(ArrayList<Asteroid> asteroids, Graphics2D g) {
        int diameter;

        for (Asteroid asteroid:asteroids){
            diameter = asteroid.getDIAMETER();
            g.setColor(asteroid.getCOLOR());
            g.fillOval(asteroid.getModeloFisico().pv.posicion.x,
                    asteroid.getModeloFisico().pv.posicion.y,
                    diameter, diameter);
        }
    }

    public void paintRooms(ArrayList<ZonaCritica> rooms, Graphics2D g){
        for (ZonaCritica room:rooms){
            g.setColor(Color.gray);
            g.drawRect(room.getPosicion().x, room.getPosicion().y, room.getDimensiones().x, room.getDimensiones().y);
        }
    }

    public void paintPlayers(ArrayList<Player> players, Graphics2D g){
        for (Player player: players){
            try {
                BufferedImage imagen = ImageIO.read(new File(player.getImagen()));
                g.drawImage(imagen,
                        player.getModeloFisico().pv.posicion.x,
                        player.getModeloFisico().pv.posicion.y,
                        player.getDIAMETER(), player.getDIAMETER(), null);
            } catch (IOException e) {
                g.setColor(player.getCOLOR());
                g.fillOval(player.getModeloFisico().pv.posicion.x,
                        player.getModeloFisico().pv.posicion.y,
                        player.getDIAMETER(), player.getDIAMETER());
            }
        }
    }
}
