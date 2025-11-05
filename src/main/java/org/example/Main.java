package org.example;

import io.javalin.Javalin;

public class Main {
    // Hardcoded secret (para DAST identificar)
    private static final String SECRET_KEY = "123456";

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        // Endpoint 1: exposição de secret
        app.get("/hello", ctx -> {
            String user = ctx.queryParam("user");
            if ("admin".equals(user)) {
                ctx.result("Bem-vindo admin! Chave: " + SECRET_KEY);
            } else {
                ctx.result("Olá " + user + "!");
            }
        });

        // Endpoint 2: simulação de SQL Injection
        app.get("/getUser", ctx -> {
            String id = ctx.queryParam("id");
            // Vulnerabilidade proposital: concatenação
            String query = "SELECT * FROM users WHERE id = " + id;
            System.out.println("Executando query (demo): " + query);

            // Simula retorno baseado no input
            try {
                int userId = Integer.parseInt(id);
                String[] users = {"Alice", "Bob", "Charlie"};
                if (userId > 0 && userId <= users.length) {
                    ctx.result(users[userId - 1]);
                } else {
                    ctx.result("Usuário não encontrado");
                }
            } catch (Exception e) {
                ctx.result("Erro no input");
            }
        });

        // Endpoint 3: simulação de command injection
        app.get("/run", ctx -> {
            String cmd = ctx.queryParam("cmd");
            try {
                // Perigoso: executa input diretamente (para demonstração)
                Runtime.getRuntime().exec(cmd);
            } catch (Exception e) {
                // ignorar
            }
            ctx.result("Comando recebido: " + cmd);
        });
    }
}
