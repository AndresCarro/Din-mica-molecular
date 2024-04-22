import pandas as pd
import matplotlib.pyplot as plt


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/SimulationData_'
OUTPUT_CSV = '../output/discreto_'
N = 250
L = 0
V = 1
# ---------------------------------------------------

# Leer el archivo CSV
df = pd.read_csv(OUTPUT_PATH + str(N) + '_' + str(L) + '_' + str(V) + '_v2.csv')
output = OUTPUT_CSV + str(N) + '_' + str(L) + '_' + str(V) + '.csv'

# Inicializar un contador acumulativo
cumulative_count = 0

# Crear listas vacías para almacenar los tiempos y el conteo acumulativo
tiempo = []
d_values = []

x = 0.05
y = 0.05
dt = 0.002
dt_acumulate = 0.002

tiempo_discreto = []
d_values_discreto = []

# Iterar sobre cada fila del DataFrame
for index, row in df.iterrows():
    # Si el valor en la columna 'crash' es igual a 'CENTER', incrementar el contador acumulativo
    if row['id'] == N:
        dx = row['x'] - x
        dy = row['y'] - y
        d = (dx**2 + dy**2)**0.5
        tiempo.append(row['time'])
        d_values.append(d)

        if row['time'] > dt_acumulate:
            d_values_discreto.append(d)
            tiempo_discreto.append(dt_acumulate)
            dt_acumulate += dt


# Graficar los resultados
plt.figure(figsize=(10, 6))
plt.plot(tiempo, d_values)
#plt.axvline(x=0.10, color='red', linestyle='--', linewidth=2)

plt.plot(tiempo_discreto, d_values_discreto, 'ro')
plt.xlabel('Tiempo(s)', fontsize=16)
plt.ylabel('Distancia(m)', fontsize=16)
plt.grid(False)
plt.show()

# Crear un nuevo DataFrame con los tiempos y el conteo acumulativo
result_df = pd.DataFrame({'tiempo': tiempo_discreto, 'd': d_values_discreto})
result_df.to_csv(output, index=False)
