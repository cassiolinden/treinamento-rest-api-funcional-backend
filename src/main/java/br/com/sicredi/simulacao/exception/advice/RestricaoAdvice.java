package br.com.sicredi.simulacao.exception.advice;

import br.com.sicredi.simulacao.dto.v1.MessageDTO;
import br.com.sicredi.simulacao.exception.v1.RestricaoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestricaoAdvice {

    @ResponseBody
    @ExceptionHandler(RestricaoException.class)
    @ResponseStatus(HttpStatus.OK)
    MessageDTO restricaoHandlerV1(RestricaoException e) {
        return new MessageDTO(e.getMensagem());
    }

    @ResponseBody
    @ExceptionHandler(br.com.sicredi.simulacao.exception.v2.RestricaoException.class)
    @ResponseStatus(HttpStatus.OK)
    br.com.sicredi.simulacao.dto.v2.MessageDTO restricaoHandlerV1(br.com.sicredi.simulacao.exception.v2.RestricaoException e) {
        return new br.com.sicredi.simulacao.dto.v2.MessageDTO(e.getMensagem(), e.getDetalhe());
    }
}
