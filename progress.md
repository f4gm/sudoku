# progress.md — Avances del proyecto Sudoku 6×6

> Registro de **lo que ya está hecho** y **lo que falta**.
> Actualizar este archivo al terminar cada paso. El plan completo está en `agents.md`.

**Última actualización:** 2026-11-06

---

## 📊 Estado general

| Fase | Descripción | Estado |
|------|-------------|--------|
| Fase 0 | Que compile y arranque (`mvn javafx:run`) | ✅ Completada |
| Fase 1 | Modelo: `SudokuBoard`, `SudokuGenerator`, `GameState` | ✅ Completada (Javadoc cerrado) |
| Fase 2 | Vista: FXML + CSS + celdas | 🔄 En curso |
| Fase 3 | Controlador: mouse, teclado, validación, ayuda | 🔄 Eventos conectados · falta ayuda con contador y victoria |
| Fase 4 | Pantalla de victoria (GameOver simplificada) | ⬜ No iniciada |
| Fase 5 | Verificación de criterios de aceptación | ⬜ No iniciada |
| Fase 6 | Git/GitHub: ≥2 PRs y ≥2 commits por estudiante | 🔄 Commits por integrante OK · falta verificar autoría de PRs |

**Leyenda:** ⬜ pendiente · 🔄 en curso · ✅ completada · ⚠️ con problemas

---

## ✅ Hecho

