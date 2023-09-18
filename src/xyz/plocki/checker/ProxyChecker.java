package xyz.plocki.checker;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ProxyChecker {

    private static File file;

    public static void main(String[] args) throws IOException {
        file = new File("proxylist.txt");
        if(!file.exists()) {
            file.createNewFile();
        }
        check();
    }

    private static void check() {
        new Thread(() -> {
            try {
                int i = 0;
                Scanner scanner = new Scanner(file);
                File c = new File("checkedproxies.txt");
                if(!c.exists()) {
                    c.createNewFile();
                }
                while (scanner.hasNext()) {
                    String next = scanner.next();
                    if(next.contains(":")) {
                        if(!(Integer.parseInt(next.split(":")[1]) >= 65534)) {
                            InetSocketAddress address = new InetSocketAddress(next.split(":")[0], Integer.parseInt(next.split(":")[1]));
                            if(address.getAddress().isReachable(1000)) {
                                PrintWriter writer = new PrintWriter(new FileWriter(c, true), true);
                                writer.write("\n " + next);
                                writer.close();
                                i = i+1;
                                System.out.println(i + " proxies are working.");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
