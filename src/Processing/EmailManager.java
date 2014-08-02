package Processing;

import java.util.*;
import javax.mail.*;

public class EmailManager
{
    private String protocol;
    private String host;
    private int    port;
    private String username;
    private String password;

    Store store;
    Folder folder;

    public EmailManager(String protocol,String host,int port,String username, String password)
    {
        this.protocol = protocol;
        this.host     = host;
        this.port     = port;
        this.username = username;
        this.password = password;
    }

    public Message[] getEmails()
    {
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);

        try
        {
            // Definindo o protocolo e as informações para conexão
            store = session.getStore(protocol);
            store.connect(host, port, username, password);

            // Abrindo pasta somente para leitura
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            // Capturando mensagens da pasta
            Message resposta[] = folder.getMessages();
            
            return resposta;
        }
        catch (NoSuchProviderException ex) 
        {
            System.out.println(ex.getMessage());
            return null;
        }
        catch (MessagingException ex)
        {
            System.out.println(ex.getMessage());
            return null;
        }        
    }

    public void closeFolder()
    {
        try
        {
            // The boolean passed to the close() method of folder states
            // whether or not to update the folder by removing deleted messages.
            folder.close(false);
            store.close();
        }
        catch (MessagingException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}