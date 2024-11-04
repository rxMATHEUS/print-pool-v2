package br.ufms.printpool.v2.producer;

import java.util.ArrayList;
import java.util.List;

public class ProducerApp {

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        try {
            for (int id = 1; id <= 4; id++) {
                Process process = new Process(id);
                processes.add(process);
                process.start();
            }
            for (Process process : processes) {
                process.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
