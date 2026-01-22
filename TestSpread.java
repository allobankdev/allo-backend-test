import com.allobank.idr_rate_aggregator.util.SpreadCalculator;

public class TestSpread {
    public static void main(String[] args) {
        SpreadCalculator calc = new SpreadCalculator();
        String[] usernames = {"aha", "boasa", "cantika", "darwin", "eesha", "fajar", "gilang", "hanna"};
        
        for (String username : usernames) {
            double factor = calc.calculateSpreadFactor(username);
            System.out.println(username + ": " + factor);
        }
    }
}
