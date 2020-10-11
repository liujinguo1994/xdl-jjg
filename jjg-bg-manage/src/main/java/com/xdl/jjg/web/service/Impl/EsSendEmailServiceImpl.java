package com.xdl.jjg.web.service.Impl;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.xdl.jjg.constant.ErrorCode;
import com.xdl.jjg.entity.EsEmail;
import com.xdl.jjg.entity.EsSmtp;
import com.xdl.jjg.mapper.EsEmailMapper;
import com.xdl.jjg.mapper.EsSmtpMapper;
import com.xdl.jjg.model.domain.EsSmtpDO;
import com.xdl.jjg.model.dto.EsSendEmailDTO;
import com.xdl.jjg.response.exception.ArgumentException;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.web.service.IEsSendEmailService;
import com.xdl.jjg.web.service.IEsSmtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;

/**
 * <p>
 *  服务实现类-发送邮件impl
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Service
public class EsSendEmailServiceImpl implements IEsSendEmailService {

    private static Logger logger = LoggerFactory.getLogger(EsSendEmailServiceImpl.class);

    @Autowired
    private IEsSmtpService smtpService;

    @Autowired
    private EsEmailMapper esEmailMapper;

    @Autowired
    private EsSmtpMapper esSmtpMapper;

    @Override
    public DubboResult sendEmail(EsSendEmailDTO sendEmailDTO) {
        DubboResult<EsSmtpDO> result = smtpService.getCurrentSmtp();
        if (!result.isSuccess()){
            throw new ArgumentException(ErrorCode.GET_SMTP_ERROR.getErrorCode(), "获取smtp方案异常");
        }
        EsSmtpDO esSmtpDO = result.getData();
        //根据对ssl的支持 分别走不同的发送方法
        if (esSmtpDO.getOpenSsl() == 1 || "smtp.qq.com".equals(esSmtpDO.getHost())) {
            //使用JavaMail发送邮件 支持ssl
            this.sendMailByJavaMail(esSmtpDO, sendEmailDTO);
        } else {
            //使用Spring提供的JavaMailSender接口实现邮件发送 暂不支持ssl
            this.sendMailByMailSender(esSmtpDO, sendEmailDTO);
        }

        return DubboResult.success();
    }

    //使用JavaMail发送邮件 支持ssl
    public void sendMailByJavaMail(EsSmtpDO smtp, EsSendEmailDTO sendEmailDTO) {

        Properties props = new Properties();
        props.put("mail.smtp.host", smtp.getHost());
        props.put("mail.smtp.port", String.valueOf(smtp.getPort()));
        props.put("mail.smtp.auth", "true");// 指定验证为true
        props.put("mail.transport.protocol", "smtp");

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", String.valueOf(smtp.getPort()));
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtps.ssl.protocols", "TSLv1 TSLv1.1 TLSv1.2");
        props.put("mail.smtp.ssl.ciphersuites", "SSL_RSA_WITH_RC4_128_SHA SSL_RSA_WITH_RC4_128_MD5 TLS_RSA_WITH_AES_128_CBC_SHA TLS_DHE_RSA_WITH_AES_128_CBC_SHA TLS_DHE_DSS_WITH_AES_128_CBC_SHA SSL_RSA_WITH_3DES_EDE_CBC_SHA SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA SSL_RSA_WITH_DES_CBC_SHA SSL_DHE_RSA_WITH_DES_CBC_SHA SSL_DHE_DSS_WITH_DES_CBC_SHA SSL_RSA_EXPORT_WITH_RC4_40_MD5 SSL_RSA_EXPORT_WITH_DES40_CBC_SHA SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA TLS_EMPTY_RENEGOTIATION_INFO_SCSV");

        //创建一个程序与邮件服务器会话对象 Session
        Session sendMailSession = Session.getInstance(props,
                new Authenticator() {// 验证账号及密码，密码需要是第三方授权码
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtp.getUsername(),
                                smtp.getPassword());
                    }
                });
        try {
            //创建一个Message，它相当于是邮件内容
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(smtp.getUsername(), smtp.getMailFrom());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(sendEmailDTO.getEmail());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(sendEmailDTO.getTitle());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            mailMessage.setContent(sendEmailDTO.getContent(), "text/html;charset=utf-8");
            //发送邮件
            Transport.send(mailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            sendEmailDTO.setSuccess(0);
            throw new ArgumentException(ErrorCode.SEND_EMAIL_ERROR.getErrorCode(), "邮件发送失败！");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sendEmailDTO.setSuccess(0);
            throw new ArgumentException(ErrorCode.SEND_EMAIL_ERROR.getErrorCode(), "邮件发送失败！");
        }finally {
            //添加发邮件记录
            add(sendEmailDTO);
            //更新邮件方案的最后发信时间及发件数量
            update(smtp);
        }
    }

    /**
     * 使用Spring提供的JavaMailSender接口实现邮件发送 暂不支持ssl
     */
    public void sendMailByMailSender(EsSmtpDO smtp, EsSendEmailDTO sendEmailDTO) {
        JavaMailSender javaMailSender = new JavaMailSenderImpl();

        ((JavaMailSenderImpl) javaMailSender).setHost(smtp.getHost());
        ((JavaMailSenderImpl) javaMailSender).setUsername(smtp.getUsername());
        ((JavaMailSenderImpl) javaMailSender).setPassword(smtp.getPassword());
        //设置发送者
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            // 设置utf-8或GBK编码，否则邮件会有乱码
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            //设置邮件标题
            helper.setSubject(sendEmailDTO.getTitle());
            //发件人
            helper.setFrom(smtp.getMailFrom());
            //收件人
            helper.setTo(sendEmailDTO.getEmail());
            //发送内容
            helper.setText(sendEmailDTO.getContent());
            //发送邮件
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            sendEmailDTO.setSuccess(0);
            throw new ArgumentException(ErrorCode.SEND_EMAIL_ERROR.getErrorCode(), "邮件发送失败！");
        }finally {
            //添加发邮件记录
            add(sendEmailDTO);
            //更新邮件方案的最后发信时间及发件数量
            update(smtp);
        }
    }




    /**
     * 添加发邮件记录
     */
    public void add(EsSendEmailDTO email) {
        EsEmail esEmail = new EsEmail();
        esEmail.setEmail(email.getEmail());
        esEmail.setTitle(email.getTitle());
        esEmail.setContent(email.getContent());
        esEmail.setType(email.getTitle());
        esEmail.setIsSuccess(email.getSuccess());
        esEmail.setUpdateTime(System.currentTimeMillis());
        esEmailMapper.insert(esEmail);
    }

    /**
     * 更新邮件方案的最后发信时间及发件数量
     */
    public void update(EsSmtpDO smtp){
        EsSmtp esSmtp = new EsSmtp();
        esSmtp.setId(smtp.getId());
        esSmtp.setLastSendTime(System.currentTimeMillis());
        esSmtp.setSendCount(smtp.getSendCount() + 1);
        esSmtpMapper.updateById(esSmtp);
    }
}
