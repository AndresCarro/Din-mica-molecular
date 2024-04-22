import pandas as pd
import matplotlib.pyplot as plt

# Cargar los datos del archivo CSV
data = pd.read_csv('../output/velocity_c.csv')

# Calcular la velocidad al cuadrado
data['velocity_squared'] = data['velocity'] ** 2

# Graficar
plt.figure(figsize=(10, 6))
plt.errorbar(data['velocity_squared'], data['best_c'], yerr=data['apreciation'], fmt='o')
plt.xlabel('Temperatura(A.U)', fontsize=16)
plt.ylabel('Pendiente', fontsize=16)
plt.grid(False)
plt.show()