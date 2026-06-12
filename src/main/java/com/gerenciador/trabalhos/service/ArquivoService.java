package com.gerenciador.trabalhos.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArquivoService {

    public static final long MAX_PDF_SIZE_BYTES = 8L * 1024 * 1024;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String salvarPdf(MultipartFile arquivo, String subPasta) {
        validarPdf(arquivo);

        try {
            // Caminho absoluto: transferTo com caminho relativo resolve contra o
            // diretório temporário do Tomcat, não contra a pasta do projeto
            Path pasta = Paths.get(uploadDir, subPasta).toAbsolutePath().normalize();
            Files.createDirectories(pasta);

            String nomeArquivo = UUID.randomUUID() + ".pdf";
            Path destino = pasta.resolve(nomeArquivo);

            try (var entrada = arquivo.getInputStream()) {
                Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
            }

            return subPasta + "/" + nomeArquivo;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo PDF");
        }
    }

    public Resource carregarArquivo(String caminhoRelativo) {
        if (caminhoRelativo == null || caminhoRelativo.isBlank()) {
            throw new RuntimeException("Nenhum arquivo PDF foi enviado para este registro");
        }

        try {
            Path caminho = Paths.get(uploadDir).resolve(caminhoRelativo).normalize();
            Path base = Paths.get(uploadDir).toAbsolutePath().normalize();

            if (!caminho.toAbsolutePath().normalize().startsWith(base)) {
                throw new RuntimeException("Caminho de arquivo inválido");
            }

            Resource resource = new UrlResource(caminho.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Arquivo PDF não encontrado");
            }

            return resource;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o arquivo PDF");
        }
    }

    public void excluirSeExistir(String caminhoRelativo) {
        if (caminhoRelativo == null || caminhoRelativo.isBlank()) {
            return;
        }

        try {
            Path caminho = Paths.get(uploadDir).resolve(caminhoRelativo).normalize();
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao excluir o arquivo PDF");
        }
    }

    private void validarPdf(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new RuntimeException("O arquivo PDF é obrigatório");
        }

        if (arquivo.getSize() > MAX_PDF_SIZE_BYTES) {
            throw new RuntimeException("Arquivo PDF excede o limite de 8 MB");
        }

        String nomeOriginal = arquivo.getOriginalFilename();
        boolean extensaoPdf = nomeOriginal != null && nomeOriginal.toLowerCase().endsWith(".pdf");
        String contentType = arquivo.getContentType();
        boolean tipoPdf = contentType != null && contentType.equalsIgnoreCase("application/pdf");

        if (!extensaoPdf && !tipoPdf) {
            throw new RuntimeException("Apenas arquivos PDF são permitidos");
        }
    }
}
