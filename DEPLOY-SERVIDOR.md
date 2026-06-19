# Deploy no servidor

Guia para publicar a **API (backend)** e integrar com o **frontend** no servidor `sistemas-web`, usando as portas disponíveis.

---

## Portas sugeridas

| Porta | Uso recomendado | Serviço |
|-------|-----------------|---------|
| **9904** | Frontend (site) | HTML/Angular/React servido por Nginx ou `npx serve` |
| **9905** | Backend (API) | Spring Boot — este repositório |
| **9906** | Reserva | Swagger, monitoramento ou outro serviço |
| **9910** | Reserva | Uso futuro |

> **MySQL não precisa de porta pública.** No Docker ele fica só na rede interna entre os containers.

---

## O que você precisa no servidor

### 1. Software instalado

```bash
sudo apt update
sudo apt install -y docker.io docker-compose-plugin git
sudo usermod -aG docker $USER
```

Depois faça logout/login para usar Docker sem `sudo`.

### 2. Código no servidor

```bash
cd ~
git clone <url-do-seu-repositorio> gerenciador-trabalhos
cd gerenciador-trabalhos
```

### 3. Arquivo `.env` (credenciais — não vai pro GitHub)

```bash
cp .env.example .env
nano .env
```

Preencha **no mínimo**:

```env
# E-mail (recuperação de senha)
SPRING_MAIL_USERNAME=seu-email@gmail.com
SPRING_MAIL_PASSWORD=sua-senha-de-app

# Segurança
JWT_SECRET=uma-chave-longa-e-aleatoria-com-pelo-menos-32-caracteres
MYSQL_PASSWORD=senha-forte-do-banco
MYSQL_ROOT_PASSWORD=outra-senha-forte

# CORS — URL do frontend no servidor (porta 9904)
CORS_ALLOWED_ORIGINS=http://SEU-IP-OU-DOMINIO:9904
```

Exemplo se o IP do servidor for `200.100.50.10`:

```env
CORS_ALLOWED_ORIGINS=http://200.100.50.10:9904
```

Se tiver domínio:

```env
CORS_ALLOWED_ORIGINS=http://meusite.uea.edu.br:9904
```

> Detalhes do e-mail: [CONFIGURACAO-EMAIL.md](CONFIGURACAO-EMAIL.md)

---

## Subir a API (backend) na porta 9905

Na pasta do projeto:

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Teste:

```bash
curl http://localhost:9905/swagger-ui.html
```

A API ficará acessível em:

```
http://SEU-SERVIDOR:9905
```

---

## Subir o frontend na porta 9904

O frontend **não está neste repositório**. No servidor, na pasta do frontend:

### Opção A — build estático + Nginx (recomendado)

```bash
# Exemplo Angular
npm install
npm run build

sudo apt install -y nginx
sudo cp -r dist/seu-projeto/* /var/www/gerenciador/
```

Configure Nginx para escutar na porta **9904** e apontar para os arquivos estáticos.

### Opção B — servidor simples (teste rápido)

```bash
npx serve -s dist/seu-projeto -l 9904
```

### Configurar URL da API no frontend

No código do frontend, a base URL da API deve ser:

```
http://SEU-SERVIDOR:9905
```

Exemplos de chamadas:

- Login: `POST http://SEU-SERVIDOR:9905/auth/login`
- Esqueci senha: `POST http://SEU-SERVIDOR:9905/auth/esqueci-senha`
- Verificar código: `POST http://SEU-SERVIDOR:9905/auth/verificar-codigo`

---

## Firewall (se necessário)

Libere as portas usadas:

```bash
sudo ufw allow 9904/tcp
sudo ufw allow 9905/tcp
sudo ufw enable
sudo ufw status
```

---

## Comandos úteis no servidor

```bash
# Ver containers rodando
docker compose -f docker-compose.prod.yml ps

# Ver logs da API
docker compose -f docker-compose.prod.yml logs -f app

# Parar tudo
docker compose -f docker-compose.prod.yml down

# Atualizar após git pull
git pull
docker compose -f docker-compose.prod.yml up -d --build
```

---

## Checklist antes de ir pro ar

- [ ] `.env` criado no servidor com `JWT_SECRET`, `MYSQL_PASSWORD` e e-mail
- [ ] `CORS_ALLOWED_ORIGINS` apontando para o frontend (`:9904`)
- [ ] API respondendo em `:9905`
- [ ] Frontend apontando para `http://servidor:9905`
- [ ] Portas 9904 e 9905 liberadas no firewall
- [ ] Senha de app do Google configurada (recuperação de senha)

---

## Desenvolvimento local vs servidor

| Ambiente | Comando | API |
|----------|---------|-----|
| Local (PC) | `docker compose up --build` | `http://localhost:8080` |
| Servidor | `docker compose -f docker-compose.prod.yml up -d --build` | `http://servidor:9905` |

---

## Estrutura final no servidor

```
Usuário (navegador)
       │
       ▼
Frontend :9904  ──HTTP──►  Backend API :9905  ──►  MySQL (interno Docker)
```

Se precisar de ajuda para configurar o Nginx do frontend na 9904, envie a pasta/stack do frontend que montamos o bloco de configuração.