- [x] **2026-11-03** — Repo clonado y verificado: `origin` → `https://github.com/f4gm/sudoku.git`, rama `master` sincronizada con `origin/master`.
- [x] **2026-11-03** — Leído el enunciado PDF completo (HU-1 a HU-5, objetivos, condiciones).
- [x] **2026-11-03** — Inventariado el código existente y detectados los problemas de compilación.
- [x] **2026-11-03** — Creado `agents.md` (plan + guía de interacción OpenCode) y `progress.md`.
- [x] **2026-11-03** — **Instalado JDK 24** y configurado `JAVA_HOME` + `PATH`. `mvnw -v` → Maven 3.8.5 + Java 24.0.2.
- [x] **2026-11-03** — **Fase 0 completada:** corregido `module-info.java` (quitado `service.timer`) y `mainClass` en `pom.xml` (`HelloApplication` → `Main`). Verificado: `mvnw clean javafx:run` abre la ventana de bienvenida.
- [x] **2026-11-03** — **`SudokuBoard` implementado**: campos `values`/`isClue`, constructor, `getValue`, `isClue`, `setValue`, `setInitial` (package-private), `isValidPlacement` (fila, columna y bloque 2×3) y `clear`. Javadoc completo.
- [x] **2026-11-03** — **PRs #5 y #6 mergeados** en `master` (rama `model/sudoku-board`). Commits `ab32990`, `f9da1b4`, `d399da2`, entre otros.
- [x] **2026-11-03** — `.gitignore`: agregados el PDF del enunciado y `training.md`.
- [x] **2026-11-04** — **Revisión de integración modelo ↔ vista ↔ controlador.** Hallazgo principal: `SudokuBoard`/`SudokuGenerator` no son referenciados por ningún controlador; `GameState` está vacío y `GameController` solo lo importa sin usarlo. El tablero se muestra vacío y `onHelp()` no hace nada.
- [x] **2026-11-04** — **Verificación empírica de la generación:** con un programa de prueba sobre 1000 tableros, **990/1000 no tenían 2 pistas por bloque 2×3** (HU-2), aunque siempre sumaban 12 pistas.
- [x] **2026-11-04** — **Generador corregido:** `removeCells` ahora conserva exactamente 2 celdas por bloque (vacía las otras 4) y `SudokuBoard.SIZE` es público. Verificado: **1000/1000 tableros con reparto `2,2,2,2,2,2`**, 12 pistas y `mvnw compile` OK con JDK 24.
- [x] **2026-11-04** — **`SudokuBoard` completo:** `getConflicts`, `isComplete` e `isValid`; `isValidPlacement` ahora delega en `getConflicts` (reglas en un solo lugar). Verificado con **23 escenarios** (fila, columna, bloque, duplicados, celda propia, valores 0/fuera de rango, tablero completo válido e inválido). Javadoc repuesto.
- [x] **2026-11-04** — **`GameState` implementado:** fachada con `newGame`, `play`, `clearCell`, `giveHint`, `isWon` (calculado) y contador de 3 ayudas. Verificado con **37 escenarios**: pistas protegidas, conflictos permitidos, borrado, 3 ayudas + cuarta nula sin consumir, victoria al resolver y reinicio de ayudas. `mvnw compile` OK.
- [x] **2026-11-04** — **Documentación actualizada:** `progress.md` (este archivo) y `training.md` (lecciones L12–L18 sobre fachada `GameState`, dirección de dependencias, regresión de HU-2, API defensiva, riesgo de código IA, bug latente de GameOver y entorno JDK 17 vs. 24).
- [x] **2026-11-06** — **Sincronización con `master`** (15 commits, fast-forward). Se abandonó la rama `model/sudoku-board` (ya mergeada en el PR #6) y se pasa a trabajar sobre `master`.
- [x] **2026-11-06** — **`master` no compilaba**: `WelcomeController.onStart()` llamaba a `startNewGame()`, un método inexistente en esa clase. Corregido moviendo el arranque a `GameController.initialize()` (el que posee `board`, `generator` y `cellFields`). Efecto colateral bueno: ahora **cancelar el alert ya no navega**.
- [x] **2026-11-06** — **Bug del tablero vacío corregido**: `startNewGame()` nunca se invocaba. Ahora `initialize()` llama a `buildgridUI()` → `startNewGame()`.
- [x] **2026-11-06** — **Fase 3 (eventos) conectada en `GameController`:**
  - **Mouse**: `setOnMouseClicked` → `selectCell()` (selección con borde azul, `requestFocus()`, pistas no seleccionables).
  - **Teclado**: `SudokuBoardAdapter.attachCellListeners(...)` — la clase **existía y nadie la llamaba**.
  - **Validación**: `handleCellUpdate()` ahora sí está conectado vía `this::handleCellUpdate`.
  - **Refactor de pintado**: un solo origen de verdad (`repaintAll()` + `resetCellBorder()` deciden por *estado*: pista / conflicto / selección / normal) en vez de `setStyle` disperso en 4 métodos.
  - **Guardia de reentrancia** `syncingUI`: evita que el listener interprete como tecla lo que el propio controlador escribió al generar el puzzle.
- [x] **2026-11-06** — **Prueba en vivo de la app:** ✅ tablero arranca con 12 pistas · ✅ se puede escribir · ✅ resaltado en rojo con mensaje · ✅ cero excepciones en el log.

### 📍 Punto exacto para retomar (próxima sesión)

1. **Fase B (vista):** `style.css` con clases reales (`.celda-error`, `.celda-ayuda`, `.celda-seleccionada`) **y migrar el controlador de `setStyle` inline a esas clases**; `Game.fxml` con botón "Nuevo juego" + contador "Ayuda (3)"; `Welcome.fxml` sin "Intrucciones TODO".
2. **Fase C:** ayuda con contador real (`GameState.giveHint()`), victoria vía `showGameOver()`, y usar `EnterKeyAdapter` en alguna escena.
3. **Fase D:** arreglar `GameOver.fxml` (`#onStart` → `#restartGame`) + `fx:id` faltantes; simplificar `setSummary`; mover `AlertHelper`/`SudokuBoardAdapter` fuera de `model/` (usan JavaFX) y darles Javadoc.

### ⚠️ Incidente de integración (2026-11-04)

- Un integrante pusheó a `model/sudoku-board` el commit `ab32990` con **su propio `SudokuBoard`**
  y un `SudokuGenerator` escrito contra **su API**.
- El merge en el remoto (`90729da`) resolvió `SudokuBoard` a **la versión de Santiago**, pero conservó
  el `SudokuGenerator` del integrante → **7 errores de compilación**.
- **`origin/master` está roto**: su `SudokuBoard` tiene **0** apariciones de `SUBGRID` y su
  `SudokuGenerator` **4**. Conviene avisarlo al equipo.
- **Decisión:** completar el `SudokuBoard` de Santiago (agregar `SUBGRID_ROWS/COLS`, `clear()`,
  renombrar `isValid`→`isValidPlacement` y `setClue`→`setInitial`) sin tocar el archivo del integrante.
- **Commit `c23d3a7`** — docs del punto de reanudación. Sync hecho: `pull` + `merge origin/master`,
  ambos sin conflictos. **Push pendiente hasta que compile.**
- Pendiente también: `.idea/misc.xml` apunta a `temurin-27` y el `pom.xml` a Java 24 (unificar antes
  de la sustentación).

### Decisiones tomadas (acordadas con el desarrollador)

1. **GameOver se mantiene** como pantalla de **victoria** al resolver el tablero.
2. **Ayuda limitada:** 3 usos + 1 sola celda rellenada por clic (cumple "no terminar el tablero con ayuda").
3. **Sin tests JUnit** por ahora: solo la aplicación.

### Problemas detectados

| # | Archivo | Problema | Impacto | Estado |
|---|---------|----------|---------|--------|
| 1 | `module-info.java` | `exports/opens com.example.sudoku.service.timer` — el paquete no existe | ❌ No compilaba | ✅ Corregido 2026-11-03 |
| 2 | `pom.xml` | `mainClass` = `...HelloApplication` (clase inexistente) | ❌ `javafx:run` fallaba | ✅ Corregido 2026-11-03 |
| 3 | `GameOver.fxml` | `onAction="#onStart"` pero el método es `restartGame()`; faltan `fx:id` de `feedbackLabel`, `recordLabel`, `remainingTimeLabel` | ❌ La carga falla y `setSummary` daría NPE | ⬜ Pendiente (Fase 4) |
| 4 | `Welcome.fxml`, `GameOver.fxml` | Texto "Intrucciones TODO" y acentos a revisar | ⚠️ Visual | ⬜ Pendiente (Fase 2) |
| 5 | `style.css` | Archivo vacío | ⚠️ Sin estilo ni resaltado | ⬜ Pendiente (Fase 2) |
| 6 | `GameController` | Sin `initialize()`, sin celdas, sin eventos; `onHelp()` vacío; no existe "Nuevo juego" | ❌ Grid vacío, juego inoperante | ✅ Eventos 2026-11-06 · ⬜ aún faltan "Nuevo juego" y ayuda con contador |
| 7 | `model/GameState.java` | Clase vacía: no hay fachada entre modelo y controlador | ❌ Modelo desconectado | ✅ Implementada 2026-11-04 (37 escenarios) |
| 8 | `model/SudokuGenerator.java` | Borraba 24 celdas al azar → no garantizaba 2 pistas por bloque 2×3 | ❌ Incumplía HU-2 (990/1000) | ✅ Corregido 2026-11-04 (1000/1000) |
| 9 | `model/SudokuGenerator.java` | Javadoc decía "generated using ChatGPT" y describía mal el borrado de pistas | ⚠️ Riesgo de nota 0 si no se comprende | ✅ Corregido 2026-11-04 |
| 10 | `model/SudokuBoard.java` | Faltaban `getConflicts`, `isComplete`, `isValid` y tamaño público | ⬜ Bloqueaba el controlador | ✅ Corregido 2026-11-04 (23 escenarios) |
| 11 | `Navigator` / `SummaryAware` | Reciben niveles y temporizador que no van en el enunciado | ⬜ Simplificar en Fase 4 | ⬜ Fase 4 |
| 12 | Entorno | `JAVA_HOME` apunta a JDK 17 y el `pom.xml` pide 24 | ❌ `mvnw compile` falla | ⚠️ Usar `C:\Users\Usuario\.jdks\corretto-24.0.2` |
| 13 | `model/GameState.java` | Falta Javadoc de clase, de `MAX_HINTS` y del constructor; `getHintsLeft` dice "clues" | ⚠️ Requisito de documentación | ✅ Ya estaba completo (verificado 2026-11-06) |
| 14 | `WelcomeController` | `onStart()` llamaba a `startNewGame()`, método inexistente en esa clase | ❌ `master` no compilaba | ✅ Corregido 2026-11-06 (movido a `GameController.initialize()`) |
| 15 | `GameController` | `startNewGame()` existía pero **nadie lo llamaba** | ❌ Tablero siempre vacío | ✅ Corregido 2026-11-06 (probado en vivo) |
| 16 | `SudokuBoardAdapter` / `EnterKeyAdapter` | Escritos pero **nunca invocados** → el teclado no funcionaba | ❌ No se podía jugar | ✅ `SudokuBoardAdapter` conectado 2026-11-06 · ⬜ `EnterKeyAdapter` sin usar |

> ⚠️ **Nota:** los problemas 3, 6 y 7 son *latentes*: la app arranca porque solo carga `Welcome.fxml` y, al pulsar "Comenzar", muestra `Game.fxml` con el grid vacío. Explotan al intentar jugar o ganar.

---

## 🔄 En curso

- **Fase 2 (vista):** `style.css` vacío, botón "Nuevo juego" y contador de ayuda sin crear,
  instrucciones "TODO" en el Welcome.
- **Fase 3 (eventos):** mouse/teclado/validación **ya conectados y probados en vivo**.
  Faltan: ayuda con contador real, victoria vía `showGameOver` y `EnterKeyAdapter`.

---

## 📌 Pasos que faltan

### Fase 1 — Modelo
- [x] `SudokuBoard`: arreglos `values` + `isClue`, constante `SIZE`, constructor.
- [x] `SudokuBoard.isValidPlacement(f, c, valor)` — fila, columna y bloque 2×3.
- [x] `SudokuBoard.setValue` / `setInitial` (package-private) / `getValue` / `isClue`.
- [x] Javadoc en `SudokuBoard`.
- [x] `SudokuBoard.getConflicts(f, c, valor)` — celdas que chocan (verificado 2026-11-04).
- [x] `SudokuBoard.isComplete()` / `isValid()` — acceso al tamaño con `SIZE` público (verificado 2026-11-04).
- [x] Reponer Javadoc de `SudokuBoard.isValidPlacement` (2026-11-04).
- [x] `SudokuGenerator`: backtracking (`fillBoard`) y puzzle con solución garantizada.
- [x] `SudokuGenerator`: **exactamente 2 pistas por bloque 2×3** (12 en total). Verificado 1000/1000.
- [x] `GameState`: tablero, generador, ayudas (3), estado de victoria y API de juego (verificado 37 escenarios).
- [x] Javadoc de `GameState` (clase, constructor, `MAX_HINTS`) y "clues" → "hints" ✅ verificado 2026-11-06: ya estaba hecho.

### Fase 2 — Vista
- [x] Crear 36 celdas en `initialize()` (`TextField` de 1 dígito, solo lectura para pistas) ✅ `buildgridUI()`.
- [ ] `Game.fxml`: barra de botones ("Nuevo juego", "Ayuda (3)") + `GridPane` 6×6 con límites de bloque.
- [ ] `style.css`: paleta, `.celda-error`, `.celda-ayuda`, `.celda-seleccionada` (+ migrar el controlador a esas clases).
- [ ] `Welcome.fxml`: instrucciones reales (quitar "TODO").
- [ ] `GameOver.fxml`: mensaje de victoria + "Nuevo juego".

### Fase 3 — Controlador y eventos
- [x] `initialize()` genera y pinta el puzzle ✅ (corrige el tablero vacío).
- [x] Mouse: clic selecciona/resalta celda ✅ (`selectCell` + `requestFocus()`), probado en vivo.
- [x] Teclado: 1–6 escribe, BACKSPACE/DELETE vacía, resto `consume()` ✅ (`SudokuBoardAdapter`), probado en vivo.
- [x] Validación en tiempo real → borde rojo + mensaje ✅ probada en vivo.
- [ ] Ayuda: 1 celda por clic, contador 3 → 0, botón se deshabilita (hoy `onHelp` ignora `GameState`).
- [ ] Detección de victoria → `showGameOver` (hoy muestra un `Alert` suelto).
- [ ] Alerta de confirmación al iniciar nuevo juego (solo desde el Welcome, falta el botón "Nuevo juego" en partida).
- [ ] Usar `EnterKeyAdapter` en alguna escena (Welcome o GameOver).

### Fase 4 — Pantalla de victoria
- [ ] Simplificar `SummaryAware` / `Navigator.showGameOver` (sin niveles ni tiempo).
- [ ] Corregir `GameOver.fxml` (`onAction`, `fx:id`) y `restartGame()`.

### Fase 5 — Verificación
- [ ] Checklist HU-1 … HU-5 completo.
- [ ] `./mvnw javadoc:javadoc` sin errores.
- [ ] Revisión de MVC (`model/` sin JavaFX, controlador sin lógica de negocio).

### Fase 6 — Git/GitHub
- [ ] Verificar que cada integrante tenga ≥2 commits y participación en ≥2 PRs.
- [ ] README con capturas y mapa HU → clase.

---

## 📚 Bitácora de aprendizaje

> Las lecciones se registran en **`training.md`** (ver `agents.md`, regla 6).

- **L01–L06** (2026-11-03): JPMS/`module-info`, variables de entorno, estructura del `pom.xml`,
  Project SDK en IntelliJ, pipeline `mvnw clean javafx:run` + MVC, y warnings de Maven vs Java 24.
- **L07–L11** (2026-11-03): encapsulamiento e invariantes, constructores y `final`, arreglos 2D,
  Query vs Command + guard clauses, convenciones JavaBeans y Javadoc como contrato.
- **L12–L18** (2026-11-04): `GameState` como fachada (SRP), dirección de dependencias (DIP),
  regresión de HU-2 y verificación empírica, API defensiva, riesgo del código de IA,
  resolución FXML (`onAction`/`fx:id`) y entorno JDK 17 vs. 24.
