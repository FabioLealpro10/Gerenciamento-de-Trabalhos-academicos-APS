package com.gerenciador.trabalhos.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gerenciador.trabalhos.dto.AlunoDTO;
import com.gerenciador.trabalhos.dto.PageResponseDTO;
import com.gerenciador.trabalhos.model.Aluno;
import com.gerenciador.trabalhos.model.EntregaTrabalho;
import com.gerenciador.trabalhos.repository.AlunoRepository;
import com.gerenciador.trabalhos.repository.EntregaTrabalhoRepository;
import com.gerenciador.trabalhos.repository.MatriculaRepository;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final EntregaTrabalhoRepository entregaTrabalhoRepository;
    private final MatriculaRepository matriculaRepository;
    private final ArquivoService arquivoService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public AlunoService(
            AlunoRepository alunoRepository,
            EntregaTrabalhoRepository entregaTrabalhoRepository,
            MatriculaRepository matriculaRepository,
            ArquivoService arquivoService,
            PasswordEncoder passwordEncoder,
            UsuarioService usuarioService) {
        this.alunoRepository = alunoRepository;
        this.entregaTrabalhoRepository = entregaTrabalhoRepository;
        this.matriculaRepository = matriculaRepository;
        this.arquivoService = arquivoService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    public AlunoDTO criar(AlunoDTO dto) {
        usuarioService.validarEmailDisponivel(dto.email(), null);

        Aluno aluno = new Aluno();
        aluno.setEmail(dto.email());
        aluno.setNome(dto.nome());
        aluno.setPassword(passwordEncoder.encode(dto.password()));
        aluno.setRole(usuarioService.toDatabaseRole(dto.role() != null ? dto.role() : "ALUNO"));
        aluno.setTurma(dto.turma());

        alunoRepository.save(aluno);

        return converterParaDTO(aluno);
    }

    public PageResponseDTO<AlunoDTO> listarTodos(int page, int size) {
        return PageResponseDTO.from(
                alunoRepository.findAll(PageRequest.of(page, size))
                        .map(this::converterParaDTO));
    }

    public PageResponseDTO<AlunoDTO> pesquisarPorNome(String nome, int page, int size) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Informe o parâmetro 'nome' com a subpalavra desejada");
        }

        return PageResponseDTO.from(
                alunoRepository.findByNomeContainingIgnoreCase(nome.trim(), PageRequest.of(page, size))
                        .map(this::converterParaDTO));
    }

    public AlunoDTO atualizar(Long id, AlunoDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (dto.nome() != null) {
            aluno.setNome(dto.nome());
        }
        if (dto.email() != null) {
            usuarioService.validarEmailDisponivel(dto.email(), aluno.getId());
            aluno.setEmail(dto.email());
        }
        if (dto.password() != null && !dto.password().isBlank()) {
            aluno.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.role() != null) {
            aluno.setRole(usuarioService.toDatabaseRole(dto.role()));
        }
        if (dto.turma() != null) {
            aluno.setTurma(dto.turma());
        }

        alunoRepository.save(aluno);

        return converterParaDTO(aluno);
    }

    @Transactional
    public void deletar(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        for (EntregaTrabalho entrega : entregaTrabalhoRepository.findByAlunoId(id)) {
            arquivoService.excluirSeExistir(entrega.getCaminhoArquivoPdf());
        }

        entregaTrabalhoRepository.deleteByAlunoId(id);
        matriculaRepository.deleteByAlunoId(id);
        alunoRepository.delete(aluno);
    }

    public AlunoDTO buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    private AlunoDTO converterParaDTO(Aluno aluno) {
        return new AlunoDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getPassword(),
                aluno.getRole(),
                aluno.getTurma());
            
    }
}
