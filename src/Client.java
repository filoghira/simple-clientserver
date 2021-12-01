import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket(InetAddress.getByName("localhost"), 8083);

        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        client.getInputStream()
                )
        );

        PrintWriter pw = new PrintWriter(
                client.getOutputStream(),
                true
        );

        pw.println("GET /lol.jpg HTTP/1.1");
        pw.println("Host: localhost:8083");
        pw.println("User-Agent: il pepe magico");
        pw.println();

        String input;
        int msgL = 0;
        while (!(input = br.readLine()).equals("")) {
            System.out.println(input);
            if(input.contains("Content-Length: ")) {
                input = input.substring("Content-Length: ".length());
                msgL = Integer.parseInt(input);
            }
        }

        int i=0;
        char[] msg = new char[msgL];
        while (i<msgL){
            msg[i] = (char) br.read();
            i++;
        }

        System.out.println(msg);
    }
}
