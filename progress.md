# progress.md — Avances del proyecto Sudoku 6×6

> Registro de **lo que ya está hecho** y **lo que falta**.
> Actualizar este archivo al terminar cada paso. El plan completo está en `agents.md`.

**Última actualización:** 2026-11-03

---

## 📊 Estado general

| Fase | Descripción | Estado |
|------|-------------|--------|
| Fase 0 | Que compile y arranque (`mvn javafx:run`) | ✅ Completada |
| Fase 1 | Modelo: `SudokuBoard`, `SudokuGenerator`, `GameState` | 🔄 En curso (`SudokuBoard` casi listo) |
| Fase 2 | Vista: FXML + CSS + celdas | ⬜ No iniciada |
| Fase 3 | Controlador: mouse, teclado, validación, ayuda | ⬜ No iniciada |
| Fase 4 | Pantalla de victoria (GameOver simplificada) | ⬜ No iniciada |
| Fase 5 | Verificación de criterios de aceptación | ⬜ No iniciada |
| Fase 6 | Git/GitHub: ≥2 PRs y ≥2 commits por estudiante | 🔄 En curso (repo clonado y enlazado a `origin`) |

**Leyenda:** ⬜ pendiente · 🔄 en curso · ✅ completada · ⚠️ con problemas

---

## ✅ Hecho

- [x] **2026-11-03** — Repo clonado y verificado: `origin` → `https://github.com/f4gm/sudoku.git`, rama `master` sincronizada con `origin/master`.
- [x] **2026-11-03** — Leído el enunciado PDF completo (HU-1 a HU-5, objetivos, condiciones).
- [x] **2026-11-03** — Inventariado el código existente y detectados los problemas de compilación (ver abajo).
- [x] **2026-11-03** — Creado `agents.md` (plan + guía de interacción OpenCode) y `progress.md`.
- [x] **2026-11-03** — **Instalado JDK 24** (Eclipse Temurin 24.0.2 vía winget) y configurado `JAVA_HOME` + `PATH` de usuario. `mvnw -v` → Maven 3.8.5 + Java 24.0.2.
- [x] **2026-11-03** — **Fase 0 completada:** corregido `module-info.java` (quitado `service.timer`) y `mainClass` en `pom.xml` (`HelloApplication` → `Main`). Verificado: `mvnw -q compile` → exit 0, `mvnw validate` → exit 0, **`mvnw clean javafx:run` → exit 0 y la ventana de bienvenida se abre** ✅.
- [x] **2026-11-03** — **`SudokuBoard` implementado** (`src/main/java/com/example/sudoku/model/SudokuBoard.java`): campos `values`/`isClue`, constructor, `getValue`, `isClue`, `setValue`, `setClue` (package-private) e `isValid` (fila, columna y bloque 2×3). Javadoc completo en versión resumida. Verificado: `mvnw -q compile` → exit 0, `mvnw -q javadoc:javadoc` → exit 0.

### Decisiones tomadas (acordadas con el desarrollador)

1. **GameOver se mantiene** como pantalla de **victoria** al resolver el tablero.
2. **Ayuda limitada:** 3 usos + 1 sola celda rellenada por clic (cumple "no terminar el tablero con ayuda").
3. **Sin tests JUnit** por ahora: solo la aplicación.

### Problemas detectados

| # | Archivo | Problema | Impacto | Estado |
|---|---------|----------|---------|--------|
| 1 | `src/main/java/module-info.java` | `exports/opens com.example.sudoku.service.timer` — el paquete no existe | ❌ No compilaba | ✅ Corregido 2026-11-03 |
| 2 | `pom.xml` | `mainClass` = `...HelloApplication` (clase inexistente) | ❌ `javafx:run` fallaba | ✅ Corregido 2026-11-03 |
| 3 | `GameOver.fxml` | `onAction="#onStart"` pero el método es `restartGame()`; faltan `fx:id` de `feedbackLabel`, `recordLabel`, `remainingTimeLabel` | ❌ NPE al llegar a GameOver | ⬜ Pendiente (Fase 4) |
| 4 | `Welcome.fxml`, `GameOver.fxml` | Caracteres corruptos (`A?Felicidades`, `A?Bienvenido`) | ⚠️ Visual | ⬜ Pendiente (Fase 2) |
| 5 | `style.css` | Archivo vacío | ⚠️ Sin estilo | ⬜ Pendiente (Fase 2) |
| 6 | `GameController.onHelp()` | Método vacío, sin celdas en el grid | ⬜ Funcionalidad pendiente | ⬜ Fase 3 |
| 7 | `model/` | `SudokuBoard`, `SudokuGenerator`, `GameState` vacíos | ⬜ Fase 1 pendiente | ⬜ Fase 1 |
| 8 | `Navigator` / `SummaryAware` | Reciben niveles y temporizador que no van en el enunciado | ⬜ Simplificar en Fase 4 | ⬜ Fase 4 |

