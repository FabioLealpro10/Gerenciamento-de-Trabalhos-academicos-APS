package com.gerenciador.trabalhos.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gerenciador.trabalhos.dto.EntregaTrabalhoCorrecaoDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoRequestDTO;
import com.gerenciador.trabalhos.dto.EntregaTrabalhoResponseDTO;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.exception.EntregaForaDoPrazoException;
import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.model.Disciplina;
import com.gerenciador.trabalhos.model.EntregaTrabalho;
import com.gerenciador.trabalhos.model.Trabalho;
import com.gerenciador.trabalhos.repository.AlunoRepository;
import com.gerenciador.trabalhos.repository.EntregaTrabalhoRepository;
import com.gerenciador.trabalhos.repository.TrabalhoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntregaTrabalhoService {

    private final EntregaTrabalhoRepository entregaRepo;
    private final TrabalhoRepository trabalhoRepo;
    private final AlunoRepository alunoRepo;
    private final ArquivoService arquivoService;

    public EntregaTrabalhoResponseDTO entregar(EntregaTrabalhoRequestDTO dto, MultipartFile arquivo) {

        Trabalho trabalho = trabalhoRepo.findById(dto.trabalhoId())
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        Aluno aluno = alunoRepo.findById(dto.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        entregaRepo.findByAlunoIdAndTrabalhoId(dto.alunoId(), dto.trabalhoId())
                .ifPresent(e -> {
                    throw new RuntimeException("Este aluno já entregou este trabalho!");
                });

        validarPrazoEntrega(trabalho, dto.dataEntrega(), aluno);

        String caminhoPdf = arquivoService.salvarPdf(arquivo, "entregas");

        EntregaTrabalho entrega = new EntregaTrabalho();
        entrega.setCaminhoArquivoPdf(caminhoPdf);
        entrega.setDataEntrega(dto.dataEntrega());
        entrega.setTrabalho(trabalho);
        entrega.setAluno(aluno);

        entregaRepo.save(entrega);

        return toDTO(entrega, "Trabalho entregue com sucesso");
    }

    public PageResponseDTO<EntregaTrabalhoResponseDTO> listarTodos(int page, int size, String pesquisa) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EntregaTrabalho> resultado = temPesquisa(pesquisa)
                ? entregaRepo.findByPesquisa(pesquisa.trim(), pageable)
                : entregaRepo.findAll(pageable);

        return toPageResponse(resultado);
    }

    public PageResponseDTO<EntregaTrabalhoResponseDTO> listarPorTrabalho(
            Long trabalhoId, int page, int size, String pesquisa) {

        trabalhoRepo.findById(trabalhoId)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        Page<EntregaTrabalho> resultado = temPesquisa(pesquisa)
                ? entregaRepo.findByTrabalhoIdAndPesquisa(trabalhoId, pesquisa.trim(), pageable)
                : entregaRepo.findByTrabalhoId(trabalhoId, pageable);

        return toPageResponse(resultado);
    }

    public EntregaTrabalhoResponseDTO buscarEntregaAluno(Long alunoId, Long trabalhoId) {
        Trabalho trabalho = trabalhoRepo.findById(trabalhoId)
                .orElseThrow(() -> new RuntimeException("Trabalho não encontrado"));

        Aluno aluno = alunoRepo.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        EntregaTrabalho entrega = entregaRepo.findByAlunoIdAndTrabalhoId(alunoId, trabalhoId)
                .orElse(null);

        if (entrega == null) {
            return criarContexto(trabalho, aluno, null, null, null, null, "trabalho a fazer");
        }

        if (entrega.getNota() == null) {
            return criarContexto(
                    trabalho,
                    aluno,
                    entrega.getId(),
                    entrega.getCaminhoArquivoPdf(),
                    entrega.getDataEntrega(),
                    null,
                    "trabalho não corrigido");
        }

        return toDTO(entrega);
    }

    public EntregaTrabalhoResponseDTO atualizar(
            Long trabalhoId, Long alunoId, String dataEntrega, MultipartFile arquivo) {

        EntregaTrabalho entrega = entregaRepo.findByAlunoIdAndTrabalhoId(alunoId, trabalhoId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        Trabalho trabalho = entrega.getTrabalho();

        if (arquivo != null && !arquivo.isEmpty()) {
            arquivoService.excluirSeExistir(entrega.getCaminhoArquivoPdf());
            entrega.setCaminhoArquivoPdf(arquivoService.salvarPdf(arquivo, "entregas"));
        }
        if (dataEntrega != null) {
            validarPrazoEntrega(trabalho, dataEntrega, entrega.getAluno());
            entrega.setDataEntrega(dataEntrega);
        }

        entregaRepo.save(entrega);

        return toDTO(entrega, "Entrega atualizada com sucesso");
    }

    public EntregaTrabalhoResponseDTO corrigir(Long trabalhoId, Long alunoId, EntregaTrabalhoCorrecaoDTO dto) {
        EntregaTrabalho entrega = entregaRepo.findByAlunoIdAndTrabalhoId(alunoId, trabalhoId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        entrega.setNota(dto.nota());
        if (dto.feedback() != null && !dto.feedback().isEmpty()) {
            entrega.setFeedback(dto.feedback());
        }

        entregaRepo.save(entrega);

        return toDTO(entrega, "Trabalho corrigido com sucesso");
    }

    public Resource baixarPdf(Long id) {
        EntregaTrabalho entrega = entregaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        return arquivoService.carregarArquivo(entrega.getCaminhoArquivoPdf());
    }

    private void validarPrazoEntrega(Trabalho trabalho, String dataEntrega, Aluno aluno) {
        if (trabalho.getDataFim() == null || trabalho.getDataFim().isBlank()) {
            return;
        }

        LocalDate dataFim = parseData(trabalho.getDataFim());
        LocalDate dataEnt = parseData(dataEntrega);

        if (dataEnt.isAfter(dataFim)) {
            throw new EntregaForaDoPrazoException(
                    criarContexto(trabalho, aluno, null, null, dataEntrega, null,
                            "Trabalho fora do prazo de envio"));
        }
    }

    private LocalDate parseData(String data) {
        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválido. Use o formato yyyy-MM-dd");
        }
    }

    private boolean temPesquisa(String pesquisa) {
        return pesquisa != null && !pesquisa.isBlank();
    }

    private PageResponseDTO<EntregaTrabalhoResponseDTO> toPageResponse(Page<EntregaTrabalho> page) {
        return new PageResponseDTO<>(
                page.getContent().stream().map(this::toDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }

    private EntregaTrabalhoResponseDTO criarContexto(
            Trabalho trabalho,
            Aluno aluno,
            Long id,
            String caminhoArquivoPdf,
            String dataEntrega,
            Float nota,
            String mensagem) {

        Disciplina disciplina = trabalho.getDisciplina();
        String disciplinaNome = disciplina != null ? disciplina.getNome() : null;
        String professorNome = disciplina != null && disciplina.getProfessor() != null
                ? disciplina.getProfessor().getNome()
                : null;

        return new EntregaTrabalhoResponseDTO(
                id,
                caminhoArquivoPdf,
                dataEntrega,
                nota,
                null,
                mensagem,
                trabalho.getId(),
                trabalho.getTitulo(),
                disciplinaNome,
                professorNome,
                aluno.getId(),
                aluno.getNome());
    }

    private EntregaTrabalhoResponseDTO toDTO(EntregaTrabalho e) {
        return toDTO(e, null);
    }

    private EntregaTrabalhoResponseDTO toDTO(EntregaTrabalho e, String mensagem) {
        Trabalho trabalho = e.getTrabalho();
        Disciplina disciplina = trabalho.getDisciplina();
        String disciplinaNome = disciplina != null ? disciplina.getNome() : null;
        String professorNome = disciplina != null && disciplina.getProfessor() != null
                ? disciplina.getProfessor().getNome()
                : null;

        return new EntregaTrabalhoResponseDTO(
                e.getId(),
                e.getCaminhoArquivoPdf(),
                e.getDataEntrega(),
                e.getNota(),
                e.getFeedback(),
                mensagem,
                trabalho.getId(),
                trabalho.getTitulo(),
                disciplinaNome,
                professorNome,
                e.getAluno().getId(),
                e.getAluno().getNome());
    }
}
