package com.gerenciador.trabalhos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Disciplina {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String dataInicio;
    private String dataFim;

    @ManyToOne
    private Professor professor;

    @JsonIgnore
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;
}
