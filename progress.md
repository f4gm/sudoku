# progress.md — Avances del proyecto Sudoku 6×6

> Registro de **lo que ya está hecho** y **lo que falta**.
> Actualizar este archivo al terminar cada paso. El plan completo está en `agents.md`.

**Última actualización:** 2026-11-04

---

## 📊 Estado general

| Fase | Descripción | Estado |
|------|-------------|--------|
| Fase 0 | Que compile y arranque (`mvn javafx:run`) | ✅ Completada |
| Fase 1 | Modelo: `SudokuBoard`, `SudokuGenerator`, `GameState` | 🔄 Implementados y verificados ✅ · falta Javadoc de `GameState` |
| Fase 2 | Vista: FXML + CSS + celdas | ⬜ No iniciada |
| Fase 3 | Controlador: mouse, teclado, validación, ayuda | ⬜ No iniciada (controlador desconectado del modelo) |
| Fase 4 | Pantalla de victoria (GameOver simplificada) | ⬜ No iniciada |
| Fase 5 | Verificación de criterios de aceptación | ⬜ No iniciada |
| Fase 6 | Git/GitHub: ≥2 PRs y ≥2 commits por estudiante | 🔄 PRs #5 y #6 mergeados; falta verificar el reparto por integrante |

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

### 📍 Punto exacto para retomar (próxima sesión)

1. **Cerrar Fase 1 — Javadoc:** completar el Javadoc de `GameState` (clase, constructor y `MAX_HINTS`), cambiar "clues" por "hints" en `getHintsLeft` y añadir `@param value` en `play`.
2. **Vista** — `Game.fxml` (botón "Nuevo juego", `fx:id`, contador de ayuda) + `style.css` (paleta, `.celda-error`, `.celda-ayuda`, `.celda-seleccionada` y bordes de bloque).
3. **Controlador** — celdas en `initialize()`, mouse/teclado, validación en tiempo real, ayuda, victoria, alerta de nuevo juego y uso de `EnterKeyAdapter`.
4. **GameOver** — alinear `onAction`/`fx:id` y simplificar el resumen a un mensaje de victoria.

> ⚠️ **Importante:** `SudokuBoard.java`, `SudokuGenerator.java` y `GameState.java` tienen cambios **sin commitear** (ver `git status`). Conviene revisarlos y commitearlos antes de seguir, o se pierden al cambiar de rama.

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
| 6 | `GameController` | Sin `initialize()`, sin celdas, sin eventos; `onHelp()` vacío; no existe "Nuevo juego" | ❌ Grid vacío, juego inoperante | ⬜ Fase 3 |
| 7 | `model/GameState.java` | Clase vacía: no hay fachada entre modelo y controlador | ❌ Modelo desconectado | ✅ Implementada 2026-11-04 (37 escenarios) |
| 8 | `model/SudokuGenerator.java` | Borraba 24 celdas al azar → no garantizaba 2 pistas por bloque 2×3 | ❌ Incumplía HU-2 (990/1000) | ✅ Corregido 2026-11-04 (1000/1000) |
| 9 | `model/SudokuGenerator.java` | Javadoc decía "generated using ChatGPT" y describía mal el borrado de pistas | ⚠️ Riesgo de nota 0 si no se comprende | ✅ Corregido 2026-11-04 |
| 10 | `model/SudokuBoard.java` | Faltaban `getConflicts`, `isComplete`, `isValid` y tamaño público | ⬜ Bloqueaba el controlador | ✅ Corregido 2026-11-04 (23 escenarios) |
| 11 | `Navigator` / `SummaryAware` | Reciben niveles y temporizador que no van en el enunciado | ⬜ Simplificar en Fase 4 | ⬜ Fase 4 |
| 12 | Entorno | `JAVA_HOME` apunta a JDK 17 y el `pom.xml` pide 24 | ❌ `mvnw compile` falla | ⚠️ Usar `C:\Users\Usuario\.jdks\corretto-24.0.2` |
| 13 | `model/GameState.java` | Falta Javadoc de clase, de `MAX_HINTS` y del constructor; `getHintsLeft` dice "clues" | ⚠️ Requisito de documentación | ⬜ Pendiente |

> ⚠️ **Nota:** los problemas 3, 6 y 7 son *latentes*: la app arranca porque solo carga `Welcome.fxml` y, al pulsar "Comenzar", muestra `Game.fxml` con el grid vacío. Explotan al intentar jugar o ganar.

---

## 🔄 En curso

- **Fase 1 — Modelo:** `SudokuBoard`, `SudokuGenerator` y `GameState` implementados y verificados.
  Solo falta cerrar el Javadoc de `GameState`. Después arranca la **Fase 2 (vista)**.

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
- [ ] Javadoc de `GameState` (clase, constructor, `MAX_HINTS`) y corregir "clues" → "hints".

### Fase 2 — Vista
- [ ] `Game.fxml`: barra de botones ("Nuevo juego", "Ayuda (3)") + `GridPane` 6×6 con límites de bloque.
- [ ] Crear 36 celdas en `initialize()` (`TextField` de 1 dígito, solo lectura para pistas).
- [ ] `style.css`: paleta, `.celda-error`, `.celda-ayuda`, `.celda-seleccionada`.
- [ ] `Welcome.fxml`: instrucciones reales (quitar "TODO").
- [ ] `GameOver.fxml`: mensaje de victoria + "Nuevo juego".

### Fase 3 — Controlador y eventos
- [ ] `initialize()` genera y pinta el puzzle desde `GameState`.
- [ ] Mouse: clic selecciona/resalta celda.
- [ ] Teclado: 1–6 escribe, BACKSPACE/DELETE vacía, resto `consume()`.
- [ ] Validación en tiempo real → borde rojo + mensaje.
- [ ] Ayuda: 1 celda por clic, contador 3 → 0, botón se deshabilita.
- [ ] Detección de victoria → `showGameOver`.
- [ ] Alerta de confirmación al iniciar nuevo juego.
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
