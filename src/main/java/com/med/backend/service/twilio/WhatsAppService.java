package com.med.backend.service.twilio;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.whatsapp_from}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendAppointmentConfirmation(String toPhone, String doctorName, String date, String time, String reason) {
        String toFormatted = "whatsapp:+591" + toPhone;
        String messageBody = String.format("""
            *✅ Confirmación de Cita Médica*
    
            Su cita ha sido agendada con éxito. Aquí están los detalles:
    
            📅 *Fecha:* %s
            🕒 *Hora:* %s
            👨‍⚕️ *Doctor:* %s
            📝 *Motivo:* %s
    
            Por favor, preséntese con al menos *10 minutos de anticipación*.
    
            _Gracias por confiar en DocHealth._
            """, date, time, doctorName, reason);

            Message.creator(
                    new com.twilio.type.PhoneNumber(toFormatted),
                    new com.twilio.type.PhoneNumber(fromNumber),
                    messageBody
            ).create();
    }
}
