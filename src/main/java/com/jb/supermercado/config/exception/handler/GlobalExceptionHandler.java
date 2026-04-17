package com.jb.supermercado.config.exception.handler;

import com.jb.supermercado.config.exception.BusinessException;
import com.jb.supermercado.config.exception.RecursoNaoEncontradoException;
import com.jb.supermercado.config.exception.response.ErroPadraoResponse;
import com.jb.supermercado.config.exception.response.ErroValidacaoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ErroValidacaoResponse response = new ErroValidacaoResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos",
                erros
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroPadraoResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {

        ErroPadraoResponse response = new ErroPadraoResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroPadraoResponse> handleBusinessException(BusinessException ex) {
        ErroPadraoResponse response = new ErroPadraoResponse(
                HttpStatus.CONFLICT.value(),
                "Regra de negócio violada",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    // 1. Trata erro genérico 500 (Qualquer erro inesperado)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadraoResponse> handleGenericException(Exception ex) {
        ErroPadraoResponse response = new ErroPadraoResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // 2. Trata erros de conversão de tipos na URL (ex: letras no ID)
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroPadraoResponse> handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        ErroPadraoResponse response = new ErroPadraoResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Tipo de parâmetro inválido",
                String.format("O parâmetro '%s' esperava o tipo '%s'", ex.getName(), ex.getRequiredType().getSimpleName())
        );
        return ResponseEntity.badRequest().body(response);
    }

    // 3. Trata JSON malformado na requisição
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadraoResponse> handleHttpMessageNotReadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ErroPadraoResponse response = new ErroPadraoResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Corpo da requisição inválido",
                "Verifique a sintaxe do seu JSON ou os tipos dos campos."
        );
        return ResponseEntity.badRequest().body(response);
    }
}