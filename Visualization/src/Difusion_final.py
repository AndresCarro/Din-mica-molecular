import pandas as pd
import matplotlib.pyplot as plt


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
N = 250
# ---------------------------------------------------

# Leer los tres archivos CSV
df1 = pd.read_csv(f'../output/DifusionData_1.csv')
df2 = pd.read_csv(f'../output/DifusionData_3.csv')
df3 = pd.read_csv(f'../output/DifusionData_10.csv')

df1c = pd.read_csv(f'../output/Difusion_1.csv')
df2c = pd.read_csv(f'../output/Difusion_3.csv')
df3c = pd.read_csv(f'../output/Difusion_10.csv')

c1 = df1c['best_c'].mean()
c2 = df2c['best_c'].mean()
c3 = df3c['best_c'].mean()

p1 = c1 * df1['tiempo']
p2 = c2 * df2['tiempo']
p3 = c3 * df3['tiempo']

plt.figure(figsize=(10, 6))

plt.plot(df1['tiempo'], p1, color='red', linestyle='--')
plt.plot(df2['tiempo'], p2, color='red', linestyle='--')
plt.plot(df3['tiempo'], p3, color='red', linestyle='--')

plt.errorbar(df1['tiempo'], df1['values'], yerr=0, fmt='o', label='V=1[m/s]')
plt.errorbar(df2['tiempo'], df2['values'], yerr=0, fmt='o', label='V=3[m/s]')
plt.errorbar(df3['tiempo'], df3['values'], yerr=0, fmt='o', label='V=10[m/s]')
plt.xlabel('Tiempo(s)', fontsize=16)
plt.ylabel('Distancia cuadratica media(m)', fontsize=16)
plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=3)

plt.grid(False)
plt.show()