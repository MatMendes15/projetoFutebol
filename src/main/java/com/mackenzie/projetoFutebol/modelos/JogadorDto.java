package com.mackenzie.projetoFutebol.modelos;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class JogadorDto {
    @NotEmpty(message = "O nome é requerido")
    private String nome;

    @NotEmpty(message = "O time é requerido")
    private String time;

    @NotEmpty(message = "A posição do jogador é requerida")
    private String posicao;

    @Min(0)
    private double idade;

    @Size(min = 10, message = "A descrição do jogador precisa de ao menos 10 caracteres")
    @Size(max = 2000, message ="A descrição do jogador não pode exceder 2000 caracteres")
    private String descricao;

    private MultipartFile fotoJogador;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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

    public MultipartFile getFotoJogador() {
        return fotoJogador;
    }

    public void setFotoJogador(MultipartFile fotoJogador) {
        this.fotoJogador = fotoJogador;
    }
}