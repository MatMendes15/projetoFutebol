package com.mackenzie.projetoFutebol.controllers;

import com.mackenzie.projetoFutebol.modelos.Time;
import com.mackenzie.projetoFutebol.servicos.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/times")
public class TimeController {

    @Autowired
    private TimeRepository timeRepository;

    @GetMapping
    public String listarTimes(Model model) {
        model.addAttribute("times", timeRepository.findAll());
        return "times/lista";
    }

    @GetMapping("/create")
    public String mostrarFormularioDeCriacao(Model model) {
        model.addAttribute("time", new Time());
        return "times/criar";
    }

    @PostMapping("/create")
    public String criarTime(@Valid @ModelAttribute Time time, BindingResult result) {
        if (result.hasErrors()) {
            return "times/criar";
        }
        timeRepository.save(time);
        return "redirect:/times";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioDeEdicao(@PathVariable Integer id, Model model) {
        Time time = timeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("time", time);
        return "times/editar";
    }

    @PostMapping("/update/{id}")
    public String atualizarTime(@PathVariable Integer id, @Valid @ModelAttribute Time time, BindingResult result) {
        if (result.hasErrors()) {
            time.setId(id);
            return "times/editar";
        }
        timeRepository.save(time);
        return "redirect:/times";
    }

    @GetMapping("/delete/{id}")
    public String deletarTime(@PathVariable Integer id) {
        Time time = timeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        timeRepository.delete(time);
        return "redirect:/times";
    }
}
