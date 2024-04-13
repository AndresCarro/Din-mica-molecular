import pandas as pd
import matplotlib.pyplot as plt


def read_data():
    # Leer los tres archivos CSV
    df1 = pd.read_csv('../output/nuevos_200_0_1.csv')
    #df2 = pd.read_csv('../output/nuevos_200_0_3.csv')
    #df3 = pd.read_csv('../output/total_200_0_10.csv')

    # Combinar los tres DataFrames en uno solo
    #combined_df = pd.concat([df1, df2, df3])
    data = [df1]

    return data


def best_c_alg(data):

    init_c = 0
    best_c = 0
    max_c = 300
    apreciacion = 10
    min_error = float('inf')
    step = int(max_c / apreciacion)

    plt.figure(figsize=(10, 6))

    for step_index in range(step):
        c = init_c + step_index * apreciacion
        error = 0
        for index, row in data.iterrows():
            error += (row['choques'] - c*row['tiempo']) ** 2
        plt.scatter(c, error, color='red', s=10)
        if error < min_error:
            min_error = error
            best_c = c

    plt.axvline(x=best_c, color='green', linestyle='--', linewidth=2)

    plt.xlabel('Pendiente', fontsize=16)
    plt.ylabel('Error', fontsize=16)
    plt.show()
    return best_c


def plot_c_data(combined_df, m):
    tiempo_recta = combined_df['tiempo']
    choques_recta = m * tiempo_recta

    # Graficar los datos agrupados por la columna 'velocidad' y la recta con pendiente m
    plt.figure(figsize=(10, 6))

    for velocidad, group in combined_df.groupby('velocidad'):
        plt.plot(group['tiempo'], group['choques'], label=f'Velocidad: {velocidad}')

    # Agregar la línea recta al gráfico
    plt.plot(tiempo_recta, choques_recta, color='black', linestyle='--', label=f'Recta de pendiente {m}')

    plt.xlabel('Tiempo [s]', fontsize=16)
    plt.ylabel('Choques centrales nuevos', fontsize=16)
    plt.legend()
    plt.grid(False)
    plt.show()


def main():

    data = read_data()

    for value in data:
        best_c = best_c_alg(value)

        plot_c_data(value, best_c)


if __name__ == "__main__":
    main()
