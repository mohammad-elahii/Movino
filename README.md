# Movino

Movie browsing app for Android. Kotlin, Jetpack Compose, Ktor, MVVM. Data comes from the public [moviesapi.ir](https://moviesapi.ir) REST API.

<p>
  <img src="app/Movino%20intro/movino%201.png" width="100%" />
</p>
<p>
  <img src="app/Movino%20intro/movino%202.png" width="100%" />
</p>
<p>
  <img src="app/Movino%20intro/movino%203.png" width="100%" />
</p>
<p>
  <img src="app/Movino%20intro/movino%204.png" width="100%" />
</p>

## Architecture

Single-activity app, all UI in Jetpack Compose (Material 3), structured as MVVM:

- `navigation/` - type-safe routing with a `@Serializable sealed class AppRoute` (`Splash`, `Home`, `Search`, `Detail(movieId)`) over Navigation-Compose `NavHost`. The splash destination is popped inclusively after navigating home.
- `homeScreen/`, `searchScreen/`, `detailScreen/` - one ViewModel per screen. Each screen has a sealed UI state (`Loading` / `Success` / `Error`, plus `Idle`/`Empty` for search) rendered through `when` blocks.
- ViewModels expose state via `StateFlow` (private `MutableStateFlow` backing a public read-only flow) and one-shot errors via `SharedFlow` events, surfaced as snackbars with a retry action.
- `ui/components/` - shared composables: top app bar, movie card, genre filter row, snackbar host, loading indicators.
- `api/` - Ktor client, request definitions, and `@Serializable` models.

## Networking (Ktor)

Ktor client 3.3.3 with the OkHttp engine, `ContentNegotiation` (kotlinx.serialization, `ignoreUnknownKeys = true`) and `Logging` plugins. Base URL is set once via `defaultRequest`:

```kotlin
defaultRequest { url("https://moviesapi.ir/api/v1/") }
```

| Endpoint | Purpose |
|---|---|
| `GET movies?page={n}&q={query}` | Paged movie list, optional search query |
| `GET movies/{id}` | Full movie detail |
| `GET genres` | Genre list for filtering |

Pagination is metadata-driven (`current_page` / `page_count` from the response). Errors are caught at the ViewModel level and mapped to `Error` states and snackbar events; no retry happens silently.

## Lifecycle usage

- `collectAsStateWithLifecycle()` collects all screen state flows, so collection stops when the UI is not started.
- `LaunchedEffect` collects error events and shows retry snackbars (home, detail), triggers search pagination when the list hits the bottom, and drives the 2-second splash delay.
- `snapshotFlow` on the home list's `LazyListState` layout info watches the last visible index and fires `loadMoreMovies()` when the user approaches the end of the list.
- Search queries flow through `debounce(500)` with `collectLatest` in the ViewModel to avoid a request per keystroke.
- All async work runs in `viewModelScope` from `init` blocks; there is no coroutine leakage into composables.

## Git structure and teamwork

Built with [ArminHozhabri](https://github.com/ArminHozhabri). 101 commits on [`github.com/mohammad-elahii/Movino`](https://github.com/mohammad-elahii/Movino):

- Mohammad Elahi: 60 commits (navigation, detail screen, merges, polish)
- Armin Hozhabri: 40 commits (home, search, global components, error handling)

Workflow: individual feature branches (`mmd_branch`, `armin_branch`) merged into `develop`, then into `main` through merge commits. Commits reference tracked issues (`implement-navigation_(#29)` ... `adding-documentation(#41)`).
