Sistemas operativos. Curso 2018/2019
#Tema 2. Procesos e hilos.
###1. Generalidades sobre procesos e hilos.
####1.1. Ejecución del SO dentro de los procesos de usuario
<div style="text-align: justify">El sistema operativo se percibe como un conjunto de rutinas que el usuario invoca para realizar diferentes funciones, ejecutadas dentro del entorno de proceso de usuario.

Cuando ocurre una llamada al sistema o una interrupción, el procesador se pone en modo núcleo y se pasa el control al sistema operativo, cambiando el modo a una rutina del sistema operativo, continuando aún así la ejecución en modo usuario. Tras haber realizado el trabajo, el sistema operativo determina si el programa debe continuar su ejecución, realizando un cambio de modo y continuando el proceso interrumpido. En caso contrario, el control se pasa a la rutina de cambio de proceso.

En cuanto a estructura de proceso, éste se compone de un bloque de control formado por un identificador y la información tanto del estado del procesador como de control de proceso, una pila de usuario, un espacio privado de direcciones de usuario, una pila de núcleo, utilizada para manejar llamadas/retornos cuando el proceso está en modo núcleo y un espacio compartido entre todos los procesos de direcciones, en el que se encuentra el código y los datos del sistema operativo. 
####1.2. Creación de procesos.
Cuando se va a añadir un nuevo proceso, el sistema operativo construye las estructuras de datos que se usan para manejar ese proceso así como reservar el espacio de direcciones en memoria principal. Los sucesos más comunes que provocan la creación de procesos son los siguientes:

- En un entorno por lotes, un proceso nuevo es creado por el planificador como respuesta a una nueva solicitud de trabajo.

- En una sesión interactiva, ocurre cuando un usuario entra en el sistema desde una terminal.

- Un programa de usuario puede necesitar realizar una función o un servicio, para el cual el SO crea un nuevo proceso en respuesta.

- Por motivos de modularidad o paralelismo, un proceso puede crear otros mediante llamadas al sistema.
####1.3. Cambio de contexto
Durante la ejecución de un proceso, sus registros, su puntero a pila, sus registros, su PC, etc. están cargados en la CPU. El sistema operativo puede interrumpirlo, para ello guarda primero los valores actuales de esos registros (contexto) en el PCB (Process Control Block) de ese proceso. Tras ello, puede cederle el uso de la CPU a otro proceso u operación. A esta conmutación de un proceso a otro de la CPU se le denomina **cambio de contexto**.
####1.4. PCBs y colas de estados
El SO mantiene una colección de colas que representan el estado de todos los procesos en el sistema, y de forma usual existe una cola por estado. Cada PCB está encolado en una cola de estado acorde al estado actual del proceso al que está asociado. Conforme un proceso cambia de estado, su PCB es retirado de una cola y encolado en otra. Podemos diferenciar las colas de estados en:

- Cola de trabajos: Conjunto de trabajos pendientes de ser admitidos por el sistema.

- Cola de preparados: conjunto de todos los procesos que residen en memoria principal, preparados y esperando para ejecutarse.

- Cola(s) de bloqueados: conjunto de procesos esperando un evento o un recurso actualmente no disponible.
####1.5. Planificación de procesos
El **planifcador de procesos** es la parte del sistema operativo que se encarga de, cuando dos o más procesos entran al mismo tiempo en el estado listo, decidir cuál de ellos va a ejecutarse a continuación y ocupar la CPU. Existen tres tipos de planificadores de la CPU:

- Planificador a largo plazo (o planificador de trabajos): Selecciona los trabajos que deben admitirse en el sistema.

- Planificador a corto plazo (o planificador de la CPU): Selecciona al proceso en estado preparado o ejecutable que debe ejecutarse a continuación y le asigna la CPU. Es invocado muy frecuentemente.

- Planificador a medio: Se encarga de sacar o introducir procesos de la memoria principal.
#####Comportamiento de un proceso
Se denomina **ráfaga de CPU** al tiempo que está un proceso en estado de ejecución. Normalmente un proceso alterna ráfagas de CPU con periodos de tiempo en que se encuentra en estado bloqueado.

