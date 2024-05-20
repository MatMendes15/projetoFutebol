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

    @ManyToOne
    @JoinColumn(name = "time_id") // Chave estrangeira
    private Time time;

    private String posicao;
    private double idade;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    private Date criadoEm;
    private String fotoJogador;

    // Getters | setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }


    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public double getIdade() {
        return idade;
    }

    public void setIdade(double idade) {
        this.idade = idade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getFotoJogador() {
        return fotoJogador;
    }

    public void setFotoJogador(String fotoJogador) {
        this.fotoJogador = fotoJogador;
    }
}
