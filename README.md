# Dinamica regida por eventos #

Repositorio correspondiente al tercer trabajo practico de la materia Simulacion de sistemas - 72.25 en el cual se simula y analiza el comportamiento de un sistema dirigido por eventos.

## Ejecución

### Simulación y visualización
Para realizar una simulación revisar que el archvio **_Simulation/input/input.json_** contenga los valores que desea introducir en la simulación

````json
{
  "L": "Largo de la grilla del sistema",
  "N": "Cantidad de particulas",
  "speed": "Velocidad de las particulas",
  "totalCrash": "Cantidad de colisiones totales",
  "frameCrash": "registo cada cierto numero de colisiones",
  "movable": "La particula central es movible o no"
}
````
Una vez establecidos estos valores correr la función main del codigo java, el cual generará un archivo **_SimulationData_N_L_Speed.csv_** con los datos dinamicos de la simulación y un archivo **_StateData_N_L_Speed.csv_** con los datos de estado de la simulación.

Si se desea visualizar la animación dirigirse a la carpeta visualization y correr el comando

````bash
python3 main_simulation.py
````

### Analisis de Presion

Para realizar este analisis se provee la función **_NoiseMultiSimulation()_** dentro del codigo java. Esta función correrá todas las simulaciones necesarias para el posterior analisis.

Una vez realizadas las simulaciones necesarias dirigirse a la carpeta visualization y correr el comando sigiente para ver la evolución temporal de la presión.

````bash
python3 pressure_vs_time.py
````

Si se desea coloreadas las particulas que colisionan con el obstaculo se puede agregar el agrgumento infected a la ejecucion del archivo.

````bash
python3 pressure_vs_time.py infected
````


y posteriormente para revisar la correspondencia con la temperatura (Velocidad al cuadrado)

````bash
python3 pressure_vs_temperature.py
````

### Analisis de Choques

Una vez realizadas las simulaciones necesarias dirigirse a la carpeta visualization y correr alguno de los comandos segun se quiera ver el numero total de choques o el numero de choques nuevos

````bash
python3 Center_nuevo_data.py
python3 Center_total_data.py
````

Este script mostrará la variación del numero de choques en el tiempo, para revisar como es el comportamiento para diferentes temperaturas correr el comando siguiente según que se elegió previamente

````bash
python3 Center_nuevo_plot.py
python3 Center_total_plot.py
````

### Analisis de Coeficiente de difusión

Una vez realizadas las simulaciones necesarias dirigirse a la carpeta visualization y correr el comando

````bash
python3 Difusion_discretizar.py
````

Este script va a discretizar la posicion relativa de la particula central para tiempos discretos. Teniendo varias de estas simulaciones correr el siguiente comando para tener la distancia media

````bash
python3 Difusion_linearizar.py
````

Una vez con estos datos, repetir estos pasos para diferentes vlocidades y correr el siguiente comando para tener el coeficiente para diferentes velocidades.

````bash
python3 Difusion_final.py
````

## Autores

- 62500 - [Gastón Ariel Francois](https://github.com/francoisgaston)
- 62655 - [Andres Carro Wetsel](https://github.com/AndresCarro)
