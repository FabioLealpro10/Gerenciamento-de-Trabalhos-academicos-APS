package com.gerenciador.trabalhos.service;

<<<<<<< HEAD
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
=======
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce

import com.gerenciador.trabalhos.dto.PageResponseDTO;
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
<<<<<<< HEAD
    private final ArquivoService arquivoService;

    public TrabalhoResponseDTO cadastrar(TrabalhoRequestDTO dto, MultipartFile arquivo) {
        Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        String caminhoPdf = arquivoService.salvarPdf(arquivo, "trabalhos");

        Trabalho trabalho = new Trabalho();
        trabalho.setTitulo(dto.titulo());
        trabalho.setDescricao(dto.descricao());
        trabalho.setCaminhoArquivoPdf(caminhoPdf);
=======

    public TrabalhoResponseDTO cadastrar(TrabalhoRequestDTO dto) {
        Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        Trabalho trabalho = new Trabalho();
        trabalho.setTitulo(dto.titulo());
        trabalho.setDescricao(dto.descricao());
        trabalho.setLinkArquivoTrabalho(dto.linkArquivoTrabalho());
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
        trabalho.setDataInicio(dto.dataInicio());
        trabalho.setDataFim(dto.dataFim());
        trabalho.setDisciplina(disciplina);

        trabalhoRepository.save(trabalho);

        return toDTO(trabalho);
    }

<<<<<<< HEAD
    public PageResponseDTO<TrabalhoResponseDTO> listarTodos(int page, int size) {
        return PageResponseDTO.from(
                trabalhoRepository.findAll(PageRequest.of(page, size))
                        .map(this::toDTO));
    }

    public TrabalhoResponseDTO atualizar(Long id, TrabalhoRequestDTO dto, MultipartFile arquivo) {
=======
    public List<TrabalhoResponseDTO> listarTodos() {
        return trabalhoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public PageResponseDTO<TrabalhoResponseDTO> listarTodosPaginado(Pageable pageable) {
        Page<Trabalho> page = trabalhoRepository.findAll(pageable);
        return new PageResponseDTO<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public TrabalhoResponseDTO atualizar(Long id, TrabalhoRequestDTO dto) {
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
        Trabalho trabalho = trabalhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        if (dto.titulo() != null) {
            trabalho.setTitulo(dto.titulo());
        }
        if (dto.descricao() != null) {
            trabalho.setDescricao(dto.descricao());
        }
<<<<<<< HEAD
        if (arquivo != null && !arquivo.isEmpty()) {
            arquivoService.excluirSeExistir(trabalho.getCaminhoArquivoPdf());
            trabalho.setCaminhoArquivoPdf(arquivoService.salvarPdf(arquivo, "trabalhos"));
=======
        if (dto.linkArquivoTrabalho() != null) {
            trabalho.setLinkArquivoTrabalho(dto.linkArquivoTrabalho());
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
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
<<<<<<< HEAD
        Trabalho trabalho = trabalhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        arquivoService.excluirSeExistir(trabalho.getCaminhoArquivoPdf());
        trabalhoRepository.delete(trabalho);
    }

    public PageResponseDTO<TrabalhoResponseDTO> listarPorDisciplina(Long disciplinaId, int page, int size) {
        return PageResponseDTO.from(
                trabalhoRepository.findByDisciplinaId(disciplinaId, PageRequest.of(page, size))
                        .map(this::toDTO));
    }

    public PageResponseDTO<TrabalhoResponseDTO> pesquisarPorNome(String nome, int page, int size) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Informe o parâmetro 'nome' com a subpalavra desejada");
        }

        return PageResponseDTO.from(
                trabalhoRepository.findByTituloContainingIgnoreCase(nome.trim(), PageRequest.of(page, size))
                        .map(this::toDTO));
    }

    public Resource baixarPdf(Long id) {
        Trabalho trabalho = trabalhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        return arquivoService.carregarArquivo(trabalho.getCaminhoArquivoPdf());
=======
        trabalhoRepository.deleteById(id);
    }

    public List<TrabalhoResponseDTO> listarPorDisciplina(Long disciplinaId) {
        return trabalhoRepository.findByDisciplinaId(disciplinaId).stream()
                .map(this::toDTO)
                .toList();
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    }

    private TrabalhoResponseDTO toDTO(Trabalho trabalho) {
        return new TrabalhoResponseDTO(
                trabalho.getId(),
                trabalho.getTitulo(),
                trabalho.getDescricao(),
<<<<<<< HEAD
                trabalho.getCaminhoArquivoPdf(),
=======
                trabalho.getLinkArquivoTrabalho(),
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
                trabalho.getDataInicio(),
                trabalho.getDataFim(),
                trabalho.getDisciplina().getId(),
                trabalho.getDisciplina().getNome());
    }
}
