# Galeria Unsplash

Esta aplicação é o resultado da evolução do projeto inicial desenvolvido no Tutorial 2. O objetivo principal desta fase (MIP-3) foi transformar uma aplicação monolítica numa arquitetura modular limpa, capaz de suportar duas interfaces visuais completamente diferentes partilhando a mesma base de dados e lógica de negócio.

---

## 1. O Plano de Refatoração (Refactoring Plan)

No início, toda a lógica da aplicação estava misturada nos ecrãs. Para garantir a separação de responsabilidades e tornar o código reutilizável, aplicámos o seguinte processo de refatoração:

1. **Criação do Módulo `:core`:** Extraímos o "motor" da aplicação para um módulo central e independente. Movemos para aqui os modelos de dados, as ligações à API do Unsplash (Ktor/Retrofit) e o `Repository`. Toda a lógica de armazenamento local (cache de 50 itens e sistema de favoritos) vive agora exclusivamente aqui.
2. **Limpeza da App Legada (`:app-xml`):** A aplicação original foi renomeada para `:app-xml`. Limpámos a sua lógica pesada, configurando-a para ser apenas uma "montra" que consome os dados fornecidos pelo módulo `:core`.
3. **Criação da App Moderna (`:app-compose`):** Desenvolvemos uma aplicação totalmente nova baseada em Jetpack Compose, que consome o exato mesmo módulo `:core`, provando que a lógica de negócio está perfeitamente isolada da interface visual.

---

## 2. Contrato de Interface (UI Contract)

As duas aplicações visuais (`app-xml` e `app-compose`) comunicam com o módulo central (`core`) através de um contrato bem definido, usando o padrão de arquitetura **MVVM (Model-View-ViewModel)** com um Fluxo Unidirecional de Dados.

* **Isolamento:** O módulo `:core` (Model) não sabe nada sobre ecrãs, botões ou componentes visuais. Apenas fornece dados através do `Repository`.
* **A Ponte (ViewModel):** Cada aplicação possui o seu próprio `ViewModel` na camada de interface. O `ViewModel` pede as informações ao `Repository` do módulo `:core` e transforma-as num estado (ex: `StateFlow` ou `LiveData`).
* **Reatividade:** A interface visual (View) apenas tem o trabalho de observar o estado exposto pelo `ViewModel`. Se um utilizador adicionar uma imagem aos favoritos, a View avisa o `ViewModel`, que fala com o `:core`. O estado é atualizado e a interface desenha-se a si própria de novo, automaticamente.

---

## 3. Especificações e Evolução do Projeto (Markdown Documentation)

O desenvolvimento deste projeto seguiu rigorosamente uma abordagem "Planeamento Primeiro" (*Planning-First Approach*). Antes de qualquer código ser gerado pelas ferramentas de Inteligência Artificial, as decisões foram planeadas e registadas na pasta `docs/`.

A evolução do projeto pode ser acompanhada através dos seguintes documentos principais:
* **`docs/06_architecture.md`**: Explica a separação do projeto em três módulos e contém o diagrama da arquitetura implementada.
* **`docs/11_refactor_plan.md`**: Detalha os passos exatos de extração da lógica antiga para o novo módulo `:core`.
* **`docs/12_compose_app.md`**: Especifica o plano de construção da nova aplicação declarativa e define a Funcionalidade Exclusiva (Animações e Cores Dinâmicas com base na paleta da fotografia).
* **`docs/prompts_log.md`**: Um diário de bordo com o registo transparente de todos os comandos enviados ao agente AntiGravity.

---

## 4. Funcionalidade Exclusiva do Jetpack Compose

Para demonstrar o poder das interfaces declarativas, a versão `:app-compose` inclui funcionalidades modernas que não existem na versão XML original:
* **Animações Fluidas:** Expansão suave das fotografias para o modo de detalhe a ecrã inteiro.
* **Temas Dinâmicos:** Integração com a Palette API para extrair as cores principais da imagem selecionada, ajustando o fundo e os elementos da aplicação para combinar com a "vibe" da fotografia.