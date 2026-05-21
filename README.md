# Manga Store - Projeto Didático Android

Este repositório contém uma aplicação Android moderna desenvolvida para fins educacionais. O objetivo é demonstrar as melhores práticas atuais no ecossistema Android, utilizando Jetpack Compose, Ktor, Coroutines e a nova biblioteca Navigation 3.

## 🚀 Boas Práticas Destacadas

### 1. Gerenciamento de Estado com Unidirectional Data Flow (UDF)

Utilizamos uma `sealed interface` para representar o estado da UI. Isso garante que a View (Compose) seja reativa e trate explicitamente todos os estados possíveis da aplicação (Carregando, Sucesso e Erro).

```kotlin
sealed interface MangaUiState {
    data object Loading : MangaUiState
    data class Success(
        val mangas: List<Manga> = emptyList(),
        val selected: Manga? = null
    ) : MangaUiState
    data class Error(val message: String) : MangaUiState
}
```

**Por que é uma boa prática?**
*   **Segurança de Tipos:** Evita estados inconsistentes (ex: exibir uma lista enquanto uma mensagem de erro está ativa).
*   **Previsibilidade:** A UI apenas reage ao estado emitido pelo ViewModel, facilitando o debug e testes.

---

### 2. Consumo de API Assíncrono com Ktor e Coroutines

O Ktor é um cliente HTTP multiplataforma e moderno. Combinado com Coroutines, permite realizar chamadas de rede sem bloquear a thread principal (UI thread), garantindo uma experiência fluida.

```kotlin
fun loadMangas() {
    viewModelScope.launch {
        _uiState.value = MangaUiState.Loading
        try {
            val collection = repository.get()
            _uiState.value = MangaUiState.Success(mangas = collection.data)
        } catch (e: Exception) {
            _uiState.value = MangaUiState.Error("Erro ao carregar mangas: ${e.message}")
        }
    }
}
```

**Decisões e Benefícios:**
*   **`viewModelScope`:** Gerencia automaticamente o ciclo de vida da Coroutine. Se o ViewModel for destruído, a chamada de rede é cancelada, evitando vazamentos de memória.
*   **Tratamento de Exceções:** O uso de `try-catch` garante que falhas de rede não causem crash no app, permitindo exibir um feedback adequado ao usuário.

---

### 3. Navegação Moderna com Navigation 3

Este projeto utiliza a **Navigation 3**, a evolução da biblioteca de navegação do Android, focada em ser baseada em estado e totalmente compatível com Compose.

```kotlin
val backStack = rememberNavBackStack(MangaListKey)

NavDisplay(
    backStack = backStack,
    entryProvider = entryProvider {
        entry<MangaListKey> {
            MangaList(navigateTo = { backStack.add(MangaDetail(it)) })
        }
        entry<MangaDetail> {
            MangaDetailScreen(id = it.id, onBack = { backStack.removeLast() })
        }
    }
)
```

**Por que utilizar?**
*   **Baseada em Estado:** A navegação é controlada por uma lista (`backstack`), tornando-a muito mais intuitiva para quem já trabalha com o estado do Compose.
*   **Type Safety:** Utiliza objetos (via Kotlin Serialization) para passar parâmetros entre telas, eliminando erros de digitação comuns em rotas baseadas em String.

---

### 4. UI Declarativa com Material Design 3

A interface utiliza componentes do **Material 3** para garantir um visual moderno e acessível.

*   **Scaffold:** Utilizado para fornecer a estrutura básica (TopBar, Content Padding).
*   **TopAppBar:** Implementada para navegação consistente e títulos de página.
*   **LazyVerticalGrid:** Utilizada para exibir o catálogo de mangas de forma adaptativa.

```kotlin
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(manga?.title ?: "Detalhes") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
            }
        )
    }
) { innerPadding ->
    // Conteúdo aqui respeitando o innerPadding
}
```

---

## 🛠️ Tecnologias Utilizadas

*   **Jetpack Compose:** UI declarativa.
*   **Ktor:** Cliente HTTP.
*   **Kotlinx Serialization:** Conversão de JSON para objetos Kotlin.
*   **Coil 3:** Carregamento de imagens assíncrono.
*   **Navigation 3:** Gerenciamento de telas.
*   **ViewModel & Flow:** Arquitetura e reatividade.

---

Este projeto é mantido para fins de ensino na disciplina de desenvolvimento Android. Fique à vontade para explorar o código e sugerir melhorias!
