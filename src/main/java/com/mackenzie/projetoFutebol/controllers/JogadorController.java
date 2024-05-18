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
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

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

        @GetMapping("edit")
        public String mostrarEdicaoPage(Model model, @RequestParam int id) {
                try {
                        Jogador jogador = repo.findById(id).get();
                        model.addAttribute("jogador", jogador);

                        JogadorDto jogadorDto = new JogadorDto();
                        jogadorDto.setId(jogador.getId()); // Assegure-se que o ID está sendo setado aqui
                        jogadorDto.setNome(jogador.getNome());
                        jogadorDto.setTime(jogador.getTime());
                        jogadorDto.setPosicao(jogador.getPosicao());
                        jogadorDto.setIdade(jogador.getIdade());
                        jogadorDto.setDescricao(jogador.getDescricao());

                        model.addAttribute("jogadorDto", jogadorDto);
                } catch (Exception ex) {
                        System.out.println("Exception: " + ex.getMessage());
                        return "redirect:/jogadores";
                }
                return "jogadores/EditJogador";
        }



        @PutMapping("update")
        public String updateJogador(@Valid @ModelAttribute JogadorDto jogadorDto, BindingResult result, Model model) {
                System.out.println("ID Recebido: " + jogadorDto.getId()); // Debug para verificar o ID recebido
                if (result.hasErrors()) {
                        model.addAttribute("jogadorDto", jogadorDto);
                        return "jogadores/EditJogador";
                }

                if (jogadorDto.getId() == 0) {
                        System.out.println("ID do jogador não recebido corretamente.");
                        return "redirect:/error";  // Redireciona para uma página de erro ou a página de lista com uma mensagem de erro.
                }


                Jogador jogador = repo.findById(jogadorDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Jogador inválido com ID:" + jogadorDto.getId()));

                try {
                        if (jogadorDto.getFotoJogador() != null && !jogadorDto.getFotoJogador().isEmpty()) {
                                String fileName = saveFile(jogadorDto.getFotoJogador());
                                jogador.setFotoJogador(fileName);
                        }
                } catch (IOException ex) {
                        System.out.println("Falha ao salvar arquivo: " + ex.getMessage());
                        result.addError(new FieldError("jogadorDto", "fotoJogador", "Falha ao salvar a foto do jogador"));
                        return "jogadores/EditJogador";
                }

                jogador.setNome(jogadorDto.getNome());
                jogador.setTime(jogadorDto.getTime());
                jogador.setPosicao(jogadorDto.getPosicao());
                jogador.setIdade(jogadorDto.getIdade());
                jogador.setDescricao(jogadorDto.getDescricao());

                repo.save(jogador);
                return "redirect:/jogadores";
        }

        @GetMapping("delete")
        public String deleteJogador (@RequestParam int id) {
                try {
                        Jogador jogador = repo.findById(id).get();

                        Path imagePath = Paths.get("public/images" + jogador.getFotoJogador());
                        try {
                                Files.delete(imagePath);
                        }
                        catch (Exception ex) {
                                System.out.println("Exception: " + ex.getMessage());
                        }

                        //deletar o jogador
                        repo.delete(jogador);
                }
                catch (Exception ex) {
                        System.out.println("Exception:" + ex.getMessage());
                }

                return "redirect:/jogadores";
        }






        private String saveFile(MultipartFile file) throws IOException {
                if (file.isEmpty()) {
                        throw new IOException("O arquivo está vazio e não pode ser salvo.");
                }

                // Assegure-se que o caminho do diretório de upload está correto e acessível
                String uploadDir = "public/images/"; // Ajuste conforme a estrutura do seu projeto
                Path uploadPath = Paths.get(uploadDir);

                // Verifique e crie o diretório se não existir
                if (!Files.exists(uploadPath)) {
                        try {
                                Files.createDirectories(uploadPath);
                        } catch (IOException e) {
                                throw new IOException("Não foi possível criar o diretório de upload: " + uploadPath);
                        }
                }

                // Tente salvar o arquivo
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        return fileName;
                } catch (IOException e) {
                        throw new IOException("Não foi possível salvar o arquivo: " + fileName, e);
                }
        }

        @Bean
        public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
                HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
                return filter;
        }

}
