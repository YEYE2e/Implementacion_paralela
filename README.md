# Implementación Paralela de Algoritmos de Ordenamiento

Este proyecto implementa y compara algoritmos de ordenamiento tanto en versiones secuenciales como paralelas, evaluando su rendimiento en diferentes condiciones de datos.

## 📋 Descripción

El proyecto realiza experimentos empíricos para comparar el rendimiento de algoritmos de ordenamiento implementados de manera secuencial y paralela. Se evalúan 6 algoritmos diferentes con múltiples tipos de datos y tamaños de arreglos.

## 🚀 Algoritmos Implementados

### Algoritmos Secuenciales
- **MergeSort**: Ordenamiento por mezcla
- **QuickSort**: Ordenamiento rápido (con optimización de mediana de tres)
- **RadixSort**: Ordenamiento por base
- **BitonicSort**: Ordenamiento bitónico
- **SampleSort**: Ordenamiento por muestreo
- **BucketSort**: Ordenamiento por cubetas

### Algoritmos Paralelos
- **ParallelMergeSort**: Implementación paralela usando ForkJoinPool
- **ParallelQuickSort**: Implementación paralela con RecursiveAction
- **ParallelRadixSort**: Implementación paralela con streams
- **ParallelBitonicSort**: Implementación paralela con tareas recursivas
- **ParallelSampleSort**: Implementación paralela con distribución en buckets
- **ParallelBucketSort**: Implementación paralela con procesamiento concurrente

## 📊 Tipos de Datos Generados

- **Random**: Datos completamente aleatorios
- **NearlySorted**: Datos casi ordenados
- **Reversed**: Datos ordenados inversamente
- **FewUnique**: Datos con pocos valores únicos

## 🔬 Tamaños de Arreglos Evaluados

- 100 elementos
- 1,000 elementos
- 10,000 elementos
- 100,000 elementos
- 1'000,000 elementos

## 📁 Estructura del Proyecto

```
Concurrencia/
├── src/
│   ├── algorithms/
│   │   ├── sequential/      # Implementaciones secuenciales
│   │   └── parallel/        # Implementaciones paralelas
│   ├── generators/          # Generadores de datos
│   ├── experiment/          # Clases para ejecutar experimentos
│   └── Main.java           # Punto de entrada
├── results/
│   └── experiment_results.csv  # Resultados de los experimentos
└── README.md
```

## 🛠️ Requisitos

- Java 11 o superior
- Múltiples núcleos de CPU (recomendado para aprovechar el paralelismo)

## ▶️ Ejecución

Compilar el proyecto:
```bash
javac -d out -sourcepath src src/Main.java
```

Ejecutar los experimentos:
```bash
java -cp "out;src" Main
```

Los resultados se guardarán en `results/experiment_results.csv`

## 📈 Resultados

El archivo CSV generado contiene las siguientes columnas:
- **OS**: Sistema operativo
- **Processor**: Procesador
- **RAM_MB**: Memoria RAM disponible en MB
- **Cores**: Número de núcleos del procesador
- **Algorithm**: Nombre del algoritmo
- **Implementation**: Tipo de implementación (Sequential/Parallel)
- **DataType**: Tipo de datos (Random/NearlySorted/Reversed/FewUnique)
- **Size**: Tamaño del arreglo
- **Iteration**: Número de iteración
- **TimeMillis**: Tiempo de ejecución en milisegundos
- **TimeNanos**: Tiempo de ejecución en nanosegundos

## 🔧 Características Técnicas

- Uso de `ForkJoinPool` para paralelización
- Implementación de `RecursiveAction` para tareas recursivas
- Optimización de QuickSort con mediana de tres y stack iterativo
- Manejo robusto de errores (StackOverflowError, OutOfMemoryError)
- Exportación de resultados en formato CSV estándar

## 📝 Notas

- Los experimentos pueden tardar varios minutos en completarse
- Se realizan 5 iteraciones por cada combinación de algoritmo/tipo de datos/tamaño
- El programa maneja automáticamente errores y continúa con las siguientes pruebas

## 👤 Autor

YEYE2e

## 📄 Licencia

Este proyecto es de código abierto y está disponible para uso educativo y de investigación.

