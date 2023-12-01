import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Program Name
 * <p>
 * brief description of the program
 *
 * @author Shrish Mahesh, L20
 * @version date
 */
public class MarketplaceServer {
    public static void main(String[] args) throws IOException{
         ServerSocket serverSocket = new ServerSocket(8080);
        while(true)
        {

            Socket socket = serverSocket.accept();
            MarketplaceServerThread st = new MarketplaceServerThread(socket);
            st.start();
        }
    }


     synchronized public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line = bfr.readLine();
            while (line != null) {
                list.add(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    synchronized public static void writeFile(ArrayList<String> lines, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename, false))) {
            for (int i = 0; i < lines.size(); i++) {
                pw.println(lines.get(i));
            }
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}