package com.gerenciador.trabalhos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Trabalho {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
<<<<<<< HEAD
    private String caminhoArquivoPdf;
=======
    private String linkArquivoTrabalho;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    private String dataInicio;
    private String dataFim;

    @ManyToOne
    private Disciplina disciplina;
}