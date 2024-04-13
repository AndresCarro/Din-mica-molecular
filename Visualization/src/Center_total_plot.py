import pandas as pd
import matplotlib.pyplot as plt


def read_data(velocities):
    data = []

    for velocity in velocities:
        velocity_int = int(velocity)
        df = pd.read_csv(f'../output/total_200_0_{velocity_int}.csv')
        data.append(df)
    
    return data


def best_c_alg(data):

    init_c = 0
    best_c = 0
    max_c = data['choques'].max()/data['tiempo'].max() * 1.5
    step = 100
    apreciacion = max_c / step
    min_error = float('inf')


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
    return best_c, apreciacion


def plot_c_data(data, best_c):
    plt.figure(figsize=(10, 6))

    for index, values in enumerate(data):
        tiempo_recta = values['tiempo']
        choques_recta = best_c[index] * tiempo_recta

        plt.plot(values['tiempo'], values['choques'], label=f'Vel: {values["velocidad"][0]}[m/s]')

        plt.plot(tiempo_recta, choques_recta, color='black', linestyle='--')

    plt.xlabel('Tiempo [s]', fontsize=16)
    plt.ylabel('Choques nuevos', fontsize=16)
    plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=3)
    plt.grid(False)
    plt.show()

def save_data(velocities, best_c, apreciations):
    for index, velocity in enumerate(velocities):
        output = f'../output/velocity_c.csv'
        result_df = pd.DataFrame({'velocity': velocities, 'best_c': best_c, 'apreciation': apreciations})
        result_df.to_csv(output, index=False)

def main():

    velocities = [1, 3.6, 10]

    data = read_data(velocities)
    best_c = []
    apreciations = []

    for index, value in enumerate(data):
        c, apreciation = best_c_alg(value)
        best_c.append(c)
        apreciations.append(apreciation)

    plot_c_data(data, best_c)

    save_data(velocities, best_c, apreciations)



if __name__ == "__main__":
    main()
