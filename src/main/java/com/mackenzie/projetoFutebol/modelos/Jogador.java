package com.mackenzie.projetoFutebol.modelos;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "jogadores")
public class Jogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String time;
    private String posicao;
    private double idade;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    private Date criadoEm;
    private String fotoJogador;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() { // Corrigido de getnome para getNome
        return nome;
    }

    public void setNome(String nome) { // Corrigido de setnome para setNome
        this.nome = nome;
    }

    public String getTime() { // Corrigido de gettime para getTime
        return time;
    }

    public void setTime(String time) { // Corrigido de settime para setTime
        this.time = time;
    }

    public String getPosicao() { // Corrigido de getposicao para getPosicao
        return posicao;
    }

    public void setPosicao(String posicao) { // Corrigido de setposicao para setPosicao
        this.posicao = posicao;
    }

    public double getIdade() { // Corrigido de getidade para getIdade
        return idade;
    }

    public void setIdade(double idade) { // Corrigido de setidade para setIdade
        this.idade = idade;
    }

    public String getDescricao() { // Corrigido de getdescricao para getDescricao
        return descricao;
    }

    public void setDescricao(String descricao) { // Corrigido de setdescricao para setDescricao
        this.descricao = descricao;
    }

    public Date getCriadoEm() { // Corrigido de getcriadoEm para getCriadoEm
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) { // Corrigido de setcriadoEm para setCriadoEm
        this.criadoEm = criadoEm;
    }

    public String getFotoJogador() { // Corrigido de getFotoJogador para getFotoJogador
        return fotoJogador;
    }

    public void setFotoJogador(String fotoJogador) { // Corrigido de setFotoJogador para setFotoJogador
        this.fotoJogador = fotoJogador;
    }
}
