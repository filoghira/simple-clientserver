import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8083);
        ArrayList<ServerThread> clients = new ArrayList<>() ;
        int numMess = 1;
        while(true){
            Socket s = server.accept();
            clients.add(new ServerThread(s, numMess));
            clients.get(clients.size()-1).start();

            numMess++;

        }
    }
}

class ServerThread extends Thread {

    private int numMess;
    private Socket s;

    public ServerThread(Socket s, int numMess){
        this.s = s;
        this.numMess = numMess;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()
                    )
            );

            String input, get = "";
            while (!(input = br.readLine()).equals("")) {
                if (input.startsWith("GET "))
                    get = input;
                System.out.println(input);
            }

            if(get.contains("GET /lol.jpg")) {
                File file = new File("lol.jpg");
                byte[] b = Files.readAllBytes(file.toPath());

                BufferedOutputStream out = new BufferedOutputStream(s.getOutputStream());
                out.write(("HTTP/1.1 200\nContent-Type: image/jpg\nContent-Length: "+b.length+"\n\n").getBytes(StandardCharsets.UTF_8));
                out.write(b);
                out.close();
            }else{
                PrintWriter pw = new PrintWriter(
                        s.getOutputStream(),
                        true
                );

                FileInputStream fstream = new FileInputStream("test.html");
                BufferedReader page = new BufferedReader(new InputStreamReader(fstream));
                String riga, tot = "";
                while ((riga = page.readLine()) != null)
                    tot += riga;

                pw.println("HTTP/1.1 200");
                pw.println("Content-Type: HTML5");
                pw.println("Content-Length: " + tot.length());
                pw.println();
                pw.println(tot);
                pw.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}