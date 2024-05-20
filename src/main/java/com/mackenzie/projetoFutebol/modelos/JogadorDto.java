package com.mackenzie.projetoFutebol.modelos;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

public class JogadorDto {
    private int id;
    @NotEmpty(message = "O nome é requerido")
    private String nome;
    @NotNull(message = "O time é requerido")
    private Integer timeId;  // Alterado para armazenar o ID do time
    @NotEmpty(message = "A posição do jogador é requerida")
    private String posicao;
    @Min(0)
    private double idade;
    @Size(min = 10, max = 2000, message = "A descrição do jogador precisa de ao menos 10 caracteres e não pode exceder 2000 caracteres")
    private String descricao;
    private MultipartFile fotoJogador;


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

    public Integer getTimeId() {
        return timeId;
    }

    public void setTimeId(Integer timeId) {
        this.timeId = timeId;
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

    public MultipartFile getFotoJogador() {
        return fotoJogador;
    }

    public void setFotoJogador(MultipartFile fotoJogador) {
        this.fotoJogador = fotoJogador;
    }
}
