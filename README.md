# AREP_ModularizacionDocker_Lab05

## AWS URL
[AWS](http://ec2-54-234-55-207.compute-1.amazonaws.com:9000/)

## Requisitos
* Git
* Java 8
* Maven

## Instalación
1. Abrimos una terminal
2. Clonamos el repositorio
```
git clone https://github.com/luisalejandrojaramillo/AREP_ModularizacionDocker_Lab05
```
3. Entramos al directorio
```
cd AREP_ModularizacionDocker_Lab05/LoadBalancer
```
4. Empaquetamos
```
mvn package
```
5. Ejecutamos
```
java -cp ./classes:./dependency/* co.edu.escuelaing.App
```
## Arquitectura
![Arquitectura](/img/ArqDocker.PNG)

En la imagen mostrada anteriormente, podemos ver el diagrama de componentes, en el cual se
busca construir una aplicaci´on desplegada en AWS usando EC2 y Docker, vamos a tener una base de datos 
MondoDB, la cual va a correr en un contenedor de Docker, un servicio de Logs el cual es
un servicio REST, este recibe una cadena la cual se almacena en la base de datosy responde con un
objeto JSON con las 10 ultimas cadenas almacenadas en la base de datos y la fecha en la que estos
fueron almacenados. La aplicaci´on web APP-LB-RoundRobin est´a compuesta por un cliente web y
al menos un servicio REST. El cliente web tiene un campo y un bot´on y cada vez que el usuario
env´ıa un mensaje, este se lo env´ıa al servicio REST y actualiza la pantalla con la informaci´on que
este le regresa en formato JSON. El servicio REST recibe la cadena e implementa un algoritmo
de balanceo de cargas de Round Robin, delegando el procesamiento del mensaje y el retorno de la
respuesta a cada una de las tres instancias del servicio LogService.

## Instructivo

* Abrimos la URL

![Step1](/img/p1.PNG)

* Agreegamos un nuevo Log

![Step2](/img/p2.PNG)

* Notamos que se actualiza

![Step3](/img/p3.PNG)

* Podemos revisar los Logs

![Step4](/img/p4.PNG)

## Informe
[Informe](/Lab05AWSDocker.pdf)

## License
[MIT License ](/LICENSE)
## Autor
Luis Alejandro Jaramillo Rincon