Se dice que un proceso está "limitado por la E/S" si intercala pequeñas ráfagas de CPU con muchos tiempos de espera, es decir, que frecuentemente realice operaciones que provoquen un cambio al estado de bloqueo (como las operaciones E/S).

Un proceso que realiza operaciones muy largas que obligan a ráfagas largas de CPU con pocas interrupciones se dice que está "limitado por la CPU". 

Es importante que el planificador seleccione una buena mezcla de trabajos, puesto que en el caso de que la mayor parte fueran procesos limitados por E/S, la cola de preparados estaría prácticamente vacía y se infrautilizaría de CPU. Si ocurriera al contrario, serían entonces los dispositivos de E/S y, en general, los recursos aparte la CPU, los que estarían infrautilizados.

#####Planificador a corto plazo
El **planificador a corto plazo** es la parte del SO que decide a qué proceso darle el control de la CPU. El **despachador** es la parte del SO que realiza las acciones adecuadas para efectuar el cambio de asignación de CPU entre dos procesos, tal como haya decidido el planificador a corto plazo. Esto involucra salvar el contexto del proceso actual y restaurar el del nuevo proceso, aparte de saltar a la posición de memoria de del nuevo proceso para su reanudación. Estos términos se pueden usar de forma equivalente.

El planificador a corto plazo se activa en las siguientes situaciones:

- Al crear un nuevo proceso, se debe elegir si ejecutar el proceso padre o el proceso hijo.
- Cuando un proceso termina o se bloquea por esperar una operación de E/S u otra razón. Se debe elegir en este caso otro proceso para ejecutar.
- En el caso de que algún elemento del SO determine que el proceso actual no puede seguir ejecutándose, pasándolo al estado de bloqueo.
- Si un evento ajeno al proceso actual hace que el SO determine que el proceso actual no es el más preferente para disfrutar de la CPU, por ejemplo si un proceso agota el periodo de tiempo que tiene asignado.

Se dice que una política de planificación es **apropiativa o preemptive** si incluye los dos últimos puntos. Una política de planificación es denominada **con derecho preferente o con desplazamiento** si gestiona la activación del planificador por las siguientes razones:

- Se ha creado un nuevo proceso.

- Cierto suceso ha provocado un cambio en el estado de un proceso de *bloqueado* a *ejecutable*.

#####Medidas y objetivos de las políticas de planificación de CPU
Se definen ciertas medidas asociadas a cada proceso. Denotamos $t$ al tiempo de CPU de un proceso y definimos:

- Tiempo de respuesta o de finalización($T$): Tiempo total transcurrido desde que se crea el proceso hasta que se termina.

- Tiempo de espera($E$): Tiempo que el proceso ha estado esperando en la cola de ejecutables. Se calcula de la siguiente manera: $E=T-t$

- Penalización($P$): Proporción del tiempo empelado por el SO en tomar decisiones alusivas a la planificación de procesos. Debe representar entre el 10 y el 30% del tiempo total de la CPU. Se calcula de la siguiente manera: $P=\frac{T}{t}$

Las políticas de planificación de CPU tienen las siguientes metas:

- Realizar un buen uso de la CPU: Una alta proporción del tiempo se debe emplear para la ejecución de procesos.
- Dar un buen servicio: Se debe minimizar la penalización de los procesos.
- En entornos interactivos, es prioritario asegurar una ágil respuesta del sistema por encima de la eficiencia de la CPU.
- En entornos no interactivos, se debe asegurar el buen aproechamiento de la CPU

####1.6. Algoritmo de planificación FIFO (First In First Out)
Se trata del algoritmo de planificación más simple, puesto que los procesos son servidos según el orden de llegada a la zona de ejecutables. Se trata de un algoritmo no apropiativo, puesto que cada proceso se ejecuta hasta que se finaliza o se bloquea. 
</div>