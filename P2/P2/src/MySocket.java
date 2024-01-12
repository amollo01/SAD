import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

public class MySocket 
{

    private Socket socket;

    // Constructor que crea una connexió a un host i port específics
    public MySocket(String host, int port) 
    {
        try 
        {
            socket = new Socket(host, port);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    // Constructor que rep una connexió de servidor existent
    public MySocket(Socket s) 
    {
        socket = s;
    }

    // Mètode per llegir una línia de text del flux d'entrada del socket
    public String llegirLinea() 
    {
        try 
        {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            return reader.readLine();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }

    // Mètode per escriure una línia de text al flux de sortida del socket
    public void escriureLinea(String line) throws IOException 
    {
        try 
        {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(line);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    // Mètode per tancar la connexió del socket
    public void tancar() 
    {
        try 
        {
            socket.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
