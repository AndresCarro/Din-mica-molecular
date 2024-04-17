import csv
import pandas as pd
import numpy as np
import json
import sys
import cv2
import matplotlib.pyplot as plt
from pressure_vs_temperature import CrashSpeed


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../../Simulation/output/'
DEFAULT_INPUT_PATH = '../../Simulation/input/'
AVG_PATH = '../output/'
L = 0
N = 250
SPEED = 3
# ---------------------------------------------------

# ---------------------------------------------------
# CONSTANTES
# ---------------------------------------------------
DELTA_T = 3.78443 * 10**(-3)
MASS = 1
NON_WALL_OBJECTS = ['INIT', 'CENTER', 'NONE']
# ---------------------------------------------------


def calculate_pressure_values(particles_coords):
    time_frames = particles_coords['time'].unique()
    pressure_values = []
    current_time = 0
    delta_t_limit = 0
    current_velocities = []

    for time_frame in time_frames:
        current_particle_coords = particles_coords[particles_coords['time'] == time_frame]
        current_time = time_frame

        if current_time - delta_t_limit > DELTA_T:
            pressure_values.append(sum(current_velocities))
            current_velocities = []
            delta_t_limit += DELTA_T

        # Save delta_v values for particles that have crashed into a wall
        for index, fila in current_particle_coords.iterrows():
            if fila['crash'] not in NON_WALL_OBJECTS and fila['id'] == fila['ParticleA']:
                current_velocities.append(CrashSpeed(fila['vel'], fila['angulo'],
                                                     fila['crash']).calculate_delta_normal_speed() / DELTA_T)
                break
            elif fila['crash'] == 'CENTER':
                pass

    return pressure_values


def main():
    particles_coords = pd.read_csv(OUTPUT_PATH + 'SimulationData_' + str(N) + '_' + str(L)
                                   + "_" + str(SPEED) + '.csv')
    pressure_values = calculate_pressure_values(particles_coords)
    print('Pressure values: ', pressure_values)
    time_values = []

    for index in range(len(pressure_values)):
        time_values.append(index * DELTA_T)

    fig, ax = plt.subplots()
    ax.plot(time_values, pressure_values)

    ax.set_xlabel('Tiempo [s]')
    ax.set_ylabel('Presión')
    ax.set_title('Conchonaro')

    # Step 6: Show or save the graph
    plt.show()


if __name__ == '__main__':
    main()
