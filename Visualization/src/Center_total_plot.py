import pandas as pd
import matplotlib.pyplot as plt

# Leer los tres archivos CSV
df1 = pd.read_csv('../output/nuevos_200_0_2.csv')
#df2 = pd.read_csv('../output/nuevos_200_0_10.csv')
df3 = pd.read_csv('../output/total_200_0_1.csv')

# Combinar los tres DataFrames en uno solo
combined_df = pd.concat([df1, df3])

# Graficar los datos agrupados por la columna 'velocidad'
plt.figure(figsize=(10, 6))
for velocidad, group in combined_df.groupby('velocidad'):
    plt.plot(group['tiempo'], group['choques'], label=f'Velocidad: {velocidad}')
plt.xlabel('Tiempo [s]', fontsize=16)
plt.ylabel('Choques centrales nuevos', fontsize=16)
plt.legend()
plt.grid(False)
plt.show()