> ⚠️ **Nota:** los problemas 3 y 4 son *latentes*: la app arranca porque solo carga `Welcome.fxml`. Explotarán al navegar a GameOver. No rompen la Fase 0 pero hay que resolverlos antes de la entrega.

---

## 🔄 En curso

- **Fase 1 — Modelo:** `SudokuBoard` implementado; falta `getConflicto`, `SudokuGenerator` y `GameState`.

---

## 📌 Pasos que faltan

### Fase 0 — Compilación base ✅ (2026-11-03)
- [x] Verificar JDK con `./mvnw -v` (requiere Java 24) → Temurin 24.0.2 instalado.
- [x] Corregir `module-info.java` (quitar `service.timer`).
- [x] Corregir `mainClass` en `pom.xml`.
- [x] Verificar que arranca: `./mvnw clean javafx:run` → ventana de bienvenida OK.
- [ ] ~~Corregir `GameOver.fxml` (`onAction` + `fx:id`)~~ → movido a Fase 4.
- [ ] ~~Corregir encoding de los FXML (acentos)~~ → movido a Fase 2.

### Fase 1 — Modelo
- [x] `SudokuBoard`: arreglos `values` + `isClue`, constante `SIZE`, constructor.
- [x] `SudokuBoard.isValid(f, c, valor)` — fila, columna y bloque 2×3.
- [x] `SudokuBoard.setValue` / `setClue` (package-private) / `getValue` / `isClue`.
- [x] Javadoc en `SudokuBoard` (versión resumida).
- [ ] `SudokuBoard.getConflicto(f, c, valor)` — celdas que chocan.
- [ ] `SudokuGenerator.generarSolucion()` — backtracking aleatorio.
- [ ] `SudokuGenerator.generarPuzzle()` — 2 pistas por bloque (12 en total).
- [ ] `GameState` — tablero, incorrectas, ayudas (3), estado.
- [ ] Javadoc en el resto del paquete `model`.

### Fase 2 — Vista
- [ ] `Game.fxml`: barra de botones + `GridPane` 6×6 con límites de bloque.
- [ ] Crear 36 celdas en `initialize()` (TextField de 1 dígito).
- [ ] `style.css`: paleta, `.celda-error`, `.celda-ayuda`, `.celda-seleccionada`.
- [ ] `Welcome.fxml`: instrucciones reales (quitar "TODO").
- [ ] `GameOver.fxml`: mensaje de victoria + "Nuevo juego".
- [ ] Javadoc en los controladores.

### Fase 3 — Controlador y eventos
- [ ] `initialize()` genera y pinta el puzzle.
- [ ] Mouse: clic selecciona/resalta celda.
- [ ] Teclado: 1–6 escribe, BACKSPACE/DELETE vacía, resto `consume()`.
- [ ] Validación en tiempo real → borde rojo + mensaje.
- [ ] Ayuda: 1 celda por clic, contador 3 → 0, botón se deshabilita.
- [ ] Detección de victoria → `showGameOver`.
- [ ] Alerta de confirmación al iniciar nuevo juego.

### Fase 4 — Pantalla de victoria
- [ ] Simplificar `SummaryAware`/`Navigator.showGameOver` (sin niveles ni tiempo).
- [ ] `restartGame()` funcional.

### Fase 5 — Verificación
- [ ] Checklist HU-1 … HU-5 completo.
- [ ] `./mvnw javadoc:javadoc` sin errores.
- [ ] Revisión de MVC (`model/` sin JavaFX).

### Fase 6 — Git/GitHub
- [ ] Definir ramas por integrante.
- [ ] **Integrante A:** ≥2 commits + ≥2 PRs (model).
- [ ] **Integrante B:** ≥2 commits + ≥2 PRs (vista).
- [ ] **Integrante C:** ≥2 commits + ≥2 PRs (controlador).
- [ ] README con capturas y mapa HU → clase.

---

## 📚 Bitácora de aprendizaje

> Las lecciones se registran en **`training.md`** (ver `agents.md`, regla 6).

- **L01–L06** (2026-11-03): JPMS/`module-info`, variables de entorno, estructura del `pom.xml`,
  Project SDK en IntelliJ, pipeline `mvnw clean javafx:run` + MVC, y warnings de Maven vs Java 24.
- **L07–L11** (2026-11-03): encapsulamiento e invariantes, constructores y `final`, arreglos 2D,
  Query vs Command + guard clauses, convenciones JavaBeans y Javadoc como contrato.
- Siguiente lección prevista: **listas/colecciones** con `getConflicto` y **recursión (backtracking)**
  con `SudokuGenerator`.
