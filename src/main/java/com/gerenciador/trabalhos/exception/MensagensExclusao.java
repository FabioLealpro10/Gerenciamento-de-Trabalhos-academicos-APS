package com.gerenciador.trabalhos.exception;

public final class MensagensExclusao {

    private MensagensExclusao() {
    }

    public static final String TRABALHO_COM_ENTREGAS =
            "Não foi possível excluir este trabalho porque existem entregas de alunos vinculadas a ele. "
                    + "Remova as entregas antes de excluir o trabalho.";

    public static final String ALUNO_COM_ENTREGAS =
            "Não foi possível excluir este aluno porque existem entregas de trabalho vinculadas a ele. "
                    + "Remova as entregas antes de excluir o aluno.";

    public static final String ALUNO_COM_MATRICULAS =
            "Não foi possível excluir este aluno porque existem matrículas vinculadas a ele. "
                    + "Remova as matrículas antes de excluir o aluno.";

    public static final String PROFESSOR_COM_DISCIPLINAS =
            "Não foi possível excluir este professor porque existem disciplinas vinculadas a ele. "
                    + "Remova ou transfira as disciplinas antes de excluir o professor.";

    public static final String DISCIPLINA_COM_TRABALHOS =
            "Não foi possível excluir esta disciplina porque existem trabalhos vinculados a ela. "
                    + "Remova os trabalhos antes de excluir a disciplina.";

    public static final String DISCIPLINA_COM_MATRICULAS =
            "Não foi possível excluir esta disciplina porque existem alunos matriculados nela. "
                    + "Remova as matrículas antes de excluir a disciplina.";

    public static final String GENERICA =
            "Não foi possível excluir este registro porque existem dados vinculados a ele no sistema. "
                    + "Remova os vínculos antes de tentar novamente.";

    public static boolean ehErroDeIntegridade(String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            return false;
        }
        String lower = mensagem.toLowerCase();
        return lower.contains("foreign key constraint")
                || lower.contains("cannot delete or update a parent row")
                || lower.contains("could not execute statement");
    }

    public static String traduzir(String causa) {
        if (causa == null || causa.isBlank()) {
            return GENERICA;
        }

        String lower = causa.toLowerCase();

        if (lower.contains("entrega_trabalho") && lower.contains("trabalho_id")) {
            return TRABALHO_COM_ENTREGAS;
        }
        if (lower.contains("entrega_trabalho") && lower.contains("aluno_id")) {
            return ALUNO_COM_ENTREGAS;
        }
        if (lower.contains("trabalho") && lower.contains("disciplina_id")) {
            return DISCIPLINA_COM_TRABALHOS;
        }
        if (lower.contains("matricula") && lower.contains("disciplina_id")) {
            return DISCIPLINA_COM_MATRICULAS;
        }
        if (lower.contains("matricula") && lower.contains("aluno_id")) {
            return ALUNO_COM_MATRICULAS;
        }
        if (lower.contains("disciplina") && lower.contains("professor_id")) {
            return PROFESSOR_COM_DISCIPLINAS;
        }

        return GENERICA;
    }
}
