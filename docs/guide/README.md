# si-avg-price

![tech](https://badgen.net/badge/lang/Kotlin/cyan)
![tech](https://badgen.net/badge/web/Spring%20Webflux/green)
![tech](https://badgen.net/badge/DB/MySQL)

![tech](https://badgen.net/badge/Java/17/yellow)
![tech](https://badgen.net/badge/Gradle/7.5/yellow)

Esta aplicação é responsável por calcular o preço médio de um produto.

---
### Ambiente de desenvolvimento
    - docker-compose
    - jdk 17+


* MySQL
    - Criar container do MySQL:
        ```shell
        docker-compose up
        docker-compose up -d ->> para rodar em segundo plano
        ```

## Configurar Intellij ##

Configure o Run ```bootRun```
Click no botão Edit Configurations ao lado do play.

Em run/debug configurations click no + e escolha Gradle.

Preencha os campos com as informações abaixo:

| Campo    		            | Valor							                |
|------------------------|-----------------------------|
| Grade project:			      | `si-avg-price`				          |
| Task:	 	     		        | `app:bootRun`					          |
| Environment variables: | `APPLICATION=si-avg-price`	 |
