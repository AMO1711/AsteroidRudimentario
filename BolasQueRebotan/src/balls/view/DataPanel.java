package balls.view;

import javax.swing.*;
import java.awt.*;

public class DataPanel extends JPanel {
    private final JTable tablainfo;
    private View view;

    public DataPanel(View view){
        this.view = view;
        String[] cabecera = {"Característica", "Valor"};
        String[][] datos = new String[3][2];
        llenarTabla(datos);
        this.tablainfo = new JTable(datos, cabecera);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(tablainfo, gbc);
    }

    public void llenarTabla(String [][] datos){
        datos[0][0] = "Nº bolas";
        datos[1][0] = "FPS";
        datos[2][0] = "T renderización";

        datos[0][1] = String.valueOf(view.getAllBalls().size());
        datos[1][1] = "muy pocos";
        datos[2][1] = "lento";
    }

    public void actualizarTabla(long renderTime){
        int fps = (int) (1000000000 /renderTime);

        tablainfo.setValueAt(String.valueOf(view.getAllBalls().size()), 0, 1);
        tablainfo.setValueAt(String.valueOf(fps), 1, 1);
        tablainfo.setValueAt(String.valueOf(renderTime/1000)+ " us", 2, 1);
    }
}
