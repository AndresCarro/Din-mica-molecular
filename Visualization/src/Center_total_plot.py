import pandas as pd
import matplotlib.pyplot as plt


def read_data(velocities):
    data = []

    for velocity in velocities:
        velocity_int = int(velocity)
        df = pd.read_csv(f'../output/total_200_0_{velocity_int}.csv')
        data.append(df)
    
    return data


def time_corte(data, N_corte):

    for index, row in data.iterrows():
        if row['choques'] >= N_corte:
            return row['tiempo']
    return data['tiempo'].max()


def plot_corte(data, porcentaje, N):
    plt.figure(figsize=(10, 6))

    for index, values in enumerate(data):
        plt.plot(values['tiempo'], values['choques']*(100/N), label=f'Vel: {values["velocidad"][0]}[m/s]')

    plt.axhline(y=porcentaje, color='red', linestyle='--', linewidth=2)
    plt.xlabel('Tiempo [s]', fontsize=16)
    plt.ylabel('Choques nuevos', fontsize=16)
    plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=3)
    plt.grid(False)
    plt.show()

def save_data(velocities, times):
    for index, velocity in enumerate(velocities):
        output = f'../output/velocity_times.csv'
        result_df = pd.DataFrame({'velocity': velocities, 'time': times})
        result_df.to_csv(output, index=False)

def main():

    velocities = [1, 3.6, 10]
    data = read_data(velocities)

    porcentaje = 6
    N = 200
    N_corte = N* (porcentaje/100)
    plot_corte(data, porcentaje, N)

    times = []
    for index, value in enumerate(data):
        time = time_corte(value, N_corte)
        times.append(time)

    save_data(velocities, times)



if __name__ == "__main__":
    main()