import pandas as pd
import matplotlib.pyplot as plt

# Cargar los datos del archivo CSV
data = pd.read_csv('../output/velocity_times.csv')

grouped = data.groupby('velocity')['time'].agg(['mean', 'std']).reset_index()

# Calcular la velocidad al cuadrado
grouped['velocity_squared'] = grouped['velocity'] ** 2

# Graficar
plt.figure(figsize=(10, 6))
plt.errorbar(grouped['velocity_squared'], grouped['mean'], yerr=grouped['std'], fmt='o')
plt.xlabel('Temperatura (A.U)', fontsize=16)
plt.ylabel('Tiempo (s)', fontsize=16)
plt.grid(False)
plt.show()
