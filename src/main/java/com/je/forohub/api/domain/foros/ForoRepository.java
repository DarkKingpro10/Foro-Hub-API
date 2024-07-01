package com.je.forohub.api.domain.foros;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ForoRepository extends JpaRepository<Foro, Long>{
	Page<Foro> findByEstadoTrue(Pageable paginacion);
}
