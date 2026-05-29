# Manga Store - Projeto Didático Android

Este repositório contém uma aplicação Android moderna desenvolvida para fins educacionais. O objetivo é demonstrar as melhores práticas atuais no ecossistema Android, utilizando **Jetpack Compose**, **Ktor**, **Coroutines** e a nova biblioteca **Navigation 3**.

---

## 🚀 Boas Práticas Destacadas

### 1. Gerenciamento de Estado com Unidirectional Data Flow (UDF)

Utilizamos o padrão **UDF** onde o estado flui do ViewModel para a UI, e eventos fluem da UI para o ViewModel. Para representar o estado, utilizamos uma `sealed interface`.

```kotlin
sealed interface MangaListUiState {
    data object Loading : MangaListUiState
    data class Success(val mangas: List<Manga> = emptyList()) : MangaListUiState
    data class Error(val message: String) : MangaListUiState
}
```

**Por que é uma boa prática?**
*   **Segurança de Tipos:** Ao usar `sealed interface`, garantimos que a UI trate explicitamente todos os estados possíveis (Carregando, Sucesso ou Erro), evitando telas em branco ou comportamentos inconsistentes.
*   **Imutabilidade:** O estado é imutável, o que facilita o rastreio de bugs e garante que apenas o ViewModel possa disparar mudanças.

---

### 2. Consumo de API Assíncrono com Ktor e Coroutines

O projeto utiliza o **Ktor** como cliente HTTP pela sua natureza moderna, multiplataforma e baseada em Coroutines.

```kotlin
fun loadMangas() {
    viewModelScope.launch {
        _uiState.value = MangaListUiState.Loading
        try {
            val collection = repository.get()
            _uiState.value = MangaListUiState.Success(mangas = collection.data)
        } catch (e: Exception) {
            _uiState.value = MangaListUiState.Error("Erro ao carregar: ${e.message}")
        }
    }
}
```

**Decisões e Benefícios:**
*   **`viewModelScope`:** As chamadas de rede são vinculadas ao ciclo de vida do ViewModel. Se o usuário sair da tela, a Coroutine é cancelada automaticamente, evitando vazamentos de memória e desperdício de dados.
*   **Resiliência:** O uso de `try-catch` permite que a aplicação se recupere de falhas de rede (como modo avião) oferecendo ao usuário uma opção de "Tentar Novamente".

---

### 3. Navegação Adaptativa com Navigation 3

Este projeto utiliza a **Navigation 3**, a mais nova evolução da biblioteca de navegação do Android, que simplifica a criação de layouts adaptativos (Lista-Detalhe) para diferentes tamanhos de tela (Tablets, Dobráveis e Celulares).

```kotlin
val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

NavDisplay(
    backStack = backStack,
    sceneStrategies = listOf(listDetailStrategy),
    entryProvider = entryProvider {
        entry<MangaListKey> (metadata = ListDetailSceneStrategy.listPane(...)) {
            MangaList(navigateTo = { backStack.add(MangaDetail(it)) })
        }
        entry<MangaDetail>(metadata = ListDetailSceneStrategy.detailPane()) {
            MangaDetailScreen(id = it.id)
        }
    }
)
```

**Por que utilizar?**
*   **Multi-painel Nativo:** Com o `ListDetailSceneStrategy`, a aplicação exibe automaticamente a lista e o detalhe lado a lado em telas grandes, e em telas separadas em celulares.
*   **Navegação Baseada em Estado:** A navegação é controlada por uma lista simples (`backstack`), tornando-a muito mais previsível e integrada ao estado do Compose.

---

### 4. UI Declarativa e Moderna com Material Design 3

A interface segue os princípios do **Material Design 3**, utilizando componentes que se adaptam ao tema e ao conteúdo.

*   **LazyVerticalGrid:** Utilizado para criar um catálogo visualmente rico que se ajusta ao número de colunas conforme o espaço disponível.
*   **Scaffold:** Fornece a estrutura padrão de slots para barras superiores e conteúdo, garantindo que o espaçamento do sistema (Safe Areas) seja respeitado.

```kotlin
LazyVerticalGrid(
    columns = GridCells.Adaptive(minSize = 160.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    // ...
)
```

---

## 🛠️ Tecnologias Utilizadas

*   **Jetpack Compose:** Framework moderno para UI declarativa.
*   **Ktor:** Cliente HTTP assíncrono.
*   **Navigation 3:** Gerenciamento de navegação e layouts adaptativos.
*   **Coil 3:** Carregamento de imagens de forma eficiente.
*   **Kotlinx Serialization:** Serialização de JSON para objetos Kotlin de forma Type-Safe.

---

Este projeto é mantido para fins de ensino. Sinta-se à vontade para explorar o código e aplicar estes conceitos em suas próprias aplicações!
