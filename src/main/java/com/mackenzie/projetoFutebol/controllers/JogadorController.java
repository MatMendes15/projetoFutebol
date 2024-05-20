package com.mackenzie.projetoFutebol.controllers;

import com.mackenzie.projetoFutebol.modelos.Jogador;
import com.mackenzie.projetoFutebol.modelos.JogadorDto;
import com.mackenzie.projetoFutebol.modelos.Time;
import com.mackenzie.projetoFutebol.servicos.jogadoresRepository;
import com.mackenzie.projetoFutebol.servicos.TimeRepository;
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

        @Autowired
        private TimeRepository timeRepository;

        @GetMapping({"", "/"}) //Trata requisição GET
        public String mostrarListaJogadores(Model model) {
                List<Jogador> jogadores = repo.findAll(Sort.by(Sort.Direction.DESC, "id")); // Busca jogadores e ordena descrecente
                model.addAttribute("jogadores", jogadores);
                return "jogadores/index";
        }

        @GetMapping("create")
        public String mostrarPaginaCriada(Model model) {
                JogadorDto jogadorDto = new JogadorDto();
                model.addAttribute("jogadorDto", jogadorDto);
                model.addAttribute("times", timeRepository.findAll()); // Adiciona a lista de times ao modelo
                return "jogadores/CriarJogador";
        }

        @PostMapping("create")
        public String criarJogador(@Valid @ModelAttribute JogadorDto jogadorDto, BindingResult result, Model model) {
                if (result.hasErrors()) {
                        model.addAttribute("times", timeRepository.findAll()); // Assegura que os times estejam disponíveis após o erro
                        return "jogadores/CriarJogador";
                }

                Jogador jogador = new Jogador();
                jogador.setNome(jogadorDto.getNome());
                jogador.setTime(timeRepository.findById(jogadorDto.getTimeId()).orElse(null)); // Busca e configura o Time pelo ID
                jogador.setPosicao(jogadorDto.getPosicao());
                jogador.setIdade(jogadorDto.getIdade());
                jogador.setDescricao(jogadorDto.getDescricao());
                jogador.setCriadoEm(new Date());

                if (jogadorDto.getFotoJogador() != null && !jogadorDto.getFotoJogador().isEmpty()) {
                        try {
                                String fileName = saveFile(jogadorDto.getFotoJogador());
                                jogador.setFotoJogador(fileName);
                        } catch (IOException ex) {
                                result.addError(new FieldError("jogadorDto", "fotoJogador", "Falha ao salvar a foto do jogador"));
                                model.addAttribute("times", timeRepository.findAll()); // Assegura que os times estejam disponíveis
                                return "jogadores/CriarJogador";
                        }
                }

                repo.save(jogador);
                return "redirect:/jogadores";
        }

        @GetMapping("edit")
        public String mostrarEdicaoPage(Model model, @RequestParam int id) {
                Jogador jogador = repo.findById(id).orElse(null);
                if (jogador == null) {
                        return "redirect:/jogadores";
                }
                model.addAttribute("jogador", jogador);

                JogadorDto jogadorDto = new JogadorDto();
                jogadorDto.setId(jogador.getId());
                jogadorDto.setNome(jogador.getNome());
                jogadorDto.setTimeId(jogador.getTime().getId()); // Garante que usamos timeId
                jogadorDto.setPosicao(jogador.getPosicao());
                jogadorDto.setIdade(jogador.getIdade());
                jogadorDto.setDescricao(jogador.getDescricao());

                model.addAttribute("jogadorDto", jogadorDto);
                model.addAttribute("times", timeRepository.findAll()); // Adiciona a lista de times ao modelo
                return "jogadores/EditJogador";
        }

        @PutMapping("update")
        public String updateJogador(@Valid @ModelAttribute JogadorDto jogadorDto, BindingResult result, Model model) {
                if (result.hasErrors()) {
                        model.addAttribute("times", timeRepository.findAll()); // Adiciona a lista de times ao modelo em caso de erro
                        return "jogadores/EditJogador";
                }

                Jogador jogador = repo.findById(jogadorDto.getId()).orElseThrow(() -> new IllegalArgumentException("Jogador inválido com ID:" + jogadorDto.getId()));
                jogador.setNome(jogadorDto.getNome());
                jogador.setTime(timeRepository.findById(jogadorDto.getTimeId()).orElse(null)); // Busca e configura o Time pelo ID
                jogador.setPosicao(jogadorDto.getPosicao());
                jogador.setIdade(jogadorDto.getIdade());
                jogador.setDescricao(jogadorDto.getDescricao());

                if (jogadorDto.getFotoJogador() != null && !jogadorDto.getFotoJogador().isEmpty()) {
                        try {
                                String fileName = saveFile(jogadorDto.getFotoJogador());
                                jogador.setFotoJogador(fileName);
                        } catch (IOException ex) {
                                result.addError(new FieldError("jogadorDto", "fotoJogador", "Falha ao salvar a foto do jogador"));
                                model.addAttribute("times", timeRepository.findAll()); // Assegura que os times estejam disponíveis
                                return "jogadores/EditJogador";
                        }
                }

                repo.save(jogador);
                return "redirect:/jogadores";
        }

        @GetMapping("delete")
        public String deleteJogador(@RequestParam int id) {
                Jogador jogador = repo.findById(id).orElse(null);
                if (jogador == null) {
                        return "redirect:/jogadores";
                }
                repo.delete(jogador);
                return "redirect:/jogadores";
        }

        private String saveFile(MultipartFile file) throws IOException {
                if (file.isEmpty()) {
                        throw new IOException("O arquivo está vazio e não pode ser salvo.");
                }

                String uploadDir = "public/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                }

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                        return fileName;
                }
        }

        @Bean
        public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
                return new HiddenHttpMethodFilter();
        }
}
