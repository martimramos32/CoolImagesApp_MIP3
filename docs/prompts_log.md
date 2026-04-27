# Prompts Log

## Prompt 1
**Goal:**
Project Setup - Inicializar a estrutura base do projeto Android com Kotlin e XML Views, e adicionar as dependências necessárias (Retrofit, Gson, Glide/Coil) para a API do Unsplash.

**Prompt used:**
"Read the /docs folder to understand the project. Then, execute ONLY Step 1 from docs/08_implementation_plan.md. Do not generate code for the other steps yet."

**Result:**
A IA criou o projeto base(incluindo todas os packages necessários) com sucesso, adicionou as dependências de rede e imagem no build.gradle e configurou o AndroidManifest.xml com as permissões de Internet. Primeiramente tentou construir a app usando a versão 8.2.1 do Gradle, mas depois reparar que esta não era compatível com a minha versão do Java (JDK25), o agente por si mesmo corrigiu o problema e mudou automaticamente para uma versão do Gradle mais recente (9.2.1) compatível com a minha versão do Java.

## Prompt 2
**Goal:**
Data Model - Criar as classes de dados em Kotlin (UnsplashImage, ImageUrls, User) que vão receber e organizar a informação do ficheiro JSON que a API do Unsplash nos vai enviar.

**Prompt used:**
"Read `docs/04_data_model.md` and `docs/07_api_usage.md` to understand the required data structure. Then, execute ONLY Step 2 from `docs/08_implementation_plan.md`. Create the Kotlin data classes needed to parse the Unsplash API JSON response. Generate only the code for these data classes and do not proceed to Step 3."

**Result:**
A IA criou o ficheiro UnsplashImage.kt com as classes de dados corretas para o ID, os URLs da imagem, descrição da imagem e dados do utilizador.

## Prompt 3
**Goal:**
API Service Setup com Segurança - Criar a ligação à internet com o Retrofit, configurando a aplicação para ler a chave de acesso a partir do ficheiro seguro `local.properties` usando a ferramenta interna `BuildConfig`.

**Prompt used:**
"Read `docs/07_api_usage.md` and the data classes created in Step 2. Then, execute ONLY Step 3 from `docs/08_implementation_plan.md`. Create the Retrofit API interface to fetch random images. IMPORTANT SECURITY REQUIREMENT: Do not hardcode the API key. Configure the `app/build.gradle` file to read a property named `UNSPLASH_ACCESS_KEY` from the `local.properties` file and expose it via a `BuildConfig` field. Then, use this `BuildConfig` variable in the Retrofit interface for the Authorization header. Generate only the code for this step and do not proceed to Step 4."

**Result:**
A IA configurou o ficheiro build.gradle para ler a chave e criou a interface do Retrofit utilizando o BuildConfig, mantendo a chave secreta fora do código fonte para manter a segurança da nossa aplicação.

## Prompt 4
**Goal:**
Repository e ViewModel - Implementar a arquitetura MVVM criando um Repositório para gerir as chamadas à API e um ViewModel com LiveData para armazenar o estado de carregamento e a lista de imagens.

**Prompt used:**
"Review the code generated in Steps 2 and 3. Then, execute ONLY Step 4 from `docs/08_implementation_plan.md`. Create the `ImageRepository` class to handle data fetching from the API. Then, create the `MainViewModel` class. The ViewModel must use `LiveData` to expose two things to the UI: a List of `UnsplashImage` objects, and a `Boolean` representing the loading state (true when fetching, false when done). Generate only the code for this step and do not proceed to Step 5."

**Result:**
A IA criou o ImageRepository para fazer o pedido ao Unsplash, o MainViewModel com LiveData para guardar a lista de fotografias e o estado da barra de carregamento e o RetrofitClient para configurar o Retrofit, conectando o Gson e o OkHttp(logging interceptor que é bastante útil para fazer debug de erros da API, caso existam).

## Prompt 5
**Goal:**
UI Layout Design - Desenhar a interface visual em XML. Criar o ecrã principal com a lista e o carregamento, e criar o design individual que cada fotografia vai ter dentro da lista.

**Prompt used:**
"Review the project structure. Then, execute ONLY Step 5 from `docs/08_implementation_plan.md`. Create the XML layout for `activity_main.xml` including a `RecyclerView`, a `ProgressBar` for loading, and a `SwipeRefreshLayout` for the refresh action. Also, create a separate XML layout file (e.g., `item_image.xml`) for the individual items in the RecyclerView, which should contain an `ImageView` for the photo and a `TextView` for the author's name. Strictly use standard Android XML Views. DO NOT use Jetpack Compose. Generate only the XML code for this step and do not proceed to Step 6."

**Result:**
A IA gerou o activity_main.xml com o SwipeRefreshLayout e o RecyclerView, e também criou o item_image.xml para definir a aparência individual de cada fotografia na lista.

## Prompt 6
**Goal:**
RecyclerView Adapter - Criar a classe do adaptador (Adapter) que faz a ponte entre a lista de dados (fotografias) e o ficheiro visual individual (`item_image.xml`).

