package com.gerenciador.trabalhos.model;

import jakarta.persistence.Column;
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

    @Column(length = 500)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 500)
    private String caminhoArquivoPdf;

    @Column(length = 50)
    private String dataInicio;

    @Column(length = 50)
    private String dataFim;

    @ManyToOne
    private Disciplina disciplina;
}