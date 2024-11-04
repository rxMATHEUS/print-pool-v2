package br.ufms.printpool.v2.producer;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Process extends Thread {

    private static final String[] FILES = {
            "documento.txt",
            "foto.jpg",
            "relatorio.pdf",
            "dados_2024.xlsx",
            "projeto_code.cpp",
            "apresentacao.pptx",
            "design_logo.psd",
            "musica.mp3",
            "video_editado.mp4",
            "script_automacao.py",
            "configuracao.yaml",
            "modelo_template.docx",
            "backup_arquivo.zip",
            "tabela_info.csv",
            "diagrama_fluxo.vsdx",
            "config_sys.ini",
            "app_instalador.exe",
            "index.html",
            "estilos.css",
            "java_programa.java",
            "imagem_fundo.png",
            "relatorio_analise.docx",
            "audio_gravacao.wav",
            "video_apresentacao.mov",
            "planilha_financeira.ods",
            "script_processamento.r",
            "documento_referencia.odt",
            "arquivo_config.txt",
            "projeto_website.html",
            "lista_compras.md",
            "dicionario_termos.json",
            "logs_sistema.log",
            "backup_db.sql",
            "grafico_vendas.svg",
            "template_email.eml",
            "imagem_projeto.bmp",
            "video_tutorial.avi",
            "modelo_pesquisa.tex",
            "apresentacao_slide.odp",
            "relatorio_execucao.xls",
            "scripts_utilitarios.sh",
            "arquivo_dados.bin",
            "tabela_preco.tsv",
            "sistema_instalador.pkg",
            "documento_reuniao.rtf",
            "audio_musical.aac",
            "video_suporte.mp4",
            "imagem_capitulo.gif",
            "relatorio_finalizado.pdf",
            "config_servidor.conf",
            "projeto_backup.tar.gz"
    };

    private static final String URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "Pool";
    private static final ConnectionFactory factory = new ActiveMQConnectionFactory(URL);
    private final int id;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");


    public Process(int id) {
        this.id = id;
    }

    public int randomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public String randomFileName() {
        return FILES[randomNumber(0, (FILES.length - 1))];
    }

    @Override
    public void run() {
        try (Connection connection = factory.createConnection()) {
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);
            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            int n = randomNumber(1, 9);
            for (int i = 0; i < n; i++) {
                String fileName = randomFileName();
                int priority = randomNumber(1, 5);
                MapMessage message = session.createMapMessage();
                message.setString("file",fileName);
                message.setInt("priority", priority);
                message.setInt("pid", id);
                long requestTime = System.currentTimeMillis();
                message.setLong("requestTime", requestTime);

                String formattedRequestTime = dateFormat.format(new Date(requestTime));

                producer.send(message, DeliveryMode.NON_PERSISTENT, priority, 0);
                System.out.println("Processo " + message.getInt("pid") + " enviando arquivo " + message.getString("file") +
                        " com prioridade " + message.getInt("priority") + " às " + formattedRequestTime);
                Thread.sleep(randomNumber(0, 5000));
            }
        } catch (JMSException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
