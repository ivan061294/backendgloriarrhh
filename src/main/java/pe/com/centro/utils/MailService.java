package pe.com.centro.utils;

import lombok.extern.slf4j.Slf4j;
import pe.com.centro.domain.MailRequest;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import static java.lang.System.getenv;

/**
 * Utils for email messages
 */
@Slf4j
public final class MailService {
    private static final String FROM_EMAIL = getenv("AWS_SES_FROM_EMAIL");

    private static final String FROM_NAME = getenv("AWS_SES_FROM_NAME");

    private static final String SMTP_HOST = getenv("AWS_SES_SMTP_HOST");

    private static final String SMTP_USERNAME = getenv("AWS_SES_SMTP_USERNAME");

    private static final String SMTP_PASSWORD = getenv("AWS_SES_SMTP_PASSWORD");

    private static void sendMail(MailRequest mailRequest, String templateKey, String subject) {

        Properties properties = System.getProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.mime.charset", "UTF-8");
        properties.put("file.encoding", "UTF-8");

        Session session = Session.getDefaultInstance(properties);

        MimeMessage message = new MimeMessage(session);

        try {
            String contentMail = Constants.HTML_MESSAGES.get(templateKey).replace("@@puesto",mailRequest.getRequiredPosition())
                                                                         .replace("@@url",mailRequest.getUrl());

            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailRequest.getTo()));
            message.setSubject(subject);
            message.setContent(contentMail, "text/html; charset=UTF-8");
            message.setSentDate(new Date());
            log.info("contentMail: {}", contentMail);
            log.info("FROM_EMAIL: {}", FROM_EMAIL);
            log.info("FROM_NAME: {}", FROM_NAME);
            log.info("to: {}", mailRequest.getTo());
            log.info("Subject: {}", mailRequest.getMailTamplete().getSubject());
            log.info("message: {}", message.toString());
            log.info("SMTP_HOST: {}, SMTP_USERNAME: {}, SMTP_PASSWORD: {}", SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD);

            Transport transport = session.getTransport();
            transport.connect(SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            log.info("mensaje enviado");

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email", e);
        }
    }
    public static void sendMail(MailRequest mailRequest) {

        Properties properties = System.getProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.mime.charset", "UTF-8");
        properties.put("file.encoding", "UTF-8");

        Session session = Session.getDefaultInstance(properties);

        MimeMessage message = new MimeMessage(session);

        log.info("mailRequest.getMailTamplete().getContentTemplate():{}",mailRequest.getMailTamplete().getContentTemplate());

        log.info("mailRequest.getRequiredPosition():{},mailRequest.getUrl():{},mailRequest.getEmployeeToReplace():{}",mailRequest.getRequiredPosition(),mailRequest.getUrl(),mailRequest.getEmployeeToReplace());

        try {
            String contentMail = mailRequest.getMailTamplete().getContentTemplate().replace("@@puesto",mailRequest.getRequiredPosition())
                                                                         .replace("@@url",mailRequest.getUrl())
                                                                         .replace("@@substitution",mailRequest.getEmployeeToReplace())
                                                                         .replace("@@numberOfRequirements",mailRequest.getNumberofRequirements())
                                                                         .replace("@@destinationUserName",mailRequest.getDestinationUserName());

            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailRequest.getTo()));
            message.setSubject(mailRequest.getMailTamplete().getSubject());
            message.setContent(contentMail, "text/html; charset=UTF-8");
            message.setSentDate(new Date());
            log.info("contentMail: {}", contentMail);
            log.info("FROM_EMAIL: {}", FROM_EMAIL);
            log.info("FROM_NAME: {}", FROM_NAME);
            log.info("to: {}", mailRequest.getTo());
            log.info("Subject: {}", mailRequest.getMailTamplete().getSubject());
            log.info("SMTP_HOST: {}, SMTP_USERNAME: {}, SMTP_PASSWORD: {}", SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD);

            Transport transport = session.getTransport();
            transport.connect(SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            log.info("mensaje enviado!");

        } catch (Exception e) {
            log.error("Error sending email", e);
        }
    }
    //Plan and No Plan Req
    //001
    // public static void sendMailForNewRequirementPlanAndNoPlan(MailDetailFlow mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Nuevo Requerimiento solicitado", mailVariable.getTo());
    //     sendMail(mailVariable, "New-Requirement-Plan-NoPlan", "Nuevo Requerimiento solicitado");
    // }

    // //002
    // public static void sendMailForPendingApprovedRequirementPlanAndNoPlan(MailDetailFlow mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento pendiente de aprobación", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Pending-Approved-Plan-NoPlan", "Solicitud de Requerimiento pendiente de aprobación");
    // }

    // //003
    // public static void sendMailForRejectRequirementPlanAndNoPlan(MailDetailFlow mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento rechazado", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Reject-Plan-NoPlan", "Solicitud de Requerimiento rechazado");
    // }
    // //004
    // //TODO: Se debe enviar el correo en bloque
    // public static void sendMailForPendingApprovedRequirementListPlanAndNoPlan(MailDetailFlow mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento pendiente de aprobación", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Pending-Approved-Plan-NoPlan", "Solicitud de Requerimiento pendiente de aprobación");
    // }

    // //005 
    // //TODO: Se debe enviar el correo en bloque
    // public static void sendMailForApprovedRequirementListPlanAndNoPlan(MailDetailFlow mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento aprobación", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Approved-Plan-NoPlan", "Solicitud de Requerimiento aprobación");
    // }
    // //006
    // //TODO:Se debe enviar el correo en bloque
    // public static void sendMailForRejectRequirementListPlanAndNoPlan(MailDetailFlow mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento rechazado", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Reject-Plan-NoPlan", "Solicitud de Requerimiento rechazado");
    // }


    // public static void sendMailForObservedRequirementPlanAndNoPlan(MailVariable mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento observado", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Observed", "Solicitud de Requerimiento observado");
    // }

    // public static void sendMailForReleasedRequirementPlanAndNoPlan(MailVariable mailVariable) {
    //     log.debug("Sending email with subject='{}', to='{}'", "Solicitud de Requerimiento liberado", mailVariable.getTo());
    //     sendMail(mailVariable, "Requirement-Released", "Solicitud de Requerimiento liberado");
    // }

    //Substitution

    //



}
