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

<<<<<<< HEAD
    private String caminhoArquivoPdf;
=======
    private String linkArquivo;
>>>>>>> f099fe231ca3115077bf2c1ca32f1cbcf3dd06ce
    private String dataEntrega;
    private Float nota;
    private String feedback;

    @ManyToOne
    private Trabalho trabalho;

    @ManyToOne
    private Aluno aluno;
}