package com.gerenciador.trabalhos.dto;


public class MatriculaDTO {

    private Long idDisciplina;
    private String nome;
    private String dataInicio;
    private String dataFim;
    private String nomeProfessor;

    public MatriculaDTO(Long idDisciplina, String nome, String dataInicio,
            String dataFim, String nomeProfessor) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.nomeProfessor = nomeProfessor;
    }

    // getters
    public Long getIdDisciplina() {
        return idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

}