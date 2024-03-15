package Security.LAB1;
import java.io.*;
import java.util.*;
import javax.crypto.*;
import java.security.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class security{
    private static final int THREAD_POOL_SIZE = 10;
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_MODE = "ECB";
    private static final String ENCRYPTION_PADDING = "PKCS5Padding";

    private static HashMap<String, BusData> busDataMap;
    private static HashMap<String, Integer> totalPassengersPerDay;
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        busDataMap = new HashMap<>();
        totalPassengersPerDay = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        String[] fileNames = {"Balcova.csv","Karsiyaka.csv", "Karabaglar.csv", "Konak.csv", "Bornova.csv", "Gaziemir.csv", "Urla.csv", "Seferihisar.csv"};

        try {
            SecretKey secretKey = generateKey();
            for (String fileName : fileNames) {
                String encryptedFileName = encryptFileName(fileName, secretKey);
                executorService.submit(new BusDataAnalyzerTask(encryptedFileName, busDataMap, secretKey));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Threads are complete. Here are the statistics for the number of passengers:");
        System.out.println("Day of the week, total passengers, Number of days, Avg Passengers");
        for (String dayOfWeek : busDataMap.keySet()) {
            BusData busData = busDataMap.get(dayOfWeek);

            System.out.println(dayOfWeek + ", " + busData.totalPassengers + ", " + busData.numDays + ", " + busData.getAveragePassengers());
        }

        for (String day : totalPassengersPerDay.keySet()) {
            int totalPassengers = totalPassengersPerDay.get(day);
            System.out.println(day + ", " + totalPassengers);
        }
    }

    private static class BusDataAnalyzerTask implements Runnable {
        private String encryptedFileName;
        private HashMap<String, BusData> busDataMap;
        private SecretKey secretKey;

        public BusDataAnalyzerTask(String encryptedFileName, HashMap<String, BusData> busDataMap, SecretKey secretKey) {
            this.encryptedFileName = encryptedFileName;
            this.busDataMap = busDataMap;
            this.secretKey = secretKey;
        }

        @Override
        public void run() {
            try {
                Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM + "/" + ENCRYPTION_MODE + "/" + ENCRYPTION_PADDING);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                String fileName = decryptFileName(encryptedFileName, cipher);

                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens[0].equals("Date")) {
                        continue;
                    }

                    String dayOfWeek = tokens[1];
                    int numPassengers = Integer.parseInt(tokens[3]);

                    lock.lock();
                    try {
                        BusData busData = busDataMap.getOrDefault(dayOfWeek, new BusData());
                        busData.totalPassengers += numPassengers;
                        busData.numDays++;
                        busData.dates.add(tokens[0]);
                        busDataMap.put(dayOfWeek, busData);

                    } finally {
                        lock.unlock();
                    }
                }
                br.close();
                System.out.println("Processed file: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static class BusData {
        int totalPassengers;
        int numDays;
        List<String> dates;

        public BusData() {
            this.totalPassengers = 0;
            this.numDays = 0;
            this.dates = new ArrayList<>();
        }

        public boolean containsDate(String date) {
            return dates.contains(date);
        }

        public double getAveragePassengers() {
            if (numDays == 0) {
                return 0.0;
            }
            return (double) totalPassengers / numDays;
        }
    }

    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        return keyGenerator.generateKey();
    }

    private static String encryptFileName(String fileName, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM + "/" + ENCRYPTION_MODE + "/" + ENCRYPTION_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(fileName.getBytes());
        return byteArrayToHexString(encryptedBytes);
    }

    private static String decryptFileName(String encryptedFileName, Cipher cipher) throws Exception {
        byte[] encryptedBytes = hexStringToByteArray(encryptedFileName);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}

