package br.com.sicredi.simulacao.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "MensagemNotFound v1")
public class MessageNotFoundDTO {

    @ApiModelProperty(required = true, example = "CPF 999999999 não encontrado")
    private String mensagem;

    public MessageNotFoundDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public MessageNotFoundDTO(){}

    public String getMensagem() {
        return mensagem;
    }
}
