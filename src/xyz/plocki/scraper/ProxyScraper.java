package xyz.plocki.scraper;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("all")
public class ProxyScraper {

    private static File toBlock;
    private static long[] id;

    public static void main(String[] args) throws IOException {
        toBlock = new File("/var/www/html/ips-to-block.list");
        id = new long[]{0};
        if(!toBlock.exists()) {
            toBlock.createNewFile();
        }
        check();
    }

    private static void check() {
        new Thread(() -> {
            List<String> urls = new ArrayList<>();
            urls.add("https://www.proxyscan.io/download?type=http");
            urls.add("https://www.proxyscan.io/download?type=https");
            urls.add("https://www.proxyscan.io/download?type=socks4");
            urls.add("https://www.proxyscan.io/download?type=socks5");
            /*urls.add("https://raw.githubusercontent.com/jetkai/proxy-list/main/archive/txt/working-proxies-history.txt");
            urls.add("https://raw.githubusercontent.com/TheSpeedX/PROXY-List/master/http.txt");
            urls.add("https://raw.githubusercontent.com/TheSpeedX/PROXY-List/master/socks4.txt");
            urls.add("https://raw.githubusercontent.com/TheSpeedX/PROXY-List/master/socks5.txt");
            urls.add("https://raw.githubusercontent.com/roosterkid/openproxylist/main/HTTPS_RAW.txt");
            urls.add("https://raw.githubusercontent.com/roosterkid/openproxylist/main/SOCKS4_RAW.txt");
            urls.add("https://raw.githubusercontent.com/roosterkid/openproxylist/main/SOCKS5_RAW.txt");
            urls.add("https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/http.txt");
            urls.add("https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/https.txt");
            urls.add("https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/proxy.txt");
            urls.add("https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/socks4.txt");
            urls.add("https://raw.githubusercontent.com/ShiftyTR/Proxy-List/master/socks5.txt");
            urls.add("https://api.proxyscrape.com/v2/?request=getproxies&protocol=http&timeout=10000&country=all&ssl=all&anonymity=all");
            urls.add("https://api.proxyscrape.com/v2/?request=getproxies&protocol=socks4&timeout=10000&country=all");
            urls.add("https://api.proxyscrape.com/v2/?request=getproxies&protocol=socks5&timeout=10000&country=all");
            urls.add("https://socks4proxy.com/?smd_process_download=1&download_id=207");
            urls.add("https://socks4proxy.com/?smd_process_download=1&download_id=208");
            urls.add("https://socks4proxy.com/?smd_process_download=1&download_id=209");
            urls.add("https://raw.github.com/stamparm/ipsum/master/levels/1.txt");
            urls.add("https://raw.github.com/clarketm/proxy-list/master/proxy-list-raw.txt");
            urls.add("https://gist.githubusercontent.com/cephurs/26b67f9320b23b3dc863/raw/e33a6a880bbc559d562b4f107e7216266cece7ee/blacklist.txt");
            urls.add("https://raw.githubusercontent.com/opsxcq/ipblacklist-database/master/tor-exitnodes");
            urls.add("https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/http.txt");
            urls.add("https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/socks4.txt");
            urls.add("https://raw.githubusercontent.com/monosans/proxy-list/main/proxies/socks5.txt");
            urls.add("https://raw.githubusercontent.com/hookzof/socks5_list/master/proxy.txt");
            urls.add("https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-socks4.txt");
            urls.add("https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-socks5.txt");
            urls.add("https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-http.txt");
            urls.add("https://raw.githubusercontent.com/jetkai/proxy-list/main/online-proxies/txt/proxies-https.txt");*/
            for(;;) {
                urls.forEach(uri -> {
                    System.out.println("\nChecking " + uri + "\n");
                    for (int a = 0; a < 2; a++) {
                        URL url = null;
                        try {
                            url = new URL(uri);
                        } catch (MalformedURLException ignored) {
                        }
                        URLConnection conn = null;
                        try {
                            assert url != null;
                            conn = url.openConnection();
                        } catch (IOException e) {
                            System.out.println("Failed to connect to " + uri);
                        }
                        Scanner in = null;
                        try {
                            assert conn != null;
                            in = new Scanner(conn.getInputStream());
                        } catch (IOException ignored) {
                        }
                        while (true) {
                            assert in != null;
                            if (!in.hasNext()) break;
                            String next = in.next();
                            id[0] = id[0] + 1;
                            try {
                                Scanner scanner = new Scanner(toBlock);
                                boolean b = false;
                                while (scanner.hasNext()) {
                                    if (scanner.next().contains(next)) {
                                        b = true;
                                    }
                                }
                                if (!b) {
                                    if (next.contains(":")) {
                                        if (!(Integer.parseInt(next.split(":")[1]) >= 65534)) {
                                            PrintWriter out = new PrintWriter(new FileWriter(toBlock, true));
                                            out.write("\n" + next.split(":")[0]);
                                            out.flush();
                                            out.close();
                                        }
                                    } else {
                                        PrintWriter out = new PrintWriter(new FileWriter(toBlock, true));
                                        out.write("\n" + next);
                                        out.flush();
                                        out.close();
                                    }
                                }
                                scanner.close();
                            } catch (IOException ignored) {
                            }
                        }
                        in.close();
                    }
                });
                try {
                    Thread.sleep(1000*10);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

}
