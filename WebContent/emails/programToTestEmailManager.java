package programa;

import classes.EmailManager;
import javax.mail.*;

public class Main {

    public static String protocol = "pop3s";
    public static String server = "pop.gmail.com";
    public static int port = 995;
    public static String username = "neurominertests";
    public static String password = "alemarme";
    public static StringBuffer bodymsg;

    public static StringBuffer getMailContent(Part part) throws Exception {
        StringBuffer bodytext = new StringBuffer();

        try {
            String contenttype = part.getContentType();

            int nameindex = contenttype.indexOf("name");
            boolean conname = false;
            if (nameindex != -1) {
                conname = true;
            }

            if (part.isMimeType("text/plain") && !conname) {
                bodytext.append((String) part.getContent());
            } else if (part.isMimeType("text/html") && !conname) {
                bodytext.append((String) part.getContent());
            } else if (part.isMimeType("Multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    getMailContent(multipart.getBodyPart(i));
                }
            } else if (part.isMimeType("message/rfc822")) {
                getMailContent((Part) part.getContent());
            } else {
            }


        } catch (Exception e) {
            System.out.println("getMailContent Error " + e);
        }
        return bodytext;
    }

    public static void main(String[] args) {

        bodymsg = new StringBuffer();
        EmailManager em = new EmailManager(protocol, server, port, username, password);
        Message[] messages = em.getEmails();
        String tipo;


        if (messages == null) {
            System.out.println("Erro capturando mensagens");
        } else {
            try {
                for (int i = 0, n = messages.length; i < n; i++) {
                    Message msg = messages[i];

                    bodymsg.delete(0, bodymsg.length());

                    System.out.println("-----------------------------------------");
                    System.out.println("MENSAGEM NÚMERO " + i + ":");
                    System.out.println("Data de Envio : " + msg.getSentDate());
                    System.out.println("De : " + msg.getFrom()[0]);
                    System.out.println("Para : " + msg.getReplyTo()[0]);
                    System.out.println("Assunto : " + msg.getSubject());
                    tipo = msg.getContentType();
                    System.out.println("Tipo do Conteúdo : " + tipo);
                    System.out.println("Conteúdo : ");


                    if (tipo.indexOf("multipart") != -1) {
                        Multipart multipart = (Multipart) msg.getContent();

                        for (int j = 0; j < multipart.getCount(); j++) {
                            Part p = multipart.getBodyPart(j);
                            bodymsg.append(getMailContent(p));
                        }

                        System.out.println(bodymsg.toString());
                    } else if (msg.isMimeType("text/plain")) {
                        System.out.println(msg.getContent().toString());
                    }

                    System.out.println("-----------------------------------------\n\n");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                em.closeFolder();
            }
        }
    }
}
