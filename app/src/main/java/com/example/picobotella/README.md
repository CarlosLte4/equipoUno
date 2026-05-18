# Estructura del proyecto

```text
picobotella
├── data
│   ├── local
│   ├── remote
│   └── repository
├── domain
│   ├── model
│   └── usecase
├── MainActivity.kt
├── navigation
├── ui
│   ├── challenges
│   ├── components
│   ├── home
│   ├── instructions
│   ├── ratings
│   └── splash
└── utils
```

## data/

Contiene todo el acceso y manejo de datos de la aplicación.

### data/local/

Almacena la información local usando Room/SQLite.

Relación con HU:

- HU 6 → listar retos
- HU 7 → agregar reto
- HU 8 → editar reto
- HU 9 → eliminar reto
- HU 12 → obtener reto aleatorio

---

### data/remote/

Contiene acceso a APIs externas.

Relación con HU:

- HU 12 → consumir API de Pokémon

---

### data/repository/

Intermediario entre ViewModel y las fuentes de datos.

Responsabilidad:

- Obtener datos desde Room
- Obtener datos desde APIs
- Exponer información limpia a ViewModels

Relación con HU:

- HU 6–9 → operaciones sobre retos
- HU 11 → lógica del juego
- HU 12 → reto y Pokémon aleatorio

---

## domain/

Contiene modelos internos y lógica principal.

### domain/model/

Modelos usados por la aplicación.

Relación con HU:

- HU 11 → estado de la botella
- HU 12 → información de Pokémon y retos

---

### domain/usecase/

Contiene acciones específicas de negocio.

Relación con HU:

- HU 6–9 → CRUD retos
- HU 11 → giro de botella
- HU 12 → reto aleatorio

---

## ui/

Contiene toda la interfaz visual.

Cada carpeta representa una funcionalidad específica.

---

### ui/splash/

Relación con HU:

- HU 1 → pantalla splash

Responsabilidad:

- Animación inicial
- Temporizador
- Navegación al home

---

### ui/home/

Relación con HU:

- HU 2 → pantalla principal
- HU 3 → toolbar
- HU 11 → botella y contador

Responsabilidad:

- Botella
- Cuenta regresiva
- Audio
- Botón "Presióname"

---

### ui/challenges/

Relación con HU:

- HU 6 → listar retos
- HU 7 → agregar reto
- HU 8 → editar reto
- HU 9 → eliminar reto
- HU 12 → mostrar reto aleatorio

Responsabilidad:

- Mostrar retos
- CRUD de retos
- Diálogos relacionados

---

### ui/instructions/

Relación con HU:

- HU 5 → instrucciones del juego

Responsabilidad:

- Reglas
- Texto explicativo
- Animaciones

---

### ui/ratings/

Relación con HU:

- HU 4 → calificación de la aplicación

Responsabilidad:

- Redireccionar a la simulación de Google Play

---

### ui/components/

Componentes reutilizables usados por múltiples pantallas.

Relación con HU:

- HU 3 → toolbar personalizada
- HU 11 → botón animado
- HU 12 → componentes del diálogo

---

## navigation/

Contiene rutas y navegación entre pantallas.

Relación con HU:

Todas las HU que requieren cambio entre pantallas:

- HU 1 → Splash → Home
- HU 3 → Toolbar
- HU 5 → Instrucciones
- HU 6 → Retos
- HU 4 → Calificación

---

## utils/

Contiene herramientas reutilizables.

Relación con HU:

- HU 2 → sonido de fondo
- HU 11 → sonido de botella
- HU 3 → animaciones de botones
- HU 5 → animaciones
