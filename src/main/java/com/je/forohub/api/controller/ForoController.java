package com.je.forohub.api.controller;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.je.forohub.api.domain.foros.DatosCrearForo;
import com.je.forohub.api.domain.foros.DatosListarForo;
import com.je.forohub.api.domain.foros.DatosObtenerForo;
import com.je.forohub.api.domain.foros.Foro;
import com.je.forohub.api.domain.foros.ForoRepository;
import com.je.forohub.api.domain.respuestas.DatosRespuesta;
import com.je.forohub.api.domain.respuestas.Respuesta;
import com.je.forohub.api.domain.respuestas.RespuestaRepository;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping("/foros")
public class ForoController {
	private final ForoRepository foroRepository;
	private final RespuestaRepository respuestaRepository;
	public ForoController(ForoRepository foroRepository, RespuestaRepository respuestaRepository) {
        this.foroRepository = foroRepository;
        this.respuestaRepository = respuestaRepository;
    }
	
	@GetMapping("/{id}")
	@Operation(summary = "Obtiene el foro y los mensajes")
    public ResponseEntity<DatosObtenerForo> obtenerForo(@PathVariable Long id, @PageableDefault(size = 2) Pageable paginacion) {
        if(id == null || !foroRepository.existsById(id)) {
        	 throw new ValidationException("No existe for con ese id");
        }
        
        var foro = foroRepository.getReferenceById(id);
        var respuestas = respuestaRepository.findAllByForo(foro,paginacion).map(DatosRespuesta::new);
        
        return  ResponseEntity.ok(new DatosObtenerForo(new DatosListarForo(foro), respuestas));
    }
	
	@GetMapping
	@Operation(summary = "Obtiene el listado de los foros")
    public ResponseEntity<Page<DatosListarForo>> listarForos(@PageableDefault(size = 2) Pageable paginacion) {
        return ResponseEntity.ok(foroRepository.findByEstadoTrue(paginacion).map(DatosListarForo::new));
    }
	
	@PostMapping
    @Transactional
	@Operation(summary = "Crea un foro")
	public ResponseEntity<DatosListarForo> crearForo(@RequestBody @Valid DatosCrearForo datos, UriComponentsBuilder uriComponentsBuilder){
		Foro foroCreado = foroRepository.save(new Foro(datos)); 
		DatosListarForo res = new DatosListarForo(foroCreado);
		
		URI url = uriComponentsBuilder.path("/foros/{id}").buildAndExpand(foroCreado.getId()).toUri();
		return ResponseEntity.created(url).body(res);
	}
	
	@PutMapping
    @Transactional
    @Operation(summary = "Actualiza los datos de un foro existente")
    public ResponseEntity actualizarForo(@RequestBody @Valid DatosListarForo datos) {
		System.out.print("ID"+datos.id());
		System.out.println("El foto existe?"+foroRepository.existsById(datos.id()));
		if(datos.id() == null || !foroRepository.existsById(datos.id())) {
        	throw new ValidationException("No existe for con ese id");
        }
        
        var foro = foroRepository.getReferenceById(datos.id());
        
        foro.setAutor(datos.autor());
        foro.setCurso(datos.curso());
        foro.setEstado(datos.estado());
        foro.setFechaCreacion(LocalDateTime.now());
        foro.setTitulo(datos.titulo());
        
        return ResponseEntity.ok(new DatosListarForo(foro)); 
    }
	
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un foro registrado - inactivo")
    public ResponseEntity eliminarForo(@PathVariable Long id) {
    	if(!foroRepository.existsById(id)) {
        	throw new ValidationException("No existe for con ese id");
        }
        Foro foro = foroRepository.getReferenceById(id);
        foro.setEstado(false);
        return ResponseEntity.noContent().build();
    }
}
