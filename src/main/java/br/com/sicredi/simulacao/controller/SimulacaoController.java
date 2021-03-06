package br.com.sicredi.simulacao.controller;

import br.com.sicredi.simulacao.dto.v1.MessageDTO;
import br.com.sicredi.simulacao.dto.v1.MessageNotFoundDTO;
import br.com.sicredi.simulacao.dto.v1.SimulacaoDTO;
import br.com.sicredi.simulacao.dto.v1.ValidacaoDTO;
import br.com.sicredi.simulacao.entity.Simulacao;
import br.com.sicredi.simulacao.entity.Simulacao_;
import br.com.sicredi.simulacao.exception.v1.SimulacaoException;
import br.com.sicredi.simulacao.repository.SimulacaoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RestController
@Api(value = "/simulacoes", tags = "Simulações")
public class SimulacaoController {

    private final SimulacaoRepository repository;

    public SimulacaoController(SimulacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/simulacoes")
    @ApiOperation(value = "Retorna todas as simulações existentes")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Simulações encontradas", response = SimulacaoDTO.class, responseContainer = "List"),
            @ApiResponse(code = 204, message = "Nome não encontrado")
    })
    List<Simulacao> getSimulacao(@RequestParam(name = "nome", required = false) String nome) {
        Example<Simulacao> example =
                Example.of( Simulacao.builder().nome(nome).build(),
                        ExampleMatcher.matchingAny().
                                withMatcher(Simulacao_.NOME, ExampleMatcher.GenericPropertyMatchers.contains()));

        return repository.findAll(example);
    }

    @GetMapping("/api/v1/simulacoes/{cpf}")
    @ApiOperation(value = "Retorna uma simulação através do CPF")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Simulação encontrada", response = SimulacaoDTO.class),
            @ApiResponse(code = 404, message = "Simulação não encontrada", response = MessageDTO.class)
    })
    ResponseEntity<Simulacao> one(@PathVariable String cpf) {
        return repository.findByCpf(cpf).
                map(simulacao -> ResponseEntity.ok().body(simulacao)).
                orElseThrow(() -> new SimulacaoException(MessageFormat.format("CPF {0} não encontrado", cpf)));
    }

    @PostMapping("/api/v1/simulacoes")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Insere uma nova simulação", code = 201)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Simulação criada com sucesso", response = SimulacaoDTO.class),
            @ApiResponse(code = 400, message = "Falta de informações", response = ValidacaoDTO.class),
            @ApiResponse(code = 409, message = "CPF já existente")
    })
    Simulacao novaSimulacao(@Valid @RequestBody SimulacaoDTO simulacao) {
        return repository.save(new ModelMapper().map(simulacao, Simulacao.class));
    }

    @PutMapping("/api/v1/simulacoes/{cpf}")
    @ApiOperation(value = "Atualiza uma simulação existente através do CPF")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Simulação alterada com sucesso", response = SimulacaoDTO.class),
            @ApiResponse(code = 404, message = "Simulação não encontrada", response = MessageDTO.class),
            @ApiResponse(code = 409, message = "CPF já existente")
    })
    Simulacao atualizaSimulacao(@RequestBody SimulacaoDTO simulacao, @PathVariable String cpf) {

        return update(new ModelMapper().
                map(simulacao, Simulacao.class), cpf).
                orElseThrow(() -> new SimulacaoException(MessageFormat.format("CPF {0} não encontrado", cpf)));
    }

    @DeleteMapping("/api/v1/simulacoes/{cpf}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Remove uma simulação existente através do CPF", code = 204)
    @ApiResponses({
            @ApiResponse(code = 204, message = "Simulação removida com sucesso"),
            @ApiResponse(code = 404, message = "Simulação não encontrada", response = MessageNotFoundDTO.class)
    })
    void delete(@PathVariable String cpf) {
        if (repository.findByCpf(cpf).isPresent()) repository.deleteByCpf(cpf);
        else {
            throw new SimulacaoException(MessageFormat.format("CPF {0} não encontrado", cpf));
        }

    }

    private Optional<Simulacao> update(Simulacao novaSimulacao, String cpf) {
        return repository.findByCpf(cpf).map(simulacao -> {
            setIfNotNull(simulacao::setId, novaSimulacao.getId());
            setIfNotNull(simulacao::setNome, novaSimulacao.getNome());
            setIfNotNull(simulacao::setCpf, novaSimulacao.getCpf());
            setIfNotNull(simulacao::setEmail, novaSimulacao.getEmail());
            setIfNotNull(simulacao::setParcelas, novaSimulacao.getParcelas());
            setIfNotNull(simulacao::setValor, novaSimulacao.getValor());

            return repository.save(simulacao);
        });
    }

    private <T> void setIfNotNull(final Consumer<T> setter, final T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
