import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Replace with your actual API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Currency Converter");

        // Input base currency
        System.out.print("Enter the base currency code (e.g., USD, EUR): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        // Input target currency
        System.out.print("Enter the target currency code (e.g., USD, EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Input amount
        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            // Fetch exchange rates
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

            if (exchangeRate == -1) {
                System.out.println("Error: Unable to fetch exchange rate. Please check currency codes.");
                return;
            }

            // Perform conversion
            double convertedAmount = amount * exchangeRate;

            // Display result
            System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) throws Exception {
        // Build the URL
        URL url = new URL(API_URL + baseCurrency);

        // Open connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Read response
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Parse JSON response
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

        if (rates.has(targetCurrency)) {
            return rates.getDouble(targetCurrency);
        } else {
            return -1; // Indicates an error
        }
    }
}