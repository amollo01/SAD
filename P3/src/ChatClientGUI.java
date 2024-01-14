import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatClientGUI {
    // Components de la interfície gràfica
    private JFrame finestra;
    private JTextArea areaMissatge;
    private JTextField entradaMissatge;
    private JList<String> llistaUsr;
    private DefaultListModel<String> llistaUsrGUI;
    private BlockingQueue<String> cuaMissatges;

    public ChatClientGUI() {
        cuaMissatges = new LinkedBlockingQueue<>();

        // Creació de la finestra principal del client de xat
        finestra = new JFrame("Xat client");
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setSize(500, 500);

        // Àrea de text on es mostren els missatges
        areaMissatge = new JTextArea();
        areaMissatge.setEditable(false); 
        areaMissatge.setLineWrap(true); 
        areaMissatge.setWrapStyleWord(true); 
        finestra.add(new JScrollPane(areaMissatge), BorderLayout.CENTER);

        // Camp de text per a escriure missatges
        entradaMissatge = new JTextField();
        entradaMissatge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Afegir el missatge escrit a la cua
                try {
                    cuaMissatges.put(entradaMissatge.getText());
                    entradaMissatge.setText("");
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        finestra.add(entradaMissatge, BorderLayout.SOUTH);

        // Llista que mostra els usuaris connectats
        llistaUsrGUI = new DefaultListModel<>();
        llistaUsr = new JList<>(llistaUsrGUI);
        finestra.add(new JScrollPane(llistaUsr), BorderLayout.EAST);

        // Mostra la finestra
        finestra.setVisible(true);
    }

    // Mètodes per gestionar missatges i llista d'usuaris
    public boolean nouMissatge() {
        return !cuaMissatges.isEmpty();
    }

    public String obtenirMissatge() {
        try {
            return cuaMissatges.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void rebreMissatge(String missatge) {
        // Tracta diferent si el missatge és una llista d'usuaris o un missatge normal
        if (missatge.startsWith("**ç$USUARIS:$ç**")) {
            String[] usuarios = missatge.substring(16).split(",");
            actualitzarLlistsUsr(usuarios);
        } else {
            areaMissatge.append(missatge + "\n");
        }
    }

    public void actualitzarLlistsUsr(String[] usuaris) {
        SwingUtilities.invokeLater(() -> {
            llistaUsrGUI.clear();
            for (String usuari : usuaris) {
                llistaUsrGUI.addElement(usuari);
            }
        });
    }
}