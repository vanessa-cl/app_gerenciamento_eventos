# App de Gerenciamento de Eventos

Este é um projeto de aplicativo de desktop para gerenciamento de eventos, desenvolvido em Kotlin com JavaFX. O aplicativo foi desenvolvido para a disciplina Estrutura de Dados do curso de Sistemas de Informação do IFBA campus Vitória da Conquista.

## Como Executar o Projeto Localmente

### Pré-requisitos
 - **Java Development Kit (JDK):** Certifique-se de ter o JDK 8 ou superior instalado.
 - **Maven:** Certifique-se de ter o Apache Maven instalado.
 - **Git:** Certifique-se de ter o Git instalado.
 - **IntelliJ IDEA:** Recomendado para um melhor suporte ao Kotlin e JavaFX.
 
### Passos
 - Abra o terminal e execute o seguinte comando para clonar o repositório:  
 `git clone git@github.com:vanessa-cl/app_gerenciamento_eventos.git`
 - Abra o IntelliJ IDEA.
 - Selecione File -> Open e navegue até o diretório onde você clonou o repositório.
 - Selecione a pasta app_gerenciamento_eventos e clique em OK.
 - No IntelliJ IDEA, vá até a janela Maven (geralmente localizada na barra lateral direita).
 Clique no botão Reload All Maven Projects para garantir que todas as dependências sejam baixadas e configuradas corretamente.
 - Navegue até o arquivo src/main/kotlin/ui/MainApp.kt.
 - Clique com o botão direito no arquivo e selecione Run 'MainApp'.
 - Uma janela do JavaFX deve abrir com o título "App de Gerenciamento de Eventos" e um botão "Clique aqui".
 
### Problemas Comuns
 - **Erro de Configuração do JavaFX:** Certifique-se de que o JavaFX está corretamente configurado no pom.xml e que o caminho do módulo está correto.
 - **Dependências do Maven:** Se houver problemas com dependências, tente executar `mvn clean install` no terminal dentro do diretório do projeto.