package huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

class HuffmanNode implements Comparable<HuffmanNode> {
    int weight;
    char karakter;
    HuffmanNode sol;
    HuffmanNode sag;

    public HuffmanNode(int weight, char character) {
        this.weight = weight;
        this.karakter = character;
        sol = null;
        sag = null;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.weight - other.weight;
    }
}

public class HuffmanCoding {
    public static Map<Character, String> buildHuffmanTree(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.add(new HuffmanNode(entry.getValue(), entry.getKey()));
        }

        while (pq.size() > 1) {
            HuffmanNode lo = pq.poll();
            HuffmanNode hi = pq.poll();
            HuffmanNode parent = new HuffmanNode(lo.weight + hi.weight, '-');
            parent.sol = lo;
            parent.sag = hi;
            pq.add(parent);
        }

        Map<Character, String> huffmanCodes = new HashMap<>();
        huffmannn(huffmanCodes, pq.peek(), "");
        return huffmanCodes;
    }

    private static void huffmannn(Map<Character, String> huffmanCodes, HuffmanNode node, String code) {
        if (node == null) {
            return;
        }

        if (node.karakter != '-') {
            huffmanCodes.put(node.karakter, code);
        }

        huffmannn(huffmanCodes, node.sol, code + "0");
        huffmannn(huffmanCodes, node.sag, code + "1");
    }

    public static String encodeMessage(String message, Map<Character, String> huffmanCodes) {
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            encodedMessage.append(huffmanCodes.get(c));
        }
        return encodedMessage.toString();
    }

    public static int paketin_hesaplanmasi(String mesaj, int packetSize) {
        return (int) Math.ceil(mesaj.length() / (double) packetSize);
    }

    public static boolean isMessageValid(String message, Map<Character, String> huffmanCodes) {
        int i = 0;
        int n = message.length();
        while (i < n) {
            boolean foundCode = false;
            for (int j = i + 1; j <= n; j++) {
                String code = message.substring(i, j);
                if (huffmanCodes.containsValue(code)) {
                    foundCode = true;
                    i = j;
                    break;
                }
            }
            if (!foundCode) {
                return false;
            }
        }
        return true;
    }

    public static double calculateCompressionRatio(String originalMessage, String encodedMessage) {
        int mesaj_boyutu = originalMessage.length() * 8;
        int encodedSize = encodedMessage.length();
        return ((mesaj_boyutu - encodedSize) / (double) mesaj_boyutu) * 100;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("mesaji giriniz ");
        String message = scanner.nextLine();

        Map<Character, String> huffmanCodes = buildHuffmanTree(message);
        System.out.println("Karakterlerin kodlari: " + huffmanCodes);

        String encodedMessage = encodeMessage(message, huffmanCodes);
        System.out.println("Kodlanmis mesaj: " + encodedMessage);

        double compressionRatio = calculateCompressionRatio(message, encodedMessage);
        System.out.println("Sikistirma yuzdesi: " + compressionRatio);

        System.out.print("Paket boyutunu giriniz: ");
        int packetSize = scanner.nextInt();

        int paket_miktari = paketin_hesaplanmasi(encodedMessage, packetSize);
        System.out.println("Paket sayï¿½sï¿½: " + paket_miktari);

        scanner.nextLine();
        System.out.print("Gelen sýkýþtýrlmýþ mesaji girin: ");
        String receivedMessage = scanner.nextLine();

        boolean isValid = isMessageValid(receivedMessage, huffmanCodes);
        System.out.println("Gelen mesaj uygun: " + isValid);
    }
}
