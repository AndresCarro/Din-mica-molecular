import pandas as pd
import matplotlib.pyplot as plt


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/SimulationData_'
OUTPUT_CSV = '../output/total_'
N = 200
L = 0
V = 1
T_ESTACIONARIO = 1000
# ---------------------------------------------------

# Leer el archivo CSV
df = pd.read_csv(OUTPUT_PATH + str(N) + '_' + str(L) + '_' + str(V) + '.csv')
output = OUTPUT_CSV + str(N) + '_' + str(L) + '_' + str(V) + '.csv'

# Inicializar un contador acumulativo
cumulative_count = 0

# Crear listas vacías para almacenar los tiempos y el conteo acumulativo
tiempo = []
choques = []
velocidad = []
mi_set= set()

# Iterar sobre cada fila del DataFrame
for index, row in df.iterrows():
    # Si el valor en la columna 'crash' es igual a 'CENTER', incrementar el contador acumulativo
    if not (row['time'] in mi_set):
        mi_set.add(row['time'])

        if row['crash'] == 'CENTER':
            cumulative_count += 1

        tiempo.append(row['time'])
        choques.append(cumulative_count)
        velocidad.append(V)


# Crear un nuevo DataFrame con los tiempos y el conteo acumulativo
result_df = pd.DataFrame({'tiempo': tiempo, 'choques': choques, 'velocidad': velocidad})

result_df.to_csv(output, index=False)