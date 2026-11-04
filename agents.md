# agents.md — Guía del proyecto Sudoku 6×6 (FPOE Mini Proyecto 2)

> Este documento es la **fuente de verdad** del proyecto: contiene el plan de desarrollo,
> las reglas de colaboración con OpenCode y los criterios de aceptación del enunciado.

---

## 📄 Resumen del enunciado

**Curso:** Fundamentos de Programación Orientada a Eventos (FPOE)
**Reto:** Juego **Sudoku 6×6** (números 1–6; sin repetir en fila, columna y bloque 2×3).
**Stack:** Java 24 · JavaFX 21 · FXML/Scene Builder · Maven · IntelliJ IDEA · Git/GitHub
**Integrantes:** 3 estudiantes
**Requisito Git:** mínimo **2 Pull Requests** y **2 commits por estudiante**.

### Historias de usuario (HU)

| ID | Historia | Criterios clave |
|----|----------|-----------------|
| **HU-1** | Interfaz gráfica intuitiva y atractiva | Cuadrícula 6×6 visible; botones etiquetados; disposición lógica; diseño coherente |
| **HU-2** | Iniciar un juego cuando se desee | Botón de inicio + **alerta de confirmación**; el tablero se rellena con **2 números por bloque 2×3** (12 pistas) validando que no se repitan |
| **HU-3** | Ingresar números en las celdas | Seleccionar celda con el **mouse**; escribir **1–6 con el teclado**; poder **borrar**; solo se acepta el rango 1–6 |
| **HU-4** | Validación automática en tiempo real | Detectar repetición en fila/columna/bloque 2×3; **resaltar celdas en rojo** + mensaje de error |
| **HU-5** | Opción de ayuda limitada | Botón "Ayuda"; sugiere **1 celda vacía válida** resaltada; **no debe ser posible terminar el tablero con la ayuda** |

### Objetivos específicos (los que se evalúan)

1. GUI intuitiva con aspectos básicos de UX/UI.
2. Usar **al menos una interfaz o clase adaptadora**.
3. Implementar **eventos de mouse y teclado**.
4. Aplicar **arquitectura Modelo-Vista-Controlador (MVC)**.
5. Documentar el código con **Javadoc**.
6. Emplear **layouts** en la interfaz.
7. Implementar una **estructura de datos**.
8. Gestionar el proyecto con **Git** en GitHub (≥2 PRs y ≥2 commits por estudiante).

### Condiciones no negociables

- Solo entregas **grupales**; sin entregas tardías.
- Sustentación: hacer **un cambio asignado en 15 minutos** + responder preguntas sobre el código fuente, **en los computadores de la sala**.
- Causas de nota 0: no hacer el cambio, no responder preguntas, **copiar código de otros proyectos** o **usar código de IA sin saber qué hace**.

---

## 📋 Plan de desarrollo

### Estado del repo al inicio

| Ya existe | Problemas detectados |
|---|---|
| `Main`/`Launcher`, `Navigator`, interfaces `Navigable`/`SummaryAware`, adaptador `EnterKeyAdapter` | `module-info.java` **exporta el paquete `service.timer` que no existe** → no compila |
| FXML de Welcome, Game, GameOver (VBox/GridPane) | `pom.xml` apunta `mainClass` a `...HelloApplication` (no existe) → `mvn javafx:run` falla |
| `GameController` con `onHelp()` vacío y sin celdas | `GameOver.fxml` llama `#onStart` pero el método es `restartGame()`; faltan `fx:id` → NullPointerException |
| Paquetes `model` (`SudokuBoard`, `SudokuGenerator`, `GameState`) **vacíos** | Mojibake en los FXML (`A?Felicidades`), `style.css` vacío, sin validación ni generación |

### Fase 0 — Poner el esqueleto a compilar y correr

1. Verificar JDK: `./mvnw -v` (el pom pide Java 24).
2. `module-info.java`: eliminar `exports/opens com.example.sudoku.service.timer`.
3. `pom.xml`: `mainClass` → `com.example.sudoku/com.example.sudoku.Main`.
4. Corregir encoding de los FXML y arreglar `onAction`/`fx:id` de `GameOver.fxml`.
5. Verificar: `./mvnw clean javafx:run` abre la ventana de bienvenida.

### Fase 1 — Modelo (HU-2, HU-4) — paquete `model`

