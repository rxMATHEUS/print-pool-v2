package br.ufms.printpool.v2.consumer;

import java.util.ArrayList;
import java.util.List;

public class ConsumerApp {

    public static void main(String[] args) {
        List<Printer> printers = new ArrayList<>();
        try {
            for (int id = 1; id <= 2; id++) {
                Printer printer = new Printer(id);
                printers.add(printer);
                printer.start();
            }
            for (Printer printer : printers) {
                printer.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
