import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Servidor {
    // Mapa per emmagatzemar els usuaris i els seus sockets associats
    private static final Map<String, MySocket> servidor = new HashMap<>();
    // Bloqueig per a control de concurrencia
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        // Creació del socket del servidor en el port 55555
        MyServerSocket serverSocket = new MyServerSocket(55555);

        while (true) {
            // Accepta una nova connexió
            MySocket socket = serverSocket.conectar();

            // Crea un nou fil per a cada client connectat
            new Thread(() -> {
                String nom = socket.llegirLinea(); 
                try {
                    socket.escriureLinea("Servidor --> Hola " + nom + " benvingut al xat de SAD");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Bloc per a l'actualització segura del mapa d'usuaris
                lock.writeLock().lock();
                try {
                    servidor.put(nom, socket);
                } finally {

                    lock.writeLock().unlock();
                }

                try {
                    Thread.sleep(500); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Enviar la llista d'usuaris actualitzada
                enviarLlistaUsuaris(); 

                // Llegir i processar els missatges del client
                String linia;
                while ((linia = socket.llegirLinea()) != null) {
                    enviar(linia, nom);
                    System.out.println(nom + " --> " + linia);
                }

                // Bloc per a la desconnexió segura de l'usuari
                lock.writeLock().lock();
                try {
                    servidor.remove(nom);
                } finally {
                    lock.writeLock().unlock();
                }

                // Enviar la llista d'usuaris actualitzada després de la desconnexió
                enviarLlistaUsuaris(); 
                socket.tancar(); // Tancar la connexió del socket
            }).start();
        }
    }

    // Mètode per enviar missatges a tots els usuaris excepte a qui l'ha enviat
    public static void enviar(String linia, String nom) {
        lock.readLock().lock();
        try {
            for (Map.Entry<String, MySocket> usuari : servidor.entrySet()) {
                MySocket socket = usuari.getValue();
                if (!nom.equals(usuari.getKey())) {
                    try {
                        socket.escriureLinea(nom + "-->" + linia);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    // Mètode per enviar la llista actualitzada d'usuaris a tots els clients
    public static void enviarLlistaUsuaris() {
        lock.readLock().lock();
        try {
            // Crear una llista amb els noms d'usuaris
            String llistaUsuaris = String.join(",", servidor.keySet());
            for (MySocket socket : servidor.values()) {
                // Enviar la llista a cada client
                try {
                    socket.escriureLinea("**ç$USUARIS:$ç**" + llistaUsuaris);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}