1. **`SudokuBoard`** (estructura de datos exigida):
   - `int[][] celdas = new int[6][6]` (0 = vacía) + `boolean[][] esPista`.
   - Getters/setters, `esPista(f,c)`, `estaCompleto()`.
   - `esValida(f, c, valor)`: sin repetir en fila, columna **y bloque 2×3** (`f/2`, `c/3`).
   - `getConflicto(f, c, valor)`: devuelve las celdas que chocan → la vista las resalta en rojo.
2. **`SudokuGenerator`**:
   - `generarSolucion()`: backtracking con mezcla aleatoria de 1–6.
   - `generarPuzzle()`: deja **exactamente 2 números por bloque 2×3 (12 pistas)**, validando que el tablero sea válido.
3. **`GameState`**: tablero vivo, celdas incorrectas, **ayudas restantes (3)**, estado `GANADO`.
4. **Javadoc** en todas las clases y métodos públicos.

### Fase 2 — Vista (HU-1) — FXML + CSS

1. **`Game.fxml`**: barra `HBox` con botones "Nuevo juego", "Ayuda (3)" y `statusLabel`; `GridPane` 6×6 con restricciones iguales y **borde cada 2 columnas / 3 filas** para marcar los bloques 2×3.
2. Las 36 celdas se **crean en `initialize()`** (no a mano en el FXML): `TextField` de 1 carácter, solo lectura para las pistas.
3. **`style.css`**: paleta coherente, `.celda-error` (borde rojo), `.celda-ayuda` (resaltado), `.celda-seleccionada`.
4. `Welcome.fxml`: título + instrucciones reales (quitar "TODO") + "Comenzar".
5. `GameOver.fxml`: mensaje de victoria + "Nuevo juego".

### Fase 3 — Controlador y eventos (HU-3, HU-4, HU-5)

1. `initialize()`: genera el puzzle y pinta las 36 celdas.
2. **Mouse**: `setOnMouseClicked` por celda → selecciona/resalta y da foco.
3. **Teclado**: handler en el `GridPane` → **1–6** escribe, **BACKSPACE/DELETE** vacía, el resto `consume()`.
4. **Validación en tiempo real**: tras cada tecla → `esValida()`; si hay conflicto → borde rojo + mensaje en `statusLabel`; si no, se limpia.
5. **Ayuda**: elige la celda vacía con menos candidatos, la rellena, la resalta, decrementa el contador y **deshabilita el botón al llegar a 0**.
6. **Victoria**: si `estaCompleto() && válido` → `navigator.showGameOver(...)`.
7. **Nuevo juego**: `Alert` de confirmación (HU-2) antes de regenerar.

### Fase 4 — Pantalla de victoria

- Simplificar `GameOverController.setSummary(...)` a solo el mensaje de victoria (quitar niveles/temporizador).
- `restartGame()` → `navigator.showWelcome()` o juego nuevo.

### Fase 5 — Verificación (criterios de aceptación)

- [ ] HU-1: cuadrícula 6×6 visible, botones etiquetados, layout lógico, diseño coherente.
- [ ] HU-2: botón de inicio + alerta de confirmación + 12 pistas sin repetirse.
- [ ] HU-3: clic selecciona celda; teclado 1–6 escribe; borrar vacía; sin valores fuera de rango.
- [ ] HU-4: conflicto en rojo + mensaje; validación en tiempo real.
- [ ] HU-5: botón Ayuda visible, 1 celda resaltada, contador 3/3 → 0.
- [ ] Javadoc completo (`./mvnw javadoc:javadoc` sin errores).
- [ ] MVC visible: `model/` sin JavaFX, `controller/` sin lógica de negocio, FXML sin lógica.

### Fase 6 — Git/GitHub (≥2 PRs y ≥2 commits por estudiante)

1. Rama por integrante desde `master`: `fagm/...`, `ggo/...`, etc. (ya existen `origin/fagm` y `origin/ggo`).
2. Flujo: `master` ← **Pull Request** ← rama feature, con revisión antes del merge.
3. Reparto sugerido:
   - **A (model):** `SudokuBoard` + `SudokuGenerator` + `GameState` → 2 commits, 1 PR.
   - **B (vista):** FXML/Scene Builder + CSS + Welcome/GameOver → 2 commits, 1 PR.
   - **C (controlador):** eventos mouse/teclado + validación + ayuda → 2 commits, 1 PR.
