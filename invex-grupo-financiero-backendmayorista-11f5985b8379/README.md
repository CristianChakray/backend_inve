Pasos para crear tu microservicio con el arqueotipo.

1. Confirma si al clonar el proyecto del repositorio trae la carpeta `target\generated-sources\archetype`
2. Si trae carpeta navega hasta la ruta `target\generated-sources\archetype` si no lo trae realiza el paso 5
3. Publica el arqueotipo a tu repositorio local de maven con el comando `mvn clean install` dentro de la ruta especificada anterior.
4. Crea tu microservicio con el arquetipo ejecutando el comando siguiente: 

mvn archetype:generate "-DinteractiveMode=false" "-DarchetypeGroupId=com.invex" "-DarchetypeArtifactId=api-microservice-archetype" "-DarchetypeVersion=0.0.1-SNAPSHOT" "-DgroupId=com.invex.{nombreDeTuProyecto}" "-DartifactId=api-{nombreDeTuProyecto}-microservice" "-Dversion=0.0.1-SNAPSHOT" "-DdomainName={nombreDeTuProyecto}"

Notas:
* Preferentemente  ejecuta el comando en una ruta diferente a la del arqueotipo. Si el proyecto del arqueotipo esta en la ruta `C:\Workspace` usa el comando en una ruta diferente.
* Sustituye la variable {nombreDeTuProyecto} por el nombre de tu microservicio 
ejemplo:
mvn archetype:generate "-DinteractiveMode=false" "-DarchetypeGroupId=com.invex" "-DarchetypeArtifactId=api-microservice-archetype" "-DarchetypeVersion=0.0.1-SNAPSHOT" "-DgroupId=com.invex.transferencias" "-DartifactId=api-transferencias-microservice" "-DdomainName=transferencias" "-Dversion=0.0.1-SNAPSHOT"

* El ejemplo del comando `mvn archetype:generate` proporcionado se ejecuta en la terminal de Power Shell en caso de algun error intenten quitando las comillas dobles.

5. Si no tiene la carpeta mencionada en el paso 2 ejecuta el comando `mvn clean install` y luego `mvn archetype:create-from-project` este ultimo creara la carpeta y podras continuar con el paso numero 3

Nota: Si el paso 5 te genera un error coloca el archivo `settings.xml` que se encuentra dentro del proyecto `src\main\resources\settings.xml` en la carpeta .m2 de maven suele estar en: `C:\Users\{nombreDeTuUsuarioPC}\.m2`