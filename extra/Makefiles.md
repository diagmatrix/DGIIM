*Manuel Gachs Ballegeer*

# Makefiles

## 0. Introducción

Un archivo del tipo *makefile* es un archivo que hace uso de la utilidad `make`. Esta utilidad determina que partes de un programa deben de compilarse, y les manda comandos para hacerlo. En esencia, un *makefile* sirve para autocompilar un proyecto en lugar de hacerlo manualmente.

Para poder hacer uso de `make`, en primer lugar debe crearse un archivo del tipo *makefile*. Para ello basta con crear uno con nombre "makefile" o "Makefile". Ambos nombres son completamente válidos (el manual de la GNU recomienda usar el segundo, puesto que suele aparecer cerca del inicio en los listados de directorios, junto otros arhcivos importantes como README).

Es posible también nombrar un archivo *makefile* con otro nombre, pero debemos especificarselo a make después con uno de los siguientes formatos:

```bash
make -f ARCHIVO
make --file=ARCHIVO
make --makefile=ARCHIVO
```

En estos apuntes se usarán ejemplos de programas en lenguaje C++. Estos ejemplos junto con todos los makefiles mostrados se encuentran [aquí](https://github.com/Manuelbelgicano/adventure). Los makefiles sirven para muchos más lenguajes de programación, además del presentado en estos apuntes. Para información más a fondo sobre makefiles que no aparezca en estos apuntes consultar en el [manual de la GNU](https://www.gnu.org/software/make/manual/make.html).

### 0.1. Contenido de un makefile

Los makefiles están compuestos por 5 tipos de cosas:

1. **Reglas**. Es en definitiva lo que queremos que haga la orden ``make``.
   1. Relgas explícitas. Reglas descritas por el autor del makefile.
   2. Reglas implícitas. Reglas automáticas de ``make``.
2. **Definiciones de variables**. Las variables son alias puestos por el autor del makefile para hacer el archivo más legible y fácil de modificar o ampliar.
3. **Directivas**. Son expresiones o palabras que se usan para diversas funciones que ayudan a simplificar un makefile.
4. **Comentarios**. Se tratan de anotacionesPara escribir un comentario, simplemente se debe escribir el caracter '#' antes del comentario.

## 1. Reglas

Pongamos un ejemplo de una regla. Imaginemos que tenemos un programa en lenguaje C++ que consta de un archivo ``goblin.cpp``, que tiene en su interior una inclusión de la cabecera ``evil.h``. Para obtener el objeto de forma manual escribiríamos en la terminal la siguiente orden:

```bash
g++ -c -g goblin.cpp
```

Esto se traduciría como la siguiente regla en un makefile:

```makefile
goblin.o: goblin.cpp evil.h
	g++ -c -g goblin.cpp
```

Esta regla nos dice dos cosas:

1. Cuándo se ejecutará. Solamente tendrá efecto si el ``goblin.o`` no existe o si los archivos ``goblin.cpp`` o ``evil.h`` han sido modificados desde la última vez que se ejecutó. Por tanto, si usamos la orden ``make`` dos veces de forma consecutiva, esta regla no hará nada.
2. Cómo se obtiene ``goblin.o``. La regla dice exactamente como se construye el archivo objeto a partir de las fuentes.

### 1.1. Sintaxis de una regla

#### Sintaxis general

Una regla está compuesta por tres cosas: objetivos (normalmente uno), pre-requisitos o dependencias y recetas. Un objetivo es el archivo u orden que se quiere realizar. Los pre-requisitos son los archivos o reglas de las que depende el objetivo, mientras que las recetas son las acciones que se siguen para obtener el objetivo. Una regla suele verse así:

```Makefile
objetivos: pre-requisitos
	receta
	...
```

O alternativamente de esta manera:

```makefile
objetivos: pre-requisitos; receta
	receta
	...
```

Es **muy importante** seguir al pie de la letra esta sintaxis, puesto que ``make`` no identificará una línea como una receta si no comienza por un tab (literalmente un tab, no es válido una sucesión de espacios), así como no sabrá interpretar la regla si el objetivo no está separado de los pre-requisitos por el caracter ':'. Además, esta sintaxis tiene las siguientes consecuencias:

- Una línea en blanco que comienza por una tab no es una línea en blanco, sino una receta vacía.
- Un comentario en una receta no es un comentario de *makefile*, sino que será pasado al shell literalmente, por lo que depende del shell si es tratado como comentario o no.
- La definición de una variable dentro del "contexto de una receta" (dentro de una regla e indentado con un tab), será tratado como parte de una receta, no como una variable del makefile, y pasado al shell.
- Una expresión condicional dentro del "contexto de una receta" será considerado parte de una receta y pasado al shell. 

#### Sintaxis específica

Volvamos al ejemplo del principio. En la práctica, los programa son más grandes y no suelen tener todos los archivos en un solo directorio. Imaginemos ahora que tenemos el siguiente árbol de directorios:

```
.
├── doc
│   └── adventure.doxy
├── include
│   ├── evil.h
│   └── underground.h
├── Makefile
├── obj
└── src
    ├── dungeon.cpp
    ├── goblin.cpp
    └── main.cpp
```

Imaginemos que en el archivo Makefile se encuentra la regla descrita anteriormente para construir ``goblin.o``. Si hacemos la orden ``make`` saltará el siguiente error:

```bash
make: *** No rule to make target 'evil.h', needed by 'goblin.o'.  Stop.
```

¿Cuál es la razón de este error? ¿No era ``evil.h`` una dependencia y no un objetivo? Esto ocurre porque ``make`` busca archivos solamente en el directorio de trabajo actual. Al no encontrar la cabecera, interpreta que se trata del objetivo de una regla. ¿Cómo arreglar esto? Primeramente, debemos escribir la ruta completa del archivo de cabecera. Después, debemos especificar donde se encuentra ese archivo en la receta. Existen varias maneras de hacer esto: a través de la directiva ``vpath``, la variable ``VPATH`` y el uso de la opción ``-I`` o ``--include-dir`` en la regla. Las dos primeras opciones serán explicadas más adelante.

Esta opción de compilación busca posibles dependencias en  ``[directorio]``. En caso de no especificar ningún directorio, ``make`` busca en los siguientes directorios (si existen) en este orden: ``/usr/local/include``, ``usr/gnu/include``, ``/usr/local/include`` y ``/usr/include``. Otra opción interesante del compilador es ``-o <nombre>``. Esta opción permite modificar el nombre de la salida de la compilación, cambiándolo por ``nombre``. Además, suele ser interesante usar los siguientes flags: ``-Wall`` y ``-g``. El primero hace que el compilador nos muestre todos los warnings, mientras que el segundo especifica al compilador que el archivo resultante pueda ser debuggeado.

En resumen, un archivo *makefile* válido para el programa del ejemplo sería el siguiente:

```makefile
all: adventure;

adventure: obj/dungeon.o obj/goblin.o obj/main.o
  g++ -Wall -g -o adventure obj/dungeon.o obj/goblin.o obj/main.o

obj/main.o: src/main.cpp include/dungeon.h
  g++ -Wall -g -I./include -c src/main.cpp -o obj/main.o

obj/dungeon.o: src/dungeon.cpp include/dungeon.h
  g++ -Wall -g -I./include -c src/dungeon.cpp -o obj/dungeon.o
  
obj/goblin.o: src/goblin.cpp include/evil.h
  g++ -Wall -g -I./include -c src/goblin.cpp -o obj/goblin.o
```

Sin embargo, es evidente que esta forma de realizar un makefile no es muy práctica, puesto que si se realiza un cambio en el nombre de una carpeta o de un archivo debe buscarse manualmente todas sus instancias en las reglas y escribir el nuevo nombre. Es por ello que se suelen usar variables para nombrar la mayoría de elementos de un makefile.

### 1.2. Reglas especiales

No todas las reglas tienen como objetivos archivos, o sus recetas son simplemente compilar, o tienen pre-requisitos. Como puede observarse, en el ejemplo anterior existe una regla llamada ``all``, que no es un archivo y no tiene recetas. A continuación se explican qué son y cómo se usan este tipo de reglas.

#### Reglas cuyo objetivo no es un archivo

En muchas ocasiones es interesante no tener que realizar acciones manualmente, como eliminar archivos objeto, actualizar los archivos de la documentación, etc. Usando la orden ``make [orden]`` podemos crear reglas en el makefile con ``orden`` como objetivo de una regla, ya que los archivos *makefile* soportan órdenes del shell. He aquí una lista de las más se usan (al menos en la corta experiencia del autor):

- **``all ``**: Esta regla es muy importante, de hecho es casi obligada si se quiere estructurar el makefile de forma flexible. Cuando usamos la orden ``make``, se ejecuta la primera regla que aparezca en el makefile primero. Esto ocurre siempre a no ser que se escriba una regla cuyo objetivo sea ``all``, en cuyo caso se ejecuta esa primero. Suele ser interesante que sus pre-requisitos sean el archivo ejecutable del programa. Ejecutar ``make all`` es equivalente a ``make``.
- **``clean [name]``**: Esta regla sirve para borrar los archivos ``name`` del proyecto. Normalmente estos archivos suelen ser los objeto, el ejecutable y los archivos de documentación. Es importante no equivocarse, puesto que el borrado con órdenes de shell es irreversible. No suele tener pre-requisitos.
- **``doxy``**: Sirve para crear la documentación del programa (en doxygen en este caso). El objetivo de la regla dependerá del programa de documentación de proyectos que se use en cada caso. No suele tener pre-requisitos.
- **``zip`` o ``tar``**: Estas reglas se utilizan para comprimir el proyecto para su posterior distribución. De tener pre-requisitos, suele ser ``clean``, para eliminar los archivos innecesarios para la distribución del programa.
- **``help``**: Sirve para mostrar un mensaje de ayuda sobre los posibles usos del makefile. Suele tratarse de una sucesión de órdenes shell ``echo``.
- **``author``**: Análoga a ``help``, salvo que esta muestra información sobre el autor del makefile.
- **Reglas sin receta**: Explicadas a continuación.

#### Reglas sin receta

Las reglas sin receta consisten en reglas que no hacen nada ellas mismas. La sintaxis preferente para este tipo de reglas es la que usa punto y coma, puesto que queda más claro que dejar una línea en blanco con un tab. Estas reglas se utilizan para evitar las reglas ímplicitas o para evitar errores para objetivos que se crean como efecto secundario de otras recetas: si el objetivo no existe este tipo de reglas aseguran que ``make`` no de errores al no saber cómo construir el objetivo, y asumirá que está desactualizado.

#### Reglas con múltiples objetivos

Escribir una regla con más de un objetivo es equivalente a escribir múltiples reglas idénticas en las que varía el objetivo. Este tipo de reglas son útiles en dos casos:

- Solamente se quieren pre-requisitos. En este caso a cada uno de los objetivos se le añaden los pre-requisitos de la regla. Por ejemplo:

  ```makefile
  obj/main.o obj/dungeon.o: include/dungeon.h
  ```


- Recetas similares para todos los objetivos: Las recetas no deben de ser completamente idénticas, puesto que el uso de la variable automática ``$@`` puede ser usada para sustituir un objetivo concreto. Este uso será ejemplificado más adelante junto con el uso de las variables.

#### Reglas con pre-requisitos sólo orden

En una regla existen dos tipos de pre-requistos: Los normales y los de sólo orden. Estos pre-requisitos se usan cuando tiene una situación en la que desea imponer un orden específico en las reglas a invocar sin forzar la actualización del objetivo si se ejecuta una de esas reglas. En caso de existir pre-requisitos de ambos tipos, tienen preferencia los de sólo orden. Es por ello que se pueden utilizar también, por ejemplo, para crear un directorio necesario para el objetivo antes de que se ejecuten las recetas. Esta es su sintaxis:

```makefile
objetivos: pre-requisitos | pre-requisitos solo-orden
```

### 1.3 Reglas implícitas

Estas reglas son las que ``make`` puede generar automáticamente si encuentra una regla sin recetas. Dependiendo del lenguaje de programación, puede realizar unas u otras. En el caso de C++, una regla cuyo objetivo sea un ``filename.o`` puede crearlo automáticamente a partir de un ``filename.cpp`` con una receta de la forma:

```makefile
$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c
```

Para resumir y ejemplificar todas estas reglas, se puede ampliar el makefile de ejemplo, quedando de esta forma:

```makefile
all: adventure help author;

rebuild: clean-hard adventure;

obj/main.o obj/dungeon.o: include/dungeon.h

adventure: obj/dungeon.o obj/goblin.o obj/main.o
	g++ -Wall -g -o adventure obj/dungeon.o obj/goblin.o obj/main.o

obj/main.o: src/main.cpp
	g++ -Wall -g -I./include -c src/main.cpp -o obj/main.o

obj/dungeon.o: src/dungeon.cpp
	g++ -Wall -g -I./include -c src/dungeon.cpp -o obj/dungeon.o
	
obj/goblin.o: src/goblin.cpp include/evil.h
	g++ -Wall -g -I./include -c src/goblin.cpp -o obj/goblin.o
	
clean-hard: clean-exec clean-obj clean-doxygen;

clean-exec:
	rm -f adventure

clean-obj:
	rm -f obj/*.o

clean-doxygen:
	rm -rf doc/html doc/latex
	
doxy: clean-doxygen
	doxygen doc/adventure.doxy
	firefox doc/html/index.html
	
zip: clean-hard| zipdir
	-rm -rf zip/*
	zip -r zip/adventure.zip *

zipdir:
	mkdir -p zip/
	
help:
	@echo "Posibles opciones:"
	@echo "		rebuild		Reconstruye el proyecto desde cero"
	@echo "		clean-hard	Borra todos los archivos creados por el makefile"
	@echo "		clean-exec	Borra el ejecutable"
	@echo "		clean-obj	Borra los archivos objeto"
	@echo "		clean-doxyen	Borra los archivos de documentación"
	@echo "		doxy		Genera la documentación del proyecto"
	@echo "		zip		Genera un zip con el proyecto"

author:
	@echo "Este proyecto ha sido creado por Manuel Gachs Ballegeer"
	@echo "https://github.com/Manuelbelgicano"
```

## 2. Variables
