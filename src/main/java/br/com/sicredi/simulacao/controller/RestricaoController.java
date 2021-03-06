package br.com.sicredi.simulacao.controller;

import br.com.sicredi.simulacao.dto.v1.MessageDTO;
import br.com.sicredi.simulacao.entity.Restricao;
import br.com.sicredi.simulacao.exception.v1.RestricaoException;
import br.com.sicredi.simulacao.service.RestricaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Optional;


@RestController
@Api(value = "Restrição", tags = "Restrições")
public class RestricaoController {

    private final RestricaoService restricaoService;

    public RestricaoController(RestricaoService restricaoService) {
        this.restricaoService = restricaoService;
    }

    @ApiOperation(value = "Consulta se um CPF possui ou não restrição")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Não possui restrição", response = Void.class),
        @ApiResponse(code = 200, message = "Pessoa com restrição", response = MessageDTO.class)
    })
    @GetMapping("/api/v1/restricoes/{cpf}")
    ResponseEntity<Void> one(@PathVariable String cpf) {

        Optional<Restricao> restricaoOptional = restricaoService.findByCpf(cpf);

        if (restricaoOptional.isPresent()) {
            throw new RestricaoException(MessageFormat.format("O CPF {0} possui restrição", cpf));
        }

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Consulta se um CPF possui ou não restrição")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Não possui restrição"),
            @ApiResponse(code = 200, message = "Pessoa com restrição", response = br.com.sicredi.simulacao.dto.v2.MessageDTO.class)
    })
    @GetMapping("/api/v2/restricoes/{cpf}")
    ResponseEntity<Void> oneV2(@PathVariable String cpf) {

        Optional<Restricao> restricaoOptional = restricaoService.findByCpf(cpf);

        if (restricaoOptional.isPresent()) {
            throw new br.com.sicredi.simulacao.exception.v2.RestricaoException(
                    MessageFormat.format("O CPF {0} possui restrição", cpf),
                    restricaoOptional.get().getTipoRestricao());
        }

        return ResponseEntity.noContent().build();
    }
}
