package com.je.forohub.api.domain.foros;

import java.time.LocalDateTime;
import java.util.List;

import com.je.forohub.api.domain.respuestas.Respuesta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "foros")
@Entity(name = "Foro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Foro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	
	@Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    private boolean estado;

    private String autor;

    private String curso;
    
    public Foro(DatosCrearForo datos) {
    	this.titulo = datos.titulo();
    	this.fechaCreacion = LocalDateTime.now();
    	this.estado = datos.estado();
    	this.autor = datos.autor();
    	this.curso = datos.curso();
    }
    
    @OneToMany(mappedBy = "foro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas;
    
    public boolean getEstado() {
    	return this.estado;
    }
}
