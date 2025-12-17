package balls.view;

import javax.swing.*;
import balls.controller.Controller;
import balls.model.Asteroid;
import balls.model.Player;
import balls.model.ZonaCritica;

import java.awt.*;
import java.util.ArrayList;

public class View extends JFrame {
    private final Controller controller;
    private final ControlPanel controlPanel;
    private final Viewer viewer;
    private final DataPanel dataPanel;

    public View(Controller controller) {
        this.controller = controller;
        this.controlPanel = new ControlPanel(this);
        this.viewer = new Viewer(this);
        this.dataPanel = new DataPanel(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- Panel de control (up-left) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        controlPanel.getFireButton().addActionListener(_ -> controller.addBall());
        controlPanel.getAutofireButton().addActionListener(_ -> controller.autoAddBall());
        controlPanel.getResumeButton().addActionListener(_ -> controller.playAnimation());
        controlPanel.getPauseButton().addActionListener(_ -> controller.pauseAnimation());
        controlPanel.getRestartButton().addActionListener(_ -> controller.stopAnimation());
        content.add(controlPanel, gbc);

        // --- Panel de datos (bottom-left)---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        content.add(dataPanel, gbc);

        // --- Viewer (right) ---
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        content.add(viewer, gbc);

        add(content);
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(() -> {this.requestFocus();});
    }

    public void viewerActivation(){
        viewer.activation();
    }

    public void moveUp(Player player){controller.moveUp(player);}

    public void moveDown(Player player){controller.moveDown(player);}

    public void moveLeft(Player player){controller.moveLeft(player);}

    public void moveRight(Player player){controller.moveRight(player);}

    public ArrayList<ZonaCritica> getAllRooms(){
         return controller.getAllRooms();
    }

    public ArrayList<Player> getAllPlayers(){return controller.getAllPlayers();}

    public ArrayList<Asteroid> getAllBalls() {
        return controller.getAllBalls();
    }

    public int getViewerWidth() {
        return viewer.getWidth();
    }

    public int getViewerHeight() {
        return viewer.getHeight();
    }

    public DataPanel getDataPanel() {
        return dataPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public Controller getController() {
        return controller;
    }

    public Viewer getViewer() {
        return viewer;
    }
}
