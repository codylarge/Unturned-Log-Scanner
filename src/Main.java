import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) {
        ServerData data = new ServerData();

        readAllLogs(data, 1, 5);

        data.printPlayersMap();
    }

    static void readAllLogs(ServerData data, int min, int max) {
        ArrayList<Account> accounts;
        ArrayList<String> ips;

        for (int i = min; i < max; i++) {
            Log log = new Log(i);
            if(log.getLogFile().exists()) {
                System.out.println("Viewing log " + i);
                accounts = log.extractAccounts();
                ips = log.extractIps();
                if(accounts.size() != ips.size()) {
                    System.out.println();
                }
                data.logConnectionData(ips, accounts);
            }
        }
        // data.printPlayersMap();
    }
}