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
    private String linkArquivoTrabalho;
    private String dataInicio;
    private String dataFim;

    @ManyToOne
    private Disciplina disciplina;
}