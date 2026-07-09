# AcademyTest — Android / Jetpack Compose

An Android port of the provided SwiftUI **AcademyTest** app, built with **Kotlin** and
**Jetpack Compose**. It's a small item-management app: a sorted list of items — each with
a name and a favorite state — a detail view, and add/delete, with **every change reflected
consistently across the whole app**.

This project is a faithful, feature-for-feature translation of the original SwiftUI app,
using idiomatic Android patterns rather than a mechanical line-by-line port.

## Features

- Sorted list of items (case-insensitive, locale-aware, stable insertion-order tie-break)
- Item detail view; **adaptive list/detail layout** (side-by-side on wide screens, list → detail on phones)
- Toggle favorite from **either** the list or the detail — the change is reflected in both instantly
- Add an item via a modal bottom sheet (name trimmed; save disabled while blank)
- Delete via swipe-to-dismiss in the list **or** the trash action in the detail
- Empty states for an empty list and for "no item selected"
- Italian UI strings (matching the original) and accessibility labels throughout
- `@Preview`s for every composable

## Build & run

Requirements: **JDK 17**, Android SDK with **API 35**, and either Android Studio (Ladybug or
newer) or the command line. No global Gradle install is needed — the committed Gradle wrapper
handles it.

```bash
# 1. Clone
git clone https://github.com/Dawar-hasnain/BeatCodeTest.git
cd BeatCodeTest

# 2. Point Gradle at your SDK (create local.properties)
echo "sdk.dir=/path/to/Android/Sdk" > local.properties

# 3. Build the debug APK
./gradlew assembleDebug

# 4. Run the unit tests
./gradlew testDebugUnitTest
```

Or simply open the project in Android Studio and press Run.

- **min SDK:** 26 (Android 8.0) · **compile / target SDK:** 35
- Build-tools is pinned to `35.0.1` (see "Notable decisions").

## Architecture

MVVM with a **single source of truth**, which is the key to the "changes reflected
throughout the app" requirement.

```
it.beatcode.academytest
├─ model/         Item                     immutable data class
├─ viewmodel/     ItemsListUiState         sorting + selection (pure)
│                 ItemsListViewModel       StateFlow<UiState>, all mutations
└─ ui/
   ├─ components/ FavoriteButton, ItemRow, EmptyState
   ├─ screens/    ItemsApp                 adaptive list/detail scaffold
   │              ItemsListScreen, ItemDetailScreen, AddItemSheet
   └─ theme/      Material 3 theme
```

- `ItemsListViewModel` owns the list and exposes it as a `StateFlow<ItemsListUiState>`.
- Both the list pane and the detail pane read from that **one** state; user actions (toggle,
  add, delete, select) route back through the ViewModel, which emits a new immutable state.
- Because items are immutable (`data class` + `copy()`), there's no shared mutable object to
  keep in sync — a favorite toggled anywhere updates the single list, and both panes recompose.

## Mapping from SwiftUI

The translation was deliberate; each SwiftUI construct maps to its idiomatic Compose equivalent:

| SwiftUI | Jetpack Compose |
| --- | --- |
| `@Observable` reference class | `ViewModel` + immutable `data class` (copy-on-update) |
| `NavigationSplitView` | `ListDetailPaneScaffold` (Material 3 adaptive) |
| `List` + `.onDelete` | `LazyColumn` + `SwipeToDismissBox` |
| `.sheet` + `.presentationDetents([.medium])` | `ModalBottomSheet` |
| `ContentUnavailableView` | `EmptyState` composable |
| SF Symbols (`star`, `plus`, `trash`, `tray`) | Material Icons |
| `localizedCaseInsensitiveCompare` | `java.text.Collator` (SECONDARY strength) |
| `#Preview` | `@Preview` |

## Testing

The logic layer is covered by **11 JUnit tests** (`app/src/test/...`), verifying the parts most
likely to break in translation:

- sorting (default order, case-insensitivity, creation-order tie-break)
- add (whitespace trimming, monotonic creation indices)
- favorite toggle (isolated to one item, reversible)
- delete semantics (list-delete selection fallback vs. detail-delete clearing selection)

```bash
./gradlew testDebugUnitTest
```

The original SwiftUI app had no tests; these were added as a value-add and to lock the
behavior parity in place.

## Use of AI

AI (Claude Code) was used throughout, as encouraged by the brief — for scaffolding, drafting
composables, and explaining Android/Compose concepts. Just as important is where its output was
**evaluated, corrected, or overridden**:

- **Idiomatic choices over mechanical translation** — the add form uses local composable state
  rather than a dedicated ViewModel (mirroring SwiftUI's `AddItemViewModel` would have been
  over-engineering in Compose); the shared reference-type model became an immutable
  `data class` + single source of truth.
- **Diagnosing real build issues** — the first build failed trying to auto-install
  `build-tools;34.0.0` (a broken local SDK `cmdline-tools` layout). Rather than accept the
  suggested "install via Android Studio," the fix was to pin `buildToolsVersion` to an installed
  version.
- **Fixing incorrect generated code** — e.g. a wrongly-imported `weight` modifier and a
  `ThreePaneScaffoldNavigator<String>` vs `<Any>` type mismatch, both caught at compile time.
- **Verifying, not trusting** — sorting and delete behavior were pinned down with unit tests
  rather than assumed correct from generated output.
- **Scope discipline** — deliberately kept to the requested scope (no persistence, networking,
  DI framework, or extra features), since the exercise is about approach, not feature count.

## Notable decisions

- **Adaptive layout** — `ListDetailPaneScaffold` is the honest equivalent of `NavigationSplitView`:
  it yields a two-pane layout on wide screens/tablets and list → detail navigation on phones.
- **Locale-aware sorting** — `Collator` at SECONDARY strength gives case-insensitive ordering that
  still respects accents (e.g. "Álgebra"), matching `localizedCaseInsensitiveCompare`.
- **Vector-only launcher icon** — because `minSdk` is 26, adaptive icons cover every device, so the
  launcher icon needs no rasterized PNGs.
- **`buildToolsVersion = "35.0.1"`** — pinned to a locally-installed version to avoid an auto-install
  step that fails on the dev machine's SDK layout; documented so it can be adjusted per environment.
