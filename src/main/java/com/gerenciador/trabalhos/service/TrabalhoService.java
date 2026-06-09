package com.gerenciador.trabalhos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gerenciador.trabalhos.dto.TrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.TrabalhoResponseDTO;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.Trabalho;
import com.gerenciador.trabalhos.repository.DisciplinaRepository;
import com.gerenciador.trabalhos.repository.TrabalhoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrabalhoService {

    private final TrabalhoRepository trabalhoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public TrabalhoResponseDTO cadastrar(TrabalhoRequestDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        Trabalho trabalho = new Trabalho();
        trabalho.setTitulo(dto.titulo());
        trabalho.setDescricao(dto.descricao());
        trabalho.setLinkArquivoTrabalho(dto.linkArquivoTrabalho());
        trabalho.setDataInicio(dto.dataInicio());
        trabalho.setDataFim(dto.dataFim());
        trabalho.setDisciplina(disciplina);

        trabalhoRepository.save(trabalho);

        return toDTO(trabalho);
    }

    public List<TrabalhoResponseDTO> listarTodos() {
        return trabalhoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public TrabalhoResponseDTO atualizar(Long id, TrabalhoRequestDTO dto) {
        Trabalho trabalho = trabalhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        if (dto.titulo() != null) {
            trabalho.setTitulo(dto.titulo());
        }
        if (dto.descricao() != null) {
            trabalho.setDescricao(dto.descricao());
        }
        if (dto.linkArquivoTrabalho() != null) {
            trabalho.setLinkArquivoTrabalho(dto.linkArquivoTrabalho());
        }
        if (dto.dataInicio() != null) {
            trabalho.setDataInicio(dto.dataInicio());
        }
        if (dto.dataFim() != null) {
            trabalho.setDataFim(dto.dataFim());
        }
        if (dto.disciplinaId() != null) {
            Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
            trabalho.setDisciplina(disciplina);
        }

        trabalhoRepository.save(trabalho);

        return toDTO(trabalho);
    }

    public void deletar(Long id) {
        trabalhoRepository.deleteById(id);
    }

    public List<TrabalhoResponseDTO> listarPorDisciplina(Long disciplinaId) {
        return trabalhoRepository.findByDisciplinaId(disciplinaId).stream()
                .map(this::toDTO)
                .toList();
    }

    private TrabalhoResponseDTO toDTO(Trabalho trabalho) {
        return new TrabalhoResponseDTO(
                trabalho.getId(),
                trabalho.getTitulo(),
                trabalho.getDescricao(),
                trabalho.getLinkArquivoTrabalho(),
                trabalho.getDataInicio(),
                trabalho.getDataFim(),
                trabalho.getDisciplina().getId(),
                trabalho.getDisciplina().getNome());
    }
}