**Prompt used:**
"Review the layouts created in Step 5 and the data model from Step 2. Then, execute ONLY Step 6 from `docs/08_implementation_plan.md`. Create the `RecyclerView.Adapter` (e.g., `ImageAdapter`) to bind the list of `UnsplashImage` objects to the `item_image.xml` layout. Use the image loading library specified in Step 1 (Glide or Coil) to load the `regular` URL into the `ImageView`, and set the `user.name` in the `TextView`. Generate only the code for this step and do not proceed to Step 7."

**Result:**
A IA gerou a classe ImageAdapter, configurando o ViewHolder para associar o nome do autor ao TextView e usando a biblioteca de imagens para carregar o URL para o ImageView.

## Prompt 7
**Goal:**
Connect UI to Logic - Ligar a interface visual à lógica no `MainActivity`, configurando a lista de imagens, o ecrã de carregamento e a ação de atualizar a página.

**Prompt used:**
"Review the code generated in Steps 4, 5, and 6. Then, execute ONLY Step 7 from `docs/08_implementation_plan.md`. In `MainActivity.kt`, initialize the `RecyclerView` with the adapter, observe the `LiveData` from the `MainViewModel` to update the list of images, and set up the refresh action (e.g., `SwipeRefreshLayout`) to fetch new images. Update the UI loading indicator based on the ViewModel's loading state. Generate only the code for this step. This is the final step of the base implementation."

**Result:**
A IA atualizou o MainActivity, ligando a lista visual ao adaptador e configurando a aplicação para reagir aos dados que chegam da internet.

## Prompt 8
**Goal:**
Ecrã de Detalhes (Extension 1) - Criar um novo ecrã que se abre ao clicar numa fotografia da lista, passando os dados necessários para mostrar a imagem em ponto grande e as respetivas informações.

**Prompt used:**
"I have added a new file `docs/09_features_extension.md` with the plan for Extension 1 (Image Details Screen). Please read this new file. Then, execute Steps 8, 9, and 10 to implement this feature. Create the new Activity and its XML layout, and update the existing adapter to handle the click event and pass the data via Intent. Generate only the code for these three steps."

**Result:**
A IA criou a ImageDetailActivity e o seu layout, e adicionou o clique no ImageAdapter para abrir o novo ecrã com a foto em grande com as devidas informações(autor, descrição caso haja e o id da imagem.).

## Prompt 9
**Goal:**
Sistema de Favoritos (Extension 2) - Criar a lógica para guardar até 5 imagens favoritas no armazenamento local e permitir que o utilizador as visualize num novo ecrã.

**Prompt used:**
"I have updated `docs/09_features_extension.md` with the plan for Extension 2 (Favorites System). Please read the updated file. Execute Steps 11, 12, 13, and 14. Implement a simple local storage to save up to 5 favorite images. Add a heart button to the layouts to toggle favorites and create a new Activity to display the favorites list. Ensure the UI updates correctly when an item is added or removed. Generate the code for these steps."

**Result:**
A IA implementou o armazenamento local, adicionou os botões de favorito nos ecrãs e criou a atividade para listar os favoritos com sucesso num ecrã à parte.

## Prompt 10
**Goal:**
Cache Avançado e Paginação (Extension 3) - Implementar a regra de manter 50 itens em cache, carregar 20 no início e pré-carregar mais quando faltarem 5 itens para o fim.

**Prompt used:**
"I have updated `docs/09_features_extension.md` with the corrected plan for Extension 3. Please read the updated file. Execute Steps 15, 16, and 17. Ensure the initial fetch is 20 images to prevent immediate pagination triggers. The scroll listener should only trigger a fetch for 10 more images when the user is 5 items away from the bottom. Enforce the 50-item cache limit. Add the relative loading indicator at the bottom. Generate the code."

**Result:**
A IA implementou a paginação na lista, garantindo o limite de 50 itens em cache e adicionou a barra de carregamento no fundo ao fazer scroll para baixo. A IA também implementou o sistema de cache, garantindo que a aplicação mantém sempre 50 itens em cache e remove os itens não favoritos quando o limite é atingido. Guarda também em cache as imagens favoritas.

## Prompt 11
**Goal:**
Polimento Visual (UI/UX) - Melhorar o aspeto da aplicação com base no Material Design, adicionando cantos arredondados, sombras, melhores margens e tipografia.

**Prompt used:**
"I have added a new file `docs/10_ui_polish.md` detailing the final visual improvements. Please read this file. Execute Steps 18, 19, and 20. Modify the XML layout files to use `MaterialCardView` with rounded corners, elevation, and margins. Improve the typography and colors. CRITICAL INSTRUCTION: You must strictly ONLY modify the UI files (XMLs) and potentially minor Adapter color state logic. Do NOT alter any of the caching, pagination, or network logic we built in the previous steps. Generate the UI code."

**Result:**
A IA atualizou os ficheiros XML com Material Cards, sombras e margens, deixando a aplicação com um aspeto muito mais polido e profissional, sem quebrar toda a lógica realizada.