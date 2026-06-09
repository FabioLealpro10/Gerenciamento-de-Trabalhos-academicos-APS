package com.gerenciador.trabalhos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class EntregaTrabalho {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkArquivo;
    private String dataEntrega;
    private Float nota;
    private String feedback;

    @ManyToOne
    private Trabalho trabalho;

    @ManyToOne
    private Aluno aluno;
}