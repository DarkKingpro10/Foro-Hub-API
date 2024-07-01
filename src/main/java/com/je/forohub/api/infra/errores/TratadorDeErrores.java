package com.je.forohub.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(403).body("Acceso denegado: " + ex.getMessage());
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<DatosErrorValidacion> handleValidationException(ValidationException ex) {
    	
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DatosErrorValidacion(ex));
    }

    private record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }

		public DatosErrorValidacion(ValidationException ex) {
			this("Validación de error:", ex.getMessage());
		}
    }

}