package com.mackenzie.projetoFutebol.controllers;

import com.mackenzie.projetoFutebol.modelos.Jogador;
import com.mackenzie.projetoFutebol.modelos.JogadorDto;
import com.mackenzie.projetoFutebol.servicos.jogadoresRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/jogadores")
public class JogadorController {

        @Autowired
        private jogadoresRepository repo;

        @GetMapping({"", "/"})
        public String mostrarListaJogadores(Model model) {
                List<Jogador> jogadores = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
                model.addAttribute("jogadores", jogadores);
                System.out.println("Acessando a lista de jogadores");
                return "jogadores/index";
        }

        @GetMapping("create")
        public String mostrarPaginaCriada(Model model) {
                JogadorDto jogadorDto = new JogadorDto();
                model.addAttribute("jogadorDto", jogadorDto);
                System.out.println("Acessando a página de criação de jogador");
                return "jogadores/CriarJogador";
        }

        @PostMapping("create")
        public String criarJogador(@Valid @ModelAttribute JogadorDto jogadorDto, BindingResult result) {
                if (result.hasErrors()) {
                        System.out.println("Erros de validação encontrados.");
                        return "jogadores/CriarJogador";
                }

                System.out.println("Nenhum erro de validação.");

                Jogador jogador = new Jogador();
                if (jogadorDto.getFotoJogador() != null && !jogadorDto.getFotoJogador().isEmpty()) {
                        try {
                                String fileName = saveFile(jogadorDto.getFotoJogador());
                                jogador.setFotoJogador(fileName);
                                System.out.println("Arquivo salvo: " + fileName);
                        } catch (IOException ex) {
                                System.out.println("Falha ao salvar arquivo: " + ex.getMessage());
                                result.addError(new FieldError("jogadorDto", "fotoJogador", "Falha ao salvar a foto do jogador"));
                                return "jogadores/CriarJogador";
                        }
                } else {
                        System.out.println("Nenhum arquivo para salvar.");
                }

                // Configuração dos dados do jogador
                jogador.setNome(jogadorDto.getNome());
                jogador.setTime(jogadorDto.getTime());
                jogador.setPosicao(jogadorDto.getPosicao());
                jogador.setIdade(jogadorDto.getIdade());
                jogador.setDescricao(jogadorDto.getDescricao());
                jogador.setCriadoEm(new Date());

                // Salvando o jogador no banco de dados
                repo.save(jogador);
                System.out.println("Salvando jogador no banco de dados.");

                return "redirect:/jogadores";
        }

        private String saveFile(MultipartFile file) throws IOException {
                if (file.isEmpty()) {
                        throw new IOException("Cannot save empty file.");
                }

                String uploadDir = "public/images/";
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        return fileName;
                }
        }
}