4. **README**: descripción, cómo ejecutar (`./mvnw clean javafx:run`), capturas, mapa HU → clase.

### Orden de ejecución

```
Fase 0 (compile) → Fase 1 (modelo) → Fase 2 (vista) → Fase 3 (eventos) → Fase 4 (victoria) → Fase 5 (QA) → Fase 6 (PRs)
```

La Fase 1 es bloqueante: **una sola persona la resuelve primero**; Fases 2 y 3 pueden ir en paralelo
(vista vs. controlador) una vez exista el contrato de `SudokuBoard`.

---

## 🤖 Guía de Interacción con OpenCode (Modo Tutorial)

Este proyecto está configurado para desarrollarse con la asistencia de **OpenCode**, actuando en **Modo Tutorial**.

### 📜 Reglas de Trabajo para la IA (OpenCode Prompt Rules)

1. **Modo Tutorial:**
   - OpenCode actúa como un **video tutorial interactivo**: explica qué hay que hacer, cómo se hace, por qué se hace de esa manera y sugiere el código a escribir.
   - Explica la teoría/diseño de cada paso antes de pedir que se implemente.

2. **El código lo escribe el desarrollador:**
   - OpenCode **NO debe escribir archivos ni código por su cuenta**. Solo sugiere qué código escribir, cómo y por qué.
   - El desarrollador debe escribir el código, no dejar que la IA lo escriba todo. OpenCode solo escribe código si el desarrollador lo pide **explícitamente**.
   - **Excepción acordada:** OpenCode sí puede crear/editar por su cuenta los archivos de **documentación y seguimiento del proyecto** (`agents.md`, `progress.md`, `training.md`, `README.md`). El código fuente del juego (`.java`, `.fxml`, `.css`, `pom.xml`) sigue escribiéndolo el desarrollador.

3. **Guía paso a paso:**
   - El desarrollo avanza de forma incremental, un paso a la vez.
   - En cada paso: explicar el concepto → sugerir el código → esperar a que el desarrollador lo escriba → revisarlo y dar el siguiente paso.

4. **Enseñanza de conceptos (Clean Architecture y SOLID):**
   - Al introducir un concepto de **Clean Architecture** o **SOLID** (por ejemplo: puertos y adaptadores, Dependency Inversion, Single Responsibility, separación de capas), explicar la teoría del concepto y su justificación en el contexto del proyecto antes de sugerir el código.
   - Vincular cada decisión de diseño con el principio que la respalda (qué problema resuelve, qué trade-off implica).

5. **Técnica "try and verify" (practicar y verificar):**
   - Para conceptos nuevos o para afianzar el aprendizaje, considerar usar la técnica **"try and verify"**: proponer que el desarrollador intente implementar por su cuenta (con orientación de alto nivel, sin dar el código completo) y luego verificar/depurar el resultado juntos.
   - Aplicarla cuando el concepto es clave para el proyecto (ej.: un caso de uso, un adaptador, un middleware) y el desarrollador ya tiene los cimientos para intentarlo.

6. **Bitácora de aprendizaje (`training.md`):**
   - Todas las explicaciones, lecciones y conceptos enseñados deben quedar registrados en `/training.md` (raíz del proyecto) para que el desarrollador pueda consultarlos después.
   - Al final de cada paso donde se haya enseñado un concepto, agregar una entrada en `training.md` con: fecha, concepto, explicación resumida y referencia al archivo/decisión del proyecto donde se aplica.

---

## 📚 Conceptos clave a repasar (POO y Orientación a Eventos)

- **POO:** encapsulamiento, abstracción, herencia, polimorfismo → ver `SudokuBoard` (encapsulamiento), interfaces `Navigable`/`SummaryAware` (abstracción/polimorfismo).
- **Orientada a eventos:** oyentes (`EventHandler`), adaptadores (`EnterKeyAdapter`), bubbling/consumo de eventos, mouse vs. teclado.
- **MVC:** qué vive en `model/`, `controller/`, `view/` y por qué no se mezclan.
- **Clean Architecture:** dependencias hacia adentro, puertos y adaptadores, casos de uso.
- **SOLID:** SRP, OCP, LSP, ISP, DIP — cada decisión de diseño se vincula con el principio que la respalda.

> Las lecciones impartidas se registran en **`training.md`**.
