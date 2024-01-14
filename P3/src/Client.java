import javax.swing.*;
import java.io.IOException;

public class Client {
    private static ChatClientGUI chatGUI;
    private static MySocket socket;
    private static String nomUsuari;

    public static void main(String[] args) {
        nomUsuari = JOptionPane.showInputDialog("Introduiex el teu nom d'usuari:");
        socket = new MySocket("localhost", 55555);

        try {
            socket.escriureLinea(nomUsuari); // Envia nom d'usuari al servidor
        } catch (IOException e) {
            e.printStackTrace();
            return; 
        }

        // Creació de la interfície gràfica en un fil d'execució separat
       SwingUtilities.invokeLater(() -> {
chatGUI = new ChatClientGUI();
startThreads();
});
}
private static void startThreads() {
    // Fil per llegir i mostrar missatges del servidor
    new Thread(() -> {
        String linia;
        while ((linia = socket.llegirLinea()) != null) {
            if (chatGUI != null) {
                chatGUI.rebreMissatge(linia);
            }
        }
    }).start();

    // Fil per enviar missatges des de la GUI
    new Thread(() -> {
        while (true) {
            if (chatGUI != null && chatGUI.nouMissatge()) {
                String misatge = chatGUI.obtenirMissatge();
                try {
                    socket.escriureLinea(misatge);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }).start();
}
}