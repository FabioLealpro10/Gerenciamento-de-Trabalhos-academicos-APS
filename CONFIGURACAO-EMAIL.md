# Configuração de e-mail (recuperação de senha)

As credenciais de envio de e-mail **não ficam no código** e **não devem ir para o GitHub**.  
Elas são configuradas localmente no arquivo `.env` na raiz do projeto.

---

## Onde colocar as credenciais

| O que | Onde colocar |
|-------|----------------|
| E-mail remetente | Arquivo `.env` → `SPRING_MAIL_USERNAME` |
| Senha de app Google | Arquivo `.env` → `SPRING_MAIL_PASSWORD` |

**Não coloque** e-mail ou senha em:

- `application.properties`
- `docker-compose.yml`
- código Java
- commits do Git

---

## Passo a passo para inicializar

### 1. Gerar senha de app no Google

1. Acesse a conta Google usada para enviar e-mails.
2. Ative a **verificação em duas etapas** na conta.
3. Acesse: [https://myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
4. Crie uma senha de app (tipo "Mail" / "Outro").
5. Copie a senha gerada (16 caracteres, sem espaços).

> Contas `@uea.edu.br` em Google Workspace também usam `smtp.gmail.com`.

---

### 2. Criar o arquivo `.env`

Na raiz do projeto, copie o modelo:

**Windows (PowerShell):**
```powershell
copy .env.example .env
```

**Linux / Mac:**
```bash
cp .env.example .env
```

Edite o `.env` e preencha com seus dados reais:

```env
SPRING_MAIL_USERNAME=seu-email@gmail.com
SPRING_MAIL_PASSWORD=abcdefghijklmnop
```

Substitua pelos seus valores. A senha de app vai **sem espaços**.

> O Docker Compose lê o `.env` automaticamente na raiz do projeto.  
> Se o arquivo não existir, a API sobe normalmente, mas o envio de e-mail não funciona.

---

### 3. Subir a aplicação com Docker

```powershell
docker-compose down
docker-compose up --build
```

O `docker-compose.yml` lê automaticamente o arquivo `.env` e injeta as variáveis no container.

---

### 4. Testar o envio

**POST** `http://localhost:8080/auth/esqueci-senha`

```json
{
  "email": "email-cadastrado-no-sistema@exemplo.com"
}
```

Resposta esperada (e-mail cadastrado):

```json
{
  "emailCadastrado": true,
  "mensagem": "Enviamos um código para seu email"
}
```

O código chega no e-mail informado e expira em **5 minutos**.

---

## Segurança no GitHub

- O arquivo `.env` está no `.gitignore` e **não será enviado** ao repositório.
- No GitHub fica apenas o `.env.example` com valores fictícios.
- Se alguma senha de app já foi commitada antes, **revogue e gere uma nova** no Google.

---

## Variáveis disponíveis

| Variável | Obrigatória | Descrição | Padrão |
|----------|-------------|-----------|--------|
| `SPRING_MAIL_USERNAME` | Sim | E-mail remetente (conta Google) | — |
| `SPRING_MAIL_PASSWORD` | Sim | Senha de app do Google | — |
| `SPRING_MAIL_HOST` | Não | Servidor SMTP | `smtp.gmail.com` |
| `SPRING_MAIL_PORT` | Não | Porta SMTP | `587` |

---

## Erros comuns

| Erro | Causa provável | Solução |
|------|----------------|---------|
| `E-mail remetente não configurado` | `.env` ausente ou `SPRING_MAIL_USERNAME` vazio | Criar/editar `.env` e reiniciar Docker |
| `Authentication failed` | Senha de app incorreta ou revogada | Gerar nova senha de app no Google |
| Erro de certificado SSL | Problema TLS no container | Rebuild: `docker-compose up --build` |

---

## Rodar sem Docker (opcional)

Defina as variáveis no terminal antes de iniciar a aplicação:

**Windows (PowerShell):**
```powershell
$env:SPRING_MAIL_USERNAME="seu-email@gmail.com"
$env:SPRING_MAIL_PASSWORD="sua-senha-de-app"
```

Depois inicie a API normalmente pela IDE ou com Maven.
