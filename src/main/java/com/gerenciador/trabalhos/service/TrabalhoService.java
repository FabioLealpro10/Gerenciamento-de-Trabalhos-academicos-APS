package com.gerenciador.trabalhos.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.dto.TrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.TrabalhoResponseDTO;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.EntregaTrabalho;
import com.gerenciador.trabalhos.model.Trabalho;
import com.gerenciador.trabalhos.repository.DisciplinaRepository;
import com.gerenciador.trabalhos.repository.EntregaTrabalhoRepository;
import com.gerenciador.trabalhos.repository.TrabalhoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrabalhoService {

    private final TrabalhoRepository trabalhoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final EntregaTrabalhoRepository entregaTrabalhoRepository;
    private final ArquivoService arquivoService;

    public TrabalhoResponseDTO cadastrar(TrabalhoRequestDTO dto, MultipartFile arquivo) {
        Disciplina disciplina = disciplinaRepository.findById(dto.disciplinaId())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        String caminhoPdf = arquivoService.salvarPdf(arquivo, "trabalhos");

        Trabalho trabalho = new Trabalho();
        trabalho.setTitulo(dto.titulo());
        trabalho.setDescricao(dto.descricao());
        trabalho.setCaminhoArquivoPdf(caminhoPdf);
        trabalho.setDataInicio(dto.dataInicio());
        trabalho.setDataFim(dto.dataFim());
        trabalho.setDisciplina(disciplina);

        trabalhoRepository.save(trabalho);

        return toDTO(trabalho);
    }

    public PageResponseDTO<TrabalhoResponseDTO> listarTodos(int page, int size) {
        return PageResponseDTO.from(
                trabalhoRepository.findAll(PageRequest.of(page, size))
                        .map(this::toDTO));
    }

    public TrabalhoResponseDTO atualizar(Long id, TrabalhoRequestDTO dto, MultipartFile arquivo) {
        Trabalho trabalho = trabalhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        if (dto.titulo() != null) {
            trabalho.setTitulo(dto.titulo());
        }
        if (dto.descricao() != null) {
            trabalho.setDescricao(dto.descricao());
        }
        if (arquivo != null && !arquivo.isEmpty()) {
            arquivoService.excluirSeExistir(trabalho.getCaminhoArquivoPdf());
            trabalho.setCaminhoArquivoPdf(arquivoService.salvarPdf(arquivo, "trabalhos"));
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

    @Transactional
    public void deletar(Long id) {
        Trabalho trabalho = trabalhoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        for (EntregaTrabalho entrega : entregaTrabalhoRepository.findByTrabalhoId(id)) {
            arquivoService.excluirSeExistir(entrega.getCaminhoArquivoPdf());
        }
        entregaTrabalhoRepository.deleteByTrabalhoId(id);

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
    }

    private TrabalhoResponseDTO toDTO(Trabalho trabalho) {
        return new TrabalhoResponseDTO(
                trabalho.getId(),
                trabalho.getTitulo(),
                trabalho.getDescricao(),
                trabalho.getCaminhoArquivoPdf(),
                trabalho.getDataInicio(),
                trabalho.getDataFim(),
                trabalho.getDisciplina().getId(),
                trabalho.getDisciplina().getNome());
    }
}
