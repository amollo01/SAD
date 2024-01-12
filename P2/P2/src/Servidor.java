import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Servidor {
    private static final Map<String, MySocket> server = new HashMap<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        // Creem un servidor escoltant al port 55555
        MyServerSocket serverSocket = new MyServerSocket(55555);

        // Bucle infinit que espera noves connexions i crea un fil per cada client
        while (true) {
            MySocket socket = serverSocket.conectar();

            new Thread() {
                public void run() {
                    try {
                        // Enviar un missatge d'inici al nou client
                        socket.escriureLinea("Servidor--> Si us plau introdueix el teu nom:");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Llegir el nom del client
                    String nom = socket.llegirLinea();
                    try {
                        // Enviar un missatge de benvinguda al client
                        socket.escriureLinea("Servidor --> Hola " + nom + " benvingut al xat de SAD");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Afegir el client al mapa de clients connectats
                    lock.writeLock().lock();
                    try {
                        server.put(nom, socket);
                    } finally {
                        lock.writeLock().unlock();
                    }

                    String linea;

                    // Bucle per rebre i enviar missatges entre clients
                    while ((linea = socket.llegirLinea()) != null) {
                        enviar(linea, nom);
                        System.out.println(nom + " --> " + linea);
                    }

                    // Eliminar el client del mapa quan es desconnecta
                    lock.writeLock().lock();
                    try {
                        server.remove(nom);
                    } finally {
                        lock.writeLock().unlock();
                    }

                    // Tancar la connexió amb el client
                    socket.tancar();
                }
            }.start();
        }
    }

    // Mètode per enviar un missatge a tots els clients excepte el remitent
    public static void enviar(String linea, String nom) {
        MySocket socket;

        lock.readLock().lock();
        try {
            for (Map.Entry<String, MySocket> user : server.entrySet()) {
                socket = user.getValue();
                if (!nom.equals(user.getKey())) {
                    try {
                        socket.escriureLinea(nom + "--> " + linea);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}