package com.mackenzie.projetoFutebol.servicos;

import com.mackenzie.projetoFutebol.modelos.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Integer> {
}
