package balls.view;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private final View view;
    private final JButton fireButton, autofireButton, restartButton, pauseButton, resumeButton;
    private final JPanel panelMovimiento;

    public ControlPanel(View view) {
        this.view = view;
        this.panelMovimiento = new JPanel();
        this.fireButton = new JButton("Disparar Bola");
        this.autofireButton = new JButton("Disparar bolas automaticamente");
        this.restartButton = new JButton("Reiniciar");
        this.pauseButton = new JButton("Pausar");
        this.resumeButton = new JButton("Reanudar");
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints gbc2 = new GridBagConstraints();
        setLayout(new GridBagLayout());
        panelMovimiento.setLayout(new GridBagLayout());

        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(fireButton, gbc);

        gbc.gridy = 1;
        add(autofireButton, gbc);

        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weighty = 1;
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        panelMovimiento.add(resumeButton, gbc2);
        
        gbc2.gridx = 1;
        panelMovimiento.add(pauseButton, gbc2);
        
        gbc2.gridx = 2;
        panelMovimiento.add(restartButton, gbc2);

        gbc.gridy = 2;
        add(panelMovimiento,gbc);
    }

    public JButton getFireButton() {
        return this.fireButton;
    }

    public JButton getAutofireButton() {
        return autofireButton;
    }

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JButton getResumeButton() {
        return resumeButton;
    }
}
