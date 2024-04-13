import pandas as pd
import matplotlib.pyplot as plt



# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
N = 200
V = 1
# ---------------------------------------------------


def best_c_alg(d, tiempo):

    init_c = 0
    best_c = 0
    max_c = d.max()/tiempo.max() * 1.5
    step = 100
    apreciacion = max_c / step
    min_error = float('inf')


    plt.figure(figsize=(10, 6))

    for step_index in range(step):
        c = init_c + step_index * apreciacion
        error = 0
        for index, d_value in enumerate(d):
            error += (d_value - c*tiempo[index]) ** 2
        plt.scatter(c, error, color='red', s=10)
        if error < min_error:
            min_error = error
            best_c = c

    plt.axvline(x=best_c, color='green', linestyle='--', linewidth=2)

    plt.xlabel('Pendiente', fontsize=16)
    plt.ylabel('Error', fontsize=16)
    plt.show()
    return best_c, apreciacion


# Leer los tres archivos CSV
df1 = pd.read_csv(f'../output/discreto_{N}_0_{V}A.csv')
df2 = pd.read_csv(f'../output/discreto_{N}_0_{V}B.csv')
df3 = pd.read_csv(f'../output/discreto_{N}_0_{V}C.csv')

# Combinar los tres DataFrames en uno solo
df = pd.concat([df1, df2, df3])
df['d'] = df['d'] * df['d']

# Agrupa los valores por tiempo y calcula el promedio y la desviación estándar
grouped = df.groupby('tiempo')['d'].agg(['mean', 'std'])

# Extrae los valores de tiempo, promedio y desviación estándar
tiempo = grouped.index
promedio = grouped['mean']
desviacion_estandar = grouped['std']

# Calcula la mejor pendiente para los datos
best_c, apreciacion = best_c_alg(promedio, tiempo)
linea = best_c * tiempo

# Grafica el promedio con desviación estándar como error
plt.figure(figsize=(10, 6))
plt.plot(tiempo, linea, color='red', linestyle='--')
plt.plot(df1['tiempo'], df1['d']**2, linestyle='--', color='black')
plt.plot(df2['tiempo'], df2['d']**2, linestyle='--', color='black')
plt.plot(df3['tiempo'], df3['d']**2, linestyle='--', color='black')
plt.errorbar(tiempo, promedio, yerr=desviacion_estandar, fmt='o')
plt.xlabel('Tiempo[s]', fontsize=16)
plt.ylabel('Distancia media[m]', fontsize=16)
plt.grid(False)
plt.show()

output = f'../output/Difusion_{V}.csv'
result_df = pd.DataFrame({'velocity': [V], 'best_c': [best_c], 'apreciation': [apreciacion]})
result_df.to_csv(output, index=False)