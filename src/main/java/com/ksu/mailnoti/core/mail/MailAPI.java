/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.core.mail;

/**
 *
 * @author Hamdi
 */
 
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
/**
 * @author Crunchify.com
 * 
 */
 
public class MailAPI { 
    private Properties mailServerProperties;
    private Session getMailSession;
    private MimeMessage generateMailMessage;

    private final String subject = "Mail Notifier System: You received a mail in your department";
    //private final String sender = "ccis.mailinformer@gmail.com";
    //private final String psswd = "ksumailer"; 
    private final String sender = "ksu.mailinformer@gmail.com";
    private final String psswd = "mymail2015"; 


    public MailAPI(){
    }
    
    public boolean sendEmail(String recipientName, String recipientMail, String deparment, String numberOfMails)  {
        String emailBody = message1+ recipientName+message2+numberOfMails+message3+deparment+message4;
        return sendEmail(recipientMail, emailBody);
    }
    
    public boolean sendEmail(String recipientMail, String message)  {
        try {          
        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");
        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);                    

        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientMail));
            //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@crunchify.com"));
            generateMailMessage.setSubject(subject);
            String emailBody = message;
//                        "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
            generateMailMessage.setContent(emailBody, "text/html");
            System.out.println("Mail Session has been created successfully..");
            
            // Step3
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            Transport transport = getMailSession.getTransport("smtp");
            
            // Enter your correct gmail UserID and Password
            // if you have 2FA enabled then provide App Specific Password
            transport.connect("smtp.gmail.com", sender, psswd);
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException ex) {
            Logger.getLogger(MailAPI.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    
    private final String message1 = 
    "    <head><title></title></head>\n" +
    "    <body>\n" +
    "        <div style=\"max-width: 800px; margin: 0; padding: 30px 0;font-family: 'Helvetica Neue', 'Segoe UI', Helvetica, Arial, 'Lucida Grande', sans-serif; font-size: 13px;\">\n" +
    "            <table width=\"80%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
    "                <tr>\n" +
    "                    <td width=\"5%\"></td>\n" +
    "                    <td align=\"left\" width=\"95%\">\n" +
    "                        Dear Dr. ";

    private final String message2 = ",<br /><br />\n" +
    "                        We would like to inform you that you have received : <b><span style=\"color:blue;text-decoration: underline;\">";
    private final String message3 = " mail(s)</b></span> in the <b><span style=\"color:blue;text-decoration: underline;\">";    
    private final String message4 = "</b></span>" +
    "                            \n<br /><br />This e-mail is delivered to you by a new system called \"Mail Informer Device\", it identifies automatically the name from the mail, and informs its owner.\n" +
                                    "<br /><br />The system is still in its beta version. <br />\n" +
    "                        <br />\n" +
    "                        <br />\n" +
    "                            If the automatic system \"ksu.mailinformer\" sent you an erroneous message, and the attached image is not your mail,\n" +
    "                            <b>please just send back this message, and we will manually address the right beneficiary.</b><br />\n" +
    "                        <br /><br />\n" +
    "                        Best Regards,<br />\n" +
    "                        Thanks for your cooperation.\n" +
    "                        <br /><br /><br />\n" +
    "                        <font color=\"#666666\">\n" +
            "Center of Smart Robotics Research<br>"+
    "                        College of Computer and Information Sciences<br>\n" +
    "                        King Saud University <br>\n" +
    "                        Tel 0114696313 <br>\n" +
    "                </font>                \n" +
    "                </td>\n" +
    "                </tr>\n" +
    "            </table>\n" +
    "        </div>\n" +
    "    </body>\n" +
    "</html>\n" ;

    

    public static void main(String args[]) throws AddressException, MessagingException {
        new MailAPI().sendEmail("Hamdi Altahery", "hamdi.altahery@gmail.com", "SOFTWARE ENGINEERING DEPARMAENT", "5");
        System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
    }        
        
}
