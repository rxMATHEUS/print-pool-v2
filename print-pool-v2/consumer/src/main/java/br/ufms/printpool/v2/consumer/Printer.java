package br.ufms.printpool.v2.consumer;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Printer extends Thread {
    private static final String URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "Pool";
    private final ConnectionFactory factory = new ActiveMQConnectionFactory(URL);
    private final int id;
    private final int latency;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final List<String> reportList = new ArrayList<>();

    public Printer(int id, int latency) {
        this.id = id;
        this.latency = latency;
    }

    public Printer(int id) {
        this(id, 1000);
    }

    @Override
    public void run() {
        try (Connection connection = factory.createConnection()) {
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);

            MessageConsumer consumer = session.createConsumer(queue);

            while (true) {
                Message message = consumer.receive(20000);
                if (message == null) {
                    break;
                }
                MapMessage mapMessage = (MapMessage) message;
                long requestTimeMillis = mapMessage.getLong("requestTime");
                String requestTime = dateFormat.format(new Date(requestTimeMillis));
                String file = mapMessage.getString("file");
                int priority = mapMessage.getInt("priority");
                int pid = mapMessage.getInt("pid");
                System.out.println("Impressora " + id + " imprimindo arquivo " + file + " com prioridade " + priority + " do processo " + pid);
                Thread.sleep(latency);
                String printTime = dateFormat.format(new Date());

                String reportEntry = "Hora da requisição: " + requestTime +
                        ", Hora da impressão: " + printTime +
                        ", Processo: " + pid +
                        ", Arquivo: " + file;
                reportList.add(reportEntry);
            }
            System.out.println("\n>>>Relatório da Impressora " + id + "<<<");
            reportList.forEach(System.out::println);
        } catch (JMSException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
