import pandas as pd
import matplotlib.pyplot as plt

# Cargar el CSV en un DataFrame
df = pd.read_csv("../../Simulation/output/SimulationData_20_0.csv")

# Agrupar por tiempo y contar la cantidad de veces que crash es CENTER
crash_counts = df.groupby('time')['crash'].apply(lambda x: (x == 'CENTER').sum()).reset_index()

# Graficar
plt.figure(figsize=(10, 6))
plt.plot(crash_counts['time'], crash_counts['crash'], marker='o', linestyle='-')
plt.title('Cantidad de veces que "crash" es CENTER por tiempo')
plt.xlabel('Tiempo')
plt.ylabel('Cantidad de veces que "crash" es CENTER')
plt.grid(True)
plt.show()