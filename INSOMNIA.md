# Credenciais e requisições — Insomnia

Guia para testar a API **Gerenciador de Trabalhos** no [Insomnia](https://insomnia.rest/).

---

## Pré-requisito

A API precisa estar rodando (Docker):

```powershell
cd "C:\Users\fabio\Documents\DSW\Minha-API\Gerenciamento-de-Trabalhos-academicos-APS"
docker compose up -d --build
```

URL base: **`http://localhost:8080`**

Swagger (alternativa ao Insomnia): http://localhost:8080/swagger-ui.html

---

## Credenciais de teste

| Campo    | Valor      |
|----------|------------|
| Email    | `admmaster@local` |
| Senha    | `123456`   |
| Perfil   | `ADMIN`    |

> Esses dados vêm do arquivo `src/main/resources/import.sql`.  
> Se o login falhar após mudanças no SQL, recrie o banco:
>
> ```powershell
> docker compose down -v
> docker compose up -d --build
> ```

---

## 1. Login (obter o token JWT)

### Configuração no Insomnia

| Campo        | Valor |
|--------------|-------|
| **Método**   | `POST` |
| **URL**      | `http://localhost:8080/auth/login` |
| **Body type**| `JSON` |

### Corpo da requisição (JSON)

```json
{
  "email": "admmaster@local",
  "password": "123456"
}
```

### Headers

| Nome           | Valor              |
|----------------|--------------------|
| `Content-Type` | `application/json` |

### Resposta esperada (sucesso — HTTP 200)

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": 1,
  "nome": "AdmMaster",
  "email": "admmaster@local",
  "role": "ADMIN"
}
```

Copie o valor de `token` para usar nas próximas requisições.

### Erros comuns

| Situação no Insomnia | Causa provável |
|----------------------|----------------|
| `No body returned for response` | Credenciais erradas ou usuário inexistente no banco (HTTP 401 sem corpo) |
| Erro de conexão | API parada ou Docker não rodando |
| `403 Forbidden` | URL ou método incorretos |
| Erro de JSON | Body não está em JSON ou `Content-Type` ausente |

---

## 2. Usar o token nas outras rotas

Em qualquer endpoint protegido, adicione o header:

| Nome            | Valor |
|-----------------|-------|
| `Authorization` | `Bearer SEU_TOKEN_AQUI` |

**Exemplo** (substitua pelo token retornado no login):

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG10ZXN0ZSIs...
```

### No Insomnia (recomendado)

1. Abra a requisição de login → envie → copie o `token`.
2. Crie um **Environment** (ou use Request → Auth):
   - Variável: `token` = valor copiado
3. Nas outras requisições, header:
   - `Authorization` = `Bearer {{ token }}`

---

## 3. Exemplos de outras rotas

### Cadastro de usuário (somente ADMIN)

| Campo      | Valor |
|------------|-------|
| Método     | `POST` |
| URL        | `http://localhost:8080/auth/register` |
| Auth       | Bearer token do admin |
| Body (JSON)| ver abaixo |

```json
{
  "nome": "Novo Admin",
  "email": "novo_admin@local",
  "password": "senha123"
}
```

### Listar alunos (exemplo protegido)

| Campo | Valor |
|-------|-------|
| Método | `GET` |
| URL | `http://localhost:8080/api/alunos` |
| Auth | `Bearer <token>` |

> Alguns endpoints `POST` estão liberados sem token na configuração atual; consulte o Swagger para a lista completa.

---

## 4. Checklist rápido no Insomnia

- [ ] Método **POST** (não GET)
- [ ] URL exata: `http://localhost:8080/auth/login`
- [ ] Body tipo **JSON** com `email` e `password`
- [ ] Header `Content-Type: application/json`
- [ ] Docker com containers **running** (`docker compose ps`)
- [ ] Status **200** e corpo com campo `token`

---

## 5. Teste rápido no PowerShell

```powershell
$body = @{ email = "admmaster@local"; password = "123456" } | ConvertTo-Json
Invoke-RestMethod -Method POST -Uri "http://localhost:8080/auth/login" -ContentType "application/json" -Body $body
```

Se retornar `token`, a API e as credenciais estão corretas.

---

## Referência

| Item | Valor |
|------|-------|
| Base URL | `http://localhost:8080` |
| Login | `POST /auth/login` |
| Register | `POST /auth/register` (requer token ADMIN) |
| Swagger | http://localhost:8080/swagger-ui.html |
| phpMyAdmin (Docker) | http://localhost:8081 — user `appuser`, senha `secret` |


Cadastro de permição de usuarios

"role": "ALUNO", "role": "PROFESS", "role": "ADMIN",




 