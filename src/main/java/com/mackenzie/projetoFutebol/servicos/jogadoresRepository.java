package com.mackenzie.projetoFutebol.servicos;

import com.mackenzie.projetoFutebol.modelos.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface jogadoresRepository extends JpaRepository<Jogador, Integer> {
}